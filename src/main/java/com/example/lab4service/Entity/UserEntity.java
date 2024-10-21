package com.example.lab4service.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@RequiredArgsConstructor
@Getter
public class UserEntity {
    @NonNull
    @Id
    String login;
    @NonNull
    String name;
    @NonNull
    int age;

    int RegisteredObjects=0;
}
