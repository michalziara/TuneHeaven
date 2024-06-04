package org.example.controller;

import org.example.dto.SongMonthlyRating;
import org.example.model.Rating;
import org.example.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.example.util.CalendarUtil.getEndOfMonth;
import static org.example.util.CalendarUtil.getStartOfMonth;

@RestController
@RequestMapping("/api")
public class RatingController {
    @Autowired
    private RatingRepository ratingRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMM");

    @GetMapping("/{songId}/avg")
    public double getAverageRating(
            @PathVariable String songId,
            @RequestParam String since,
            @RequestParam String until
    ) throws ParseException {
        Date dateSince = dateFormat.parse(since);
        Date dateUntil = dateFormat.parse(until);
        List<Rating> ratings = ratingRepository.findBySongIdAndDateBetween(songId, dateSince, dateUntil);
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }

    @GetMapping("/{songId}/avg-three-months")
    public List<SongMonthlyRating> getAverageRatingForThreeMonths(@PathVariable String songId) {
        Calendar cal = Calendar.getInstance();

        List<SongMonthlyRating> ratings = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            cal.add(Calendar.MONTH, -1);
            String month = shortDateFormat.format(cal.getTime());
            Date startOfMonth = getStartOfMonth(cal.getTime());
            Date endOfMonth = getEndOfMonth(cal.getTime());

            List<SongMonthlyRating> monthlyRatings = ratingRepository.findAverageRatingForSongByMonth(songId, startOfMonth, endOfMonth, month);
            if (!monthlyRatings.isEmpty()) {
                ratings.add(monthlyRatings.get(0));
            } else {
                ratings.add(new SongMonthlyRating(songId, 0.0, month));
            }
        }

        return ratings;
    }
}
