package org.example.repository;

import org.example.dto.SongMonthlyRating;
import org.example.dto.SongRating;
import org.example.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    @Query("SELECT r FROM Rating r WHERE r.user_id = :userId AND r.song_id = :songId")
    Optional<Rating> findByUserIdAndSongId(@Param("userId") String userId, @Param("songId") String songId);

    @Query("SELECT new org.example.dto.SongRating(r.song_id, AVG(r.rating)) FROM Rating r WHERE r.rating_date BETWEEN :start AND :end GROUP BY r.song_id")
    List<SongRating> findAverageRatingBySongIdBetweenDates(@Param("start") Date start, @Param("end") Date end);

    @Query("SELECT r FROM Rating r WHERE r.song_id = :songId AND r.rating_date >= :since AND r.rating_date <= :until")
    List<Rating> findBySongIdAndDateBetween(
            @Param("songId") String songId,
            @Param("since") Date since,
            @Param("until") Date until
    );

    @Query("SELECT new org.example.dto.SongMonthlyRating(r.song_id, AVG(r.rating), cast(:month as string)) " +
            "FROM Rating r WHERE r.song_id = :songId AND r.rating_date BETWEEN :start AND :end GROUP BY r.song_id")
    List<SongMonthlyRating> findAverageRatingForSongByMonth(@Param("songId") String songId,
                                                            @Param("start") Date start,
                                                            @Param("end") Date end,
                                                            @Param("month") String month);
}
