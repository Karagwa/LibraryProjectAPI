package org.encode.libraryprojectapi.model.request;

import lombok.Data;

@Data
public class BookCreationRequest {
    private String name;
    private String isbn;
    private String authorId;
}
