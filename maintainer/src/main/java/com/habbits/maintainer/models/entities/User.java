package com.habbits.maintainer.models.entities;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection="users")
public class User {
    // no gender because we here do not support sexism
    // and totally not because I forgot and now it will be too much work
    // to change it or anything.
    @Id private ObjectId id;
    @Indexed(unique = true) @NonNull private String userName;
    @NonNull private String password;
    @NonNull private String zipCode;
    @NonNull private String countryCode;
    @NonNull private String phoneNumber;
    @NonNull private String city;
    @NonNull private String address;
    @NonNull private String firstName;
    @NonNull private String lastName;
    private String middleName;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
