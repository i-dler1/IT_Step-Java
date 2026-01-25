package org.example.hwSpring.repository;

import org.example.hwSpring.model.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> getAllMovies();

    List<Movie> findByGenre(String genre);

    List<Movie> findByRating(double minRating, double maxRating);

    List<Movie> findByYear(int minYear, int maxYear);
}
