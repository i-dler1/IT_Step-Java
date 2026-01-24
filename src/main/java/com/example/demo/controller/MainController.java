package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;

import java.util.List;
import java.util.Scanner;

public class MainController {

    boolean running = true;

    private final MovieService movieService;
    private Scanner scanner;

    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    public void start() {
        while (running) {
            printMenu();
            int choice = readIntInput();

            switch (choice) {
                case 1 -> showAllMovies();
                case 2 -> searchByGenre();
                case 3 -> searchByYear();
                case 4 -> searchByRating();
                case 5 -> advancedSearch();
                case 6 -> running = false;
                default -> System.out.println("Incorrect selection. Try again.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\nApplication menu:");
        System.out.println("1. Show all movies.");
        System.out.println("2. Search movies by genre.");
        System.out.println("3. Search movies by year.");
        System.out.println("4. Search movies by rating.");
        System.out.println("5. Advanced movie search.");
        System.out.println("6. Exit the application.");
        System.out.print(">> ");
    }

    private void showAllMovies() {
        System.out.println("\nAll movies: ");
        List<Movie> movies = movieService.getAllMovie();
        displayMovies(movies);
    }

    private void searchByGenre() {
        System.out.println("\nMovies by genre:");
        List<String> allowedGenres = movieService.getAllowedGenres();

        if (allowedGenres != null && !allowedGenres.isEmpty()) {
            System.out.println("Allowed genres: " + String.join("\n", allowedGenres));
        }

        System.out.print(">> ");
        String genre = scanner.nextLine().trim();

        if (genre.isEmpty()) {
            System.out.println("The genre cannot be empty! ");
            return;
        }

        System.out.println("\nFilms in genre '" + genre + "':");
        List<Movie> movies = movieService.filterByGenre(genre);
        displayMovies(movies);
    }

    private void searchByYear() {
        System.out.println("\nMovies by year: ");
        System.out.println("Enter start of production: ");
        System.out.print(">> ");
        int minYear = readIntInput();
        System.out.println("Enter end of production: ");
        System.out.print(">> ");
        int maxYear = readIntInput();

        if (minYear > maxYear) {
            System.out.println("The start of production cannot be greater than the end!");
            return;
        }

        System.out.println("\nFilms from " + minYear + " to " + maxYear + ":");
        List<Movie> movies = movieService.getMoviesByYear(minYear, maxYear);
        displayMovies(movies);
    }

    private void searchByRating() {
        System.out.println("\nMovies by rating: ");
        double minRating, maxRating;

        do {
            System.out.println("Enter the minimum rating (0-10): ");
            System.out.print(">> ");
            minRating = readDoubleInput();
            if (minRating < 0 || minRating > 10) {
                System.out.println("Rating must be between 0 and 10!");
            }
        } while (minRating < 0 || minRating > 10);

        do {
            System.out.println("Enter the maximum rating (0-10): ");
            System.out.print(">> ");
            maxRating = readDoubleInput();
            if (maxRating < 0 || maxRating > 10) {
                System.out.println("Rating must be between 0 and 10!");
            }
        } while (maxRating < 0 || maxRating > 10);

        if (minRating > maxRating) {
            System.out.println("The minimum rating cannot be greater than the maximum!");
            return;
        }

        System.out.println("\nMovies rated " + minRating + " to " + maxRating + ": ");
        List<Movie> movies = movieService.sortByRating(minRating, maxRating);
        displayMovies(movies);
    }

    private void advancedSearch() {
        System.out.println("Advanced movie search:");
        System.out.println("(Press enter if you want to skip the input criteria.)");
//TODO
    }

    private void displayMovies(List<Movie> movies) {
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
            System.out.println("-".repeat(80));
            System.out.println("Total movies found: " + movies.size());
        }
    }

    private int readIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter an integer.");
            }
        }
    }

    private double readDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number.");
            }
        }
    }

    private void init() {
        this.scanner = new Scanner(System.in);
    }
}
