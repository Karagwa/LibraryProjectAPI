package org.encode.libraryprojectapi.controller;


import lombok.RequiredArgsConstructor;
import org.encode.libraryprojectapi.Security.CustomUserDetailsService;
import org.encode.libraryprojectapi.model.User;
import org.encode.libraryprojectapi.model.request.AuthenticationRequest;
import org.encode.libraryprojectapi.model.request.AuthenticationResponse;
import org.encode.libraryprojectapi.model.Librarian;
import org.encode.libraryprojectapi.model.Member;
import org.encode.libraryprojectapi.model.request.LibrarianCreationRequest;
import org.encode.libraryprojectapi.model.request.MemberCreationRequest;
import org.encode.libraryprojectapi.repository.LibrarianRepository;
import org.encode.libraryprojectapi.repository.MemberRepository;
import org.encode.libraryprojectapi.service.LibraryService;
import org.encode.libraryprojectapi.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final LibraryService libraryService;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//            );
//        } catch (Exception e) {
//            throw new Exception("Incorrect username or password", e);
//        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            if (!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new Exception("Incorrect username or password");
        }

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register/member")
    public ResponseEntity<String> registerMember(@RequestBody Member member) {
        member.setPassword(new BCryptPasswordEncoder().encode(member.getPassword()));
        memberRepository.save(member);
        return ResponseEntity.ok("Member registered successfully");
    }

    @PostMapping("/register/librarian")
    public ResponseEntity<String> registerLibrarian(@RequestBody Librarian librarian) {
        librarian.setPassword(new BCryptPasswordEncoder().encode(librarian.getPassword()));
        librarianRepository.save(librarian);
        return ResponseEntity.ok("Librarian registered successfully");
    }

    @PostMapping("/signup/librarian")

    public ResponseEntity<String> createLibrarian(@RequestBody LibrarianCreationRequest request) {
        logger.info("Creating new librarian(s) with request: {}", request);
        Librarian librarian = new Librarian();
        librarian.setFirstName(request.getFirstName());
        librarian.setLastName(request.getLastName());

        return ResponseEntity.ok(libraryService.signUp(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        customUserDetailsService.saveUser(user);
        return ResponseEntity.ok("User created successfully");
    }


}
