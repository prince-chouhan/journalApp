package net.majorProject.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Builder
@Document(collection = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    @DBRef
    @Builder.Default
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles;


}
