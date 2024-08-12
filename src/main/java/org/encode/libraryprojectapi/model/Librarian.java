package org.encode.libraryprojectapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="librarians")
public class Librarian {
    @Id
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
