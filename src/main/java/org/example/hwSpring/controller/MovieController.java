package org.example.hwSpring.controller;

import lombok.RequiredArgsConstructor;
import org.example.hwSpring.service.MovieService;

@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // Метод для вывода фильмов ужасов после 2000 года
    public void showHorrorMoviesAfter2000() {
        System.out.println("\nHorror movies after 2000.");

        double ratingThreshold = movieService.getRatingThreshold();

        var horrorMovies = movieService.filterByGenre("Horror");
        var filtered = horrorMovies.stream()
                .filter(m -> m.getYear() > 2000)
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No horror movies found after 2000.");
        } else {
            System.out.printf("%-4s %-50s %-6s %-6s\n",
                    "ID", "Title", "Year", "Rating");
            System.out.println("-".repeat(80));
            filtered.forEach(m -> System.out.printf("%-4d %-50s %-6d %-6.1f\n",
                    m.getId(), m.getTitle(), m.getYear(), m.getRating()));
            System.out.println("-".repeat(80));

            long total = filtered.size();
            long aboveThreshold = filtered.stream()
                    .filter(m -> m.getRating() >= ratingThreshold)
                    .count();
            double percentage = total > 0 ? (aboveThreshold * 100.0 / total) : 0;

            System.out.println("Total horror movies after 2000: " + filtered.size());
            System.out.println("Rating threshold: " + ratingThreshold);
            System.out.println("Movies above threshold (≥ " + ratingThreshold + "): " + aboveThreshold);
            System.out.printf("Percentage above threshold: %.1f%%\n", percentage);

            // Средний рейтинг
            double averageRating = filtered.stream()
                    .mapToDouble(m -> m.getRating())
                    .average()
                    .orElse(0.0);
            System.out.printf("Average rating: %.2f\n", averageRating);
        }
    }
}
