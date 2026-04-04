package org.example.hwSpring.repository;

import org.example.hwSpring.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    List<Movie> getAllMovies();

    List<Movie> findByGenre(String genre);

    List<Movie> findByRating(double minRating, double maxRating);

    List<Movie> findByYear(int minYear, int maxYear);

    Optional<Movie> findById(Long id);
    Movie save(Movie movie);
    void deleteById(Long id);
}
