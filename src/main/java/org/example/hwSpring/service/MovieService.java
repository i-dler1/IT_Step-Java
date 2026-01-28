package org.example.hwSpring.service;

import org.example.hwSpring.model.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> filterByGenre(String genre);

    List<Movie> sortByRating(double minRating, double maxRating);

    List<Movie> getAllMovies();

    List<String> getAllowedGenres();

    List<Movie> getByYear(int minYear, int maxYear);

    double getRatingThreshold();
}
