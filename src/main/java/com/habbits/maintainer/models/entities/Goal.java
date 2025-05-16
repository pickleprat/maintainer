package com.habbits.maintainer.models.entities;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "goals")
public class Goal {
    @Id private ObjectId id;
    @NonNull private String objective;
    @NonNull private Period unit;
    private int steps;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    @DBRef private User user;
    @DBRef private Hobby hobby;
}
