package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USER", schema = "C##TUNE")
public class User {
    @Id
    @Column(name = "ID")
    private String id;

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();
}
