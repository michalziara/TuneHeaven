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
@Table(name = "ARTIST", schema = "C##TUNE")
public class Artist {
    @Id
    @Column(name = "ARTIST_ID")
    private String artist_id;
    @Column(name = "ARTIST_NAME")
    private String artist_name;

    @OneToMany(mappedBy = "artist_id", cascade = CascadeType.ALL)
    private List<Song> songs = new ArrayList<>();
}
