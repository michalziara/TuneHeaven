package org.example.controller;

import org.example.dto.SongMonthlyRating;
import org.example.model.Rating;
import org.example.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingRepository ratingRepository;

    @Test
    public void testGetAverageRating() throws Exception {
        String songId = "6ab3aa00-3c6a-11ee-be56-0242ac120002";
        String userId = "6ab3aa00-3c6a-11ee-be56-0242ac120002";
        String since = "20230123";
        String until = "20230421";
        List<Rating> ratings = Arrays.asList(new Rating("1", userId, songId, 4, new Date()),
                new Rating("2", userId, songId, 5, new Date()));
        when(ratingRepository.findBySongIdAndDateBetween(songId, new Date(), new Date())).thenReturn(ratings);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/{songId}/avg", songId)
                .param("since", since)
                .param("until", until));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testGetAverageRatingForThreeMonths() throws Exception {
        String songId = "6ab3aa00-3c6a-11ee-be56-0242ac120002";
        SongMonthlyRating rating1 = new SongMonthlyRating(songId, 4.5, "202303");
        SongMonthlyRating rating2 = new SongMonthlyRating(songId, 3.0, "202302");
        SongMonthlyRating rating3 = new SongMonthlyRating(songId, 5.0, "202301");
        List<SongMonthlyRating> ratings = Arrays.asList(rating1, rating2, rating3);
        when(ratingRepository.findAverageRatingForSongByMonth(songId, null, null, null)).thenReturn(ratings);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/{songId}/avg-three-months", songId));
        resultActions.andExpect(status().isOk());
    }
}
