package org.example.hwSpring.config;

import org.example.hwSpring.repository.MovieRepository;
import org.example.hwSpring.repository.MovieRepositoryJSON;
import org.example.hwSpring.service.MovieService;
import org.example.hwSpring.service.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Configuration(enforceUniqueMethods = false, proxyBeanMethods = true)
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "org.example.hwSpring",
        useDefaultFilters = false,
        includeFilters = {
                @Filter(type = FilterType.ANNOTATION, value = Component.class)
        })
public class AppConfig {

    @Bean
    public String movieFilePath(
            @Value("${movie.data.file}") String path) {
        return path;
    }

    @Bean
    public double ratingThreshold(
            @Value("${app.rating.threshold}") double threshold) {
        return threshold;
    }

    @Bean("allowedGenres")
    public List<String> allowedGenres() {
        return Arrays.asList(
                "Action", "Adventure", "Animation", "Biography", "Comedy",
                "Crime", "Documentary", "Drama", "Family", "Fantasy",
                "Film-Noir", "History", "Horror", "Music", "Musical",
                "Mystery", "Romance", "Sci-Fi", "Short", "Sport",
                "Thriller", "War", "Western"
        );
    }

    @Bean("movieRepository")
    public MovieRepository movieRepository(String movieFilePath) {
        MovieRepositoryJSON repository = new MovieRepositoryJSON();
        repository.setMovieFilePath(movieFilePath);
        return repository;
    }

    @Bean("movieService")
    public MovieService movieService(
            @Qualifier("movieRepository") MovieRepository movieRepository,
            @Qualifier("allowedGenres") List<String> allowedGenres,
            double ratingThreshold) {
        MovieServiceImpl service = new MovieServiceImpl(movieRepository, allowedGenres);
        service.setRatingThreshold(ratingThreshold);
        return service;
    }
}