package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final List<String> allowedGenres;


    public MovieServiceImpl(MovieRepository movieRepository, List<String> allowedGenres) {
        this.movieRepository = movieRepository;
        this.allowedGenres = allowedGenres;
    }

    public List<Movie> searchMovies(String genre, Integer minYear, Integer maxYear,
                                    Double minRating, Double maxRating) {
        return movieRepository.getAllMovies().stream()
                .filter(movie -> genre == null || movie.getGenre().equalsIgnoreCase(genre))
                .filter(movie -> minYear == null || movie.getYear() >= minYear)
                .filter(movie -> maxYear == null || movie.getYear() <= maxYear)
                .filter(movie -> minRating == null || movie.getRating() >= minRating)
                .filter(movie -> maxRating == null || movie.getRating() <= maxRating)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> filterByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    @Override
    public List<Movie> sortByRating(double minRating, double maxRating) {
        return movieRepository.findByRating(minRating, maxRating)
                .stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getAllMovie() {
        return movieRepository.getAllMovies();
    }

    @Override
    public List<String> getAllowedGenres() {
        return allowedGenres;
    }

    @Override
    public List<Movie> getMoviesByYear(int minYear, int maxYear) {
        return movieRepository.findMovieByYear(minYear, maxYear);
    }


//    public List<Movie> getMoviesByGenre(String genre) {
//        return searchMovies(genre, null, null, null, null);
//    }
//
//    public List<String> getAllGenres() {
//        return movieRepository.getAllMovies().stream()
//                .map(Movie::getGenre)
//                .distinct()
//                .sorted()
//                .collect(Collectors.toList());
//    }
//
//    public int getMinYear() {
//        return movieRepository.getAllMovies().stream()
//                .mapToInt(Movie::getYear)
//                .min()
//                .orElse(1900);
//    }
//
//    public int getMaxYear() {
//        return movieRepository.getAllMovies().stream()
//                .mapToInt(Movie::getYear)
//                .max()
//                .orElse(2024);
//    }
}
