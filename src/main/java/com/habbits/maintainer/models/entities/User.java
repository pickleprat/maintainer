package com.habbits.maintainer.models.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="users")
public class User {
}
