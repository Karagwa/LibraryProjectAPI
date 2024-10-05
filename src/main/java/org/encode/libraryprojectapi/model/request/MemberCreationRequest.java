package org.encode.libraryprojectapi.model.request;

import lombok.Data;
import org.encode.libraryprojectapi.model.Role;

@Data
public class MemberCreationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

}
