package org.encode.libraryprojectapi.model.request;

import lombok.Data;

@Data
public class LibrarianCreationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
