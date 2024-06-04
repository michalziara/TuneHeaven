package org.example.service;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Artist;
import org.example.model.Rating;
import org.example.model.Song;
import org.example.model.User;
import org.example.repository.ArtistRepository;
import org.example.repository.RatingRepository;
import org.example.repository.SongRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CsvImportService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Value("${csvFolderPath}")
    private String csvFolderPath;

    public void importCsvFiles() throws Exception {
        File folder = new File(csvFolderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Podana ścieżka nie jest folderem.");
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    importCsv(file);
                }
            }
        }
    }
    @Transactional
    void importCsv(File csvFile) throws Exception {
        Date ratingDate = new Date();
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            boolean firstLine = true;
            Map<String, Integer> headerMap = new HashMap<>();
            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    for (int i = 0; i < line.length; i++) {
                        headerMap.put(line[i], i);
                    }
                    firstLine = false;
                    continue;
                }
                String songId = line[headerMap.get("song_id")];
                String artistId = line[headerMap.get("artist_id")];
                String userId = line[headerMap.get("user_id")];

                User user = userRepository.findById(userId).orElse(new User());
                user.setId(userId);
                userRepository.save(user);

                Artist artist = artistRepository.findById(artistId).orElse(new Artist());
                artist.setArtist_id(artistId);
                artist.setArtist_name(line[headerMap.get("artist_name")]);
                artistRepository.save(artist);

                Song song = songRepository.findById(songId).orElse(new Song());
                song.setId(songId);
                song.setSong_name(line[headerMap.get("song_name")]);
                song.setArtist_id(artistId);
                song.setGenre(line[headerMap.get("genre")]);
                songRepository.save(song);

                Rating rating = ratingRepository.findByUserIdAndSongId(userId, songId).orElse(new Rating());
                rating.setUser_id(userId);
                rating.setSong_id(songId);
                rating.setRating(Integer.parseInt(line[headerMap.get("review_rating")]));
                rating.setRating_date(ratingDate);
                ratingRepository.save(rating);
            }
        }
    }
}
