package org.example.hwSpring.controller;

import org.example.hwSpring.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdvancedSearchController {
    private final MainController mainController;

    public AdvancedSearchController(MainController mainController) {
        this.mainController = mainController;
    }

    public void execute() {
        System.out.println("\n" + "-".repeat(25));
        System.out.println("Advanced movie search.");
        System.out.println("-".repeat(25));
        System.out.println("Specify criteria (press Enter to skip):");

        // Метод из MainController для ввода данных
        String genre = mainController.askForGenre(true); // true - можно пропустить через Enter
        MainController.YearRange yearRange = mainController.askForYearRange(true);
        MainController.RatingRange ratingRange = mainController.askForRatingRange(true);

        // Проверка указан ли хоть один критерий
        if (genre == null && yearRange.min == null && yearRange.max == null
                && ratingRange.min == null && ratingRange.max == null) {
            System.out.println("\nNo criteria specified. Showing all movies.");
            mainController.displayMovies(mainController.getMovieService().getAllMovies());
            return;
        }

        // Показываем выбранные критерии
        showCriteria(genre, yearRange, ratingRange);

        List<Movie> results = performSearch(genre, yearRange, ratingRange);

        System.out.println("\n" + "-".repeat(10));
        System.out.print("Result.");
        System.out.println("\n" + "-".repeat(80));
        mainController.displayMovies(results);
    }

    private void showCriteria(String genre, MainController.YearRange yearRange,
                              MainController.RatingRange ratingRange) {
        System.out.println("\nSelected criteria:");
        System.out.println(" -- Genre: " + (genre != null ? genre : "Any"));
        System.out.println(" -- Year: " + yearRange);
        System.out.println(" -- Rating: " + ratingRange);
    }

    private List<Movie> performSearch(String genre, MainController.YearRange yearRange,
                                      MainController.RatingRange ratingRange) {
        List<Movie> movies = mainController.getMovieService().getAllMovies();

        if (genre != null) {
            movies = movies.stream()
                    .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (yearRange.min != null) {
            movies = movies.stream()
                    .filter(m -> m.getYear() >= yearRange.min)
                    .collect(Collectors.toList());
        }

        if (yearRange.max != null) {
            movies = movies.stream()
                    .filter(m -> m.getYear() <= yearRange.max)
                    .collect(Collectors.toList());
        }

        if (ratingRange.min != null) {
            movies = movies.stream()
                    .filter(m -> m.getRating() >= ratingRange.min)
                    .collect(Collectors.toList());
        }

        if (ratingRange.max != null) {
            movies = movies.stream()
                    .filter(m -> m.getRating() <= ratingRange.max)
                    .collect(Collectors.toList());
        }

        return movies.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .collect(Collectors.toList());
    }
}