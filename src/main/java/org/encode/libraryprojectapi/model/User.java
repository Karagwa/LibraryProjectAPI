package org.encode.libraryprojectapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.GeneratedValue;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Getter
@Setter

@Document(collection = "users")

public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String username;
    private String password;
    private Role role;
    private List<String> borrowedBooks;

}