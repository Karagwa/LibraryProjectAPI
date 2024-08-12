package org.encode.libraryprojectapi.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.encode.libraryprojectapi.model.Author;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class BookCreationRequest {
    private String title;
    private String isbn;

    private String authorId;
}
