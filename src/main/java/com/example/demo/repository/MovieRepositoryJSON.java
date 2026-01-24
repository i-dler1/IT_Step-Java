package com.example.demo.repository;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MovieRepositoryJSON implements MovieRepository {

    @Setter
    private String movie;

    @Override
    public List<Movie> getAllMovies() {
        try {
            return newMapper().readValue(new File(movie),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error reading movie data from: " + movie, e);
        }
    }

    @Override
    public List<Movie> findByGenre(String genre) {
        return getAllMovies()
                .stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByRating(double minRating, double maxRating) {
        return getAllMovies()
                .stream()
                .filter(movie -> movie.getRating() >= minRating
                        && movie.getRating() <= maxRating)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> findMovieByYear(int minYear, int maxYear) {
        return getAllMovies()
                .stream()
                .filter(m -> m.getYear() >= minYear
                        && m.getYear() <= maxYear)
                .collect(Collectors.toList());
    }

    private ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setLocale(Locale.ENGLISH);
        return mapper;
    }

}
