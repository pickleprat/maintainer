package com.habbits.maintainer.models.entities;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Hobbys:  is a fancy keyword to declare the hobby entity
 */

@Data
@Document(collection="hobbys")
public class Hobby {
    @Id private ObjectId id;
    @Indexed(unique = true) private String title;
    @NonNull private String category;
    @NonNull private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
