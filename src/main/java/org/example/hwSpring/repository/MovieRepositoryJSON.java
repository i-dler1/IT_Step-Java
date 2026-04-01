package org.example.hwSpring.repository;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.hwSpring.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MovieRepositoryJSON implements MovieRepository {

    @Value("${movie.data.file}")
    private String movieFilePath;

    @Override
    public List<Movie> getAllMovies() {
        try {
            return newMapper().readValue(new File(movieFilePath),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error reading movie data from: " + movieFilePath, e);
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
    public List<Movie> findByYear(int minYear, int maxYear) {
        return getAllMovies()
                .stream()
                .filter(m -> m.getYear() >= minYear
                        && m.getYear() <= maxYear)
                .collect(Collectors.toList());
    }

    private final AtomicLong idGenerator = new AtomicLong(100);

    @Override
    public Optional<Movie> findById(Long id) {
        return getAllMovies().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Movie save(Movie movie) {
        List<Movie> movies = getAllMovies();

        if (movie.getId() == null) {
            movie.setId(idGenerator.getAndIncrement());
            movies.add(movie);
        } else {
            movies.removeIf(m -> m.getId().equals(movie.getId()));
            movies.add(movie);
        }

        writeMovies(movies);
        return movie;
    }

    @Override
    public void deleteById(Long id) {  // <-- ДОБАВИТЬ
        List<Movie> movies = getAllMovies();
        movies.removeIf(m -> m.getId().equals(id));
        writeMovies(movies);
    }

    private void writeMovies(List<Movie> movies) {  // <-- ДОБАВИТЬ
        try {
            ObjectMapper mapper = newMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File(movieFilePath), movies);
        } catch (IOException e) {
            throw new RuntimeException("Error writing movie data to: " + movieFilePath, e);
        }
    }

    private ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setLocale(Locale.ENGLISH);
        return mapper;
    }
}