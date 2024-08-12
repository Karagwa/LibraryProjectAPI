package org.encode.libraryprojectapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection="members")
public class Member {
    @Id
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private MemberStatus status;
    private List<String> borrowedBooks= new ArrayList<>();
}
