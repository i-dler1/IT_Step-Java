package org.example.hwSpring.controller;

import lombok.Getter;
import org.example.hwSpring.model.Movie;
import org.example.hwSpring.service.MovieService;

import java.util.List;
import java.util.Scanner;

public class MainController {

    private boolean running = true;

    @Getter
    private final MovieService movieService;

    private Scanner scanner;

    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    private void init() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        if (scanner == null) {
            throw new IllegalStateException("Controller not properly initialized. Call init() first.");
        }

        while (running) {
            printMenu();
            int choice = readIntInput();

            switch (choice) {
                case 1 -> showAllMovies();
                case 2 -> searchByGenre();
                case 3 -> searchByYear();
                case 4 -> searchByRating();
                case 5 -> new AdvancedSearchController(this).execute();
                case 6 -> {
                    running = false;
                    System.out.println("Exiting application...");
                }
                default -> System.out.println("Incorrect selection. Try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nApplication menu: ");
        System.out.println("1. Show all movies");
        System.out.println("2. Search by genre");
        System.out.println("3. Search by year");
        System.out.println("4. Search by rating");
        System.out.println("5. Advanced search");
        System.out.println("6. Exit");
        System.out.print(">> ");
    }

    public void displayMovies(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            System.out.println("No movies found.");
        } else {
            System.out.printf("\n%-4s %-50s %-6s %-10s %-6s\n",
                    "ID", "Title", "Year", "Genre", "Rating");
            System.out.println("-".repeat(80));

            for (Movie movie : movies) {
                System.out.printf("%-4d %-50s %-6d %-10s %-6.1f\n",
                        movie.getId(),
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getGenre(),
                        movie.getRating());
            }
            System.out.println("\nTotal movies found: " + movies.size());
        }
    }

    private void showAllMovies() {
        System.out.println("\nAll movies.");
        displayMovies(movieService.getAllMovies());
    }

    private void searchByGenre() {
        System.out.println("\nSearch by genre.");
        String genre = askForGenre(false);
        if (genre != null) {
            displayMovies(movieService.filterByGenre(genre));
        }
    }

    private void searchByYear() {
        System.out.println("\nSearch by year.");
        YearRange range = askForYearRange(false);
        if (range.min != null && range.max != null) {
            displayMovies(movieService.getByYear(range.min, range.max));
        }
    }

    private void searchByRating() {
        System.out.println("\nSearch by rating.");
        RatingRange range = askForRatingRange(false);
        if (range.min != null && range.max != null) {
            displayMovies(movieService.sortByRating(range.min, range.max));
        }
    }

    // Метод для проверки введенных целых чисел
    private int readIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.print("Input cannot be empty. Please enter a number: ");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number format. Please enter an integer: ");
            }
        }
    }

    // Метод для запроса жанра
    public String askForGenre(boolean canSkip) {
        List<String> allowedGenres = movieService.getAllowedGenres();

        if (allowedGenres != null && !allowedGenres.isEmpty()) {
            System.out.println("Allowed genres: \n" + String.join("\n", allowedGenres));
        }

        System.out.print("\nEnter genre" + (canSkip ? " (press Enter to skip): " : ": "));
        String input = scanner.nextLine().trim();

        if (!canSkip && input.isEmpty()) {
            System.out.println("Genre cannot be empty!");
            return askForGenre(canSkip);
        }

        return input.isEmpty() ? null : input;
    }

    // Метод для запроса года
    public YearRange askForYearRange(boolean canSkip) {
        YearRange range = new YearRange();

        System.out.print("Enter minimum year" + (canSkip ? " (press Enter to skip): " : ": "));
        String minInput = scanner.nextLine().trim();
        if (!minInput.isEmpty()) {
            try {
                range.min = Integer.parseInt(minInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format.");
            }
        }

        System.out.print("Enter maximum year" + (canSkip ? " (press Enter to skip): " : ": "));
        String maxInput = scanner.nextLine().trim();
        if (!maxInput.isEmpty()) {
            try {
                range.max = Integer.parseInt(maxInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format.");
            }
        }

        return range;
    }

    // Метод для запроса рейтинга
    public RatingRange askForRatingRange(boolean canSkip) {
        RatingRange range = new RatingRange();

        System.out.print("Enter minimum rating 0-10" + (canSkip ? " (press Enter to skip): " : ": "));
        String minInput = scanner.nextLine().trim();
        if (!minInput.isEmpty()) {
            try {
                double value = Double.parseDouble(minInput);
                if (value >= 0 && value <= 10) {
                    range.min = value;
                } else {
                    System.out.println("Rating must be 0-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating format.");
            }
        }

        System.out.print("Enter maximum rating 0-10" + (canSkip ? " (press Enter to skip): " : ": "));
        String maxInput = scanner.nextLine().trim();
        if (!maxInput.isEmpty()) {
            try {
                double value = Double.parseDouble(maxInput);
                if (value >= 0 && value <= 10) {
                    range.max = value;
                } else {
                    System.out.println("Rating must be 0-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating format.");
            }
        }
        return range;
    }

    public static class YearRange {
        public Integer min;
        public Integer max;

        @Override
        public String toString() {
            if (min != null && max != null) {
                return min + " - " + max;
            } else if (min != null) {
                return "From " + min;
            } else if (max != null) {
                return "Up to " + max;
            }
            return "Any";
        }
    }

    public static class RatingRange {
        public Double min;
        public Double max;

        @Override
        public String toString() {
            if (min != null && max != null) {
                return String.format("%.1f - %.1f", min, max);
            } else if (min != null) {
                return String.format("From %.1f", min);
            } else if (max != null) {
                return String.format("Up to %.1f", max);
            }
            return "Any";
        }
    }
}