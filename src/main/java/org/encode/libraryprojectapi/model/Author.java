package org.encode.libraryprojectapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@Document(collection = "authors")
public class Author {
    @Id
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'authorId':?#{#self._id}}")
    private String authorId;

    private String firstName;
    private String lastName;

}
