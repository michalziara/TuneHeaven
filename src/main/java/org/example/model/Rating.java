package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RATING", schema = "C##TUNE")
public class Rating {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;
    @Column(name = "USER_ID")
    private String user_id;
    @Column(name = "SONG_ID")
    private String song_id;
    @Column(name = "RATING")
    private int rating;

    @Temporal(TemporalType.DATE)
    @Column(name = "RATING_DATE")
    private Date rating_date;

}
