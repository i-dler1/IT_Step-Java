package com.example.demo.service;

import com.example.demo.model.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> filterByGenre(String genre);

    List<Movie> sortByRating(double minRating, double maxRating);

    List<Movie> getAllMovie();

    List<String> getAllowedGenres();

    List<Movie> getMoviesByYear(int minYear, int maxYear);
}
