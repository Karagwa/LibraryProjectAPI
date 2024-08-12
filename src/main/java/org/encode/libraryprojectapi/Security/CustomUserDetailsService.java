package org.encode.libraryprojectapi.Security;

import lombok.RequiredArgsConstructor;
import org.encode.libraryprojectapi.model.User;
import org.encode.libraryprojectapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findByUsername(username);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            // Return all users found
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (User user : users) {
                authorities.add(new SimpleGrantedAuthority((user.getRole()).name()));
            }
            // Use the first user's username and password, or implement a custom logic
            return new org.springframework.security.core.userdetails.User(users.get(0).getUsername(), users.get(0).getPassword(), authorities);
        }
    }


    public void saveUser(User user) {
        // Encode password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
