package com.example.demo.repository;

import com.example.demo.model.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> getAllMovies();

    List<Movie> findByGenre(String genre);

    List<Movie> findByRating(double minRating, double maxRating);

    List<Movie> findMovieByYear(int minYear, int maxYear);
}
