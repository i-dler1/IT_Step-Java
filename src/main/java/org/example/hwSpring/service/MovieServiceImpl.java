package org.example.hwSpring.service;

import lombok.Getter;
import lombok.Setter;
import org.example.hwSpring.model.Movie;
import org.example.hwSpring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final List<String> allowedGenres;

    @Setter
    @Getter
    private double ratingThreshold;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, List<String> allowedGenres) {
        this.movieRepository = movieRepository;
        this.allowedGenres = allowedGenres;
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
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public List<String> getAllowedGenres() {
        return allowedGenres;
    }

    @Override
    public List<Movie> getByYear(int minYear, int maxYear) {
        return movieRepository.findByYear(minYear, maxYear);
    }


}
