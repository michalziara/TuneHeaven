package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "SONG", schema = "C##TUNE")
public class Song {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "SONG_NAME")
    private String song_name;
    @Column(name = "ARTIST_ID")
    private String artist_id;
    @Column(name = "GENRE")
    private String genre;

    @OneToMany(mappedBy = "song_id", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();
}