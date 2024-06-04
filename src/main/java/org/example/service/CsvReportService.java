package org.example.service;

import org.example.dto.SongRating;
import org.example.model.Song;
import org.example.repository.RatingRepository;
import org.example.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.util.CalendarUtil.getEndOfMonth;
import static org.example.util.CalendarUtil.getStartOfMonth;

@Service
public class CsvReportService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private SongRepository songRepository;

    @Value("${csvExportPath}")
    private String csvExportPath;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");

    public void generateMonthlyReports() throws IOException {
        Calendar cal = Calendar.getInstance();
        Date startOfCurrentMonth = getStartOfMonth(cal.getTime());
        Date endOfCurrentMonth = getEndOfMonth(cal.getTime());

        cal.add(Calendar.MONTH, -1);
        Date startOfPreviousMonth = getStartOfMonth(cal.getTime());
        Date endOfPreviousMonth = getEndOfMonth(cal.getTime());

        cal.add(Calendar.MONTH, -1);
        Date startOfTwoMonthsBack = getStartOfMonth(cal.getTime());
        Date endOfTwoMonthsBack = getEndOfMonth(cal.getTime());

        List<SongRating> currentMonthRatings = ratingRepository.findAverageRatingBySongIdBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        List<SongRating> previousMonthRatings = ratingRepository.findAverageRatingBySongIdBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        List<SongRating> twoMonthsBackRatings = ratingRepository.findAverageRatingBySongIdBetweenDates(startOfTwoMonthsBack, endOfTwoMonthsBack);

        Map<String, Double> previousMonthRatingsMap = previousMonthRatings.stream()
                .collect(Collectors.toMap(SongRating::getSongId, SongRating::getAverageRating));
        Map<String, Double> twoMonthsBackRatingsMap = twoMonthsBackRatings.stream()
                .collect(Collectors.toMap(SongRating::getSongId, SongRating::getAverageRating));

        generateTrendingSongsReport(currentMonthRatings, previousMonthRatingsMap, twoMonthsBackRatingsMap);
        generateLoosingSongsReport(currentMonthRatings, previousMonthRatingsMap, twoMonthsBackRatingsMap);
    }

    private void generateTrendingSongsReport(List<SongRating> currentMonthRatings,
                                             Map<String, Double> previousMonthRatingsMap,
                                             Map<String, Double> twoMonthsBackRatingsMap) throws IOException {
        String fileName = "trending100songs-" + dateFormat.format(new Date()) + ".csv";
        try (FileWriter writer = new FileWriter(csvExportPath + "/" + fileName)) {
            writer.append("song_name,song_uuid,rating_this_month,rating_previous_month,rating_2months_back\n");
            currentMonthRatings.stream()
                    .sorted((a, b) -> Double.compare(
                            b.getAverageRating() - previousMonthRatingsMap.getOrDefault(b.getSongId(), 0.0),
                            a.getAverageRating() - previousMonthRatingsMap.getOrDefault(a.getSongId(), 0.0)
                    ))
                    .limit(100)
                    .forEach(rating -> {
                        Song song = songRepository.findById(rating.getSongId()).orElse(null);
                        if (song != null) {
                            try {
                                writer.append(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                                        song.getSong_name(),
                                        song.getId(),
                                        rating.getAverageRating(),
                                        previousMonthRatingsMap.getOrDefault(rating.getSongId(), 0.0),
                                        twoMonthsBackRatingsMap.getOrDefault(rating.getSongId(), 0.0)
                                ));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void generateLoosingSongsReport(List<SongRating> currentMonthRatings,
                                            Map<String, Double> previousMonthRatingsMap,
                                            Map<String, Double> twoMonthsBackRatingsMap) throws IOException {
        String fileName = "songs-loosing-" + dateFormat.format(new Date()) + ".csv";
        try (FileWriter writer = new FileWriter(csvExportPath + "/" + fileName)) {
            writer.append("song_name,song_uuid,rating_this_month,rating_previous_month,rating_2months_back\n");
            currentMonthRatings.stream()
                    .filter(rating ->
                            previousMonthRatingsMap.containsKey(rating.getSongId()) &&
                                    (previousMonthRatingsMap.get(rating.getSongId()) - rating.getAverageRating()) >= 0.4
                    )
                    .forEach(rating -> {
                        Song song = songRepository.findById(rating.getSongId()).orElse(null);
                        if (song != null) {
                            try {
                                writer.append(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                                        song.getSong_name(),
                                        song.getId(),
                                        rating.getAverageRating(),
                                        previousMonthRatingsMap.get(rating.getSongId()),
                                        twoMonthsBackRatingsMap.getOrDefault(rating.getSongId(), 0.0)
                                ));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
