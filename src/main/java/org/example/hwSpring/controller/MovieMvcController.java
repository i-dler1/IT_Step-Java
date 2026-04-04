package org.example.hwSpring.controller;

import org.example.hwSpring.model.Movie;
import org.example.hwSpring.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieMvcController {

    private final MovieService movieService;

    // Главная страница со всеми фильмами
    @GetMapping
    public String getAllMovies(Model model) {
        System.out.println("=== getAllMovies called ===");
        System.out.println("Movies count: " + movieService.getAllMovies().size());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());
        return "movies/list";
    }

    // Страница с деталями фильма
    @GetMapping("/{id}")
    public String getMovieById(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        return "movies/details";
    }

    // Страница поиска по жанру
    @GetMapping("/genre/{genre}")
    public String getMoviesByGenre(@PathVariable String genre, Model model) {
        model.addAttribute("movies", movieService.filterByGenre(genre));
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());
        return "movies/list";
    }

    // Страница поиска по году
    @GetMapping("/year")
    public String getMoviesByYear(
            @RequestParam Integer minYear,
            @RequestParam Integer maxYear,
            Model model) {
        model.addAttribute("movies", movieService.getByYear(minYear, maxYear));
        model.addAttribute("minYear", minYear);
        model.addAttribute("maxYear", maxYear);
        return "movies/list";
    }

    // Страница поиска по рейтингу
    @GetMapping("/rating")
    public String getMoviesByRating(
            @RequestParam Double minRating,
            @RequestParam Double maxRating,
            Model model) {
        model.addAttribute("movies", movieService.sortByRating(minRating, maxRating));
        model.addAttribute("minRating", minRating);
        model.addAttribute("maxRating", maxRating);
        return "movies/list";
    }

    // Страница расширенного поиска
    @GetMapping("/search")
    public String advancedSearchForm(Model model) {
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());
        return "movies/search";
    }

    // Результаты расширенного поиска
    @PostMapping("/search")
    public String advancedSearch(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            Model model) {

        List<Movie> results = performSearch(genre, minYear, maxYear, minRating, maxRating);
        model.addAttribute("movies", results);
        model.addAttribute("genre", genre);
        model.addAttribute("minYear", minYear);
        model.addAttribute("maxYear", maxYear);
        model.addAttribute("minRating", minRating);
        model.addAttribute("maxRating", maxRating);
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());

        return "movies/search-results";
    }

    // Форма создания нового фильма
    @GetMapping("/new")
    public String createMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());
        return "movies/form";
    }

    // Создание нового фильма
    @PostMapping
    public String createMovie(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        Movie savedMovie = movieService.saveMovie(movie);
        redirectAttributes.addFlashAttribute("message", "Фильм успешно создан!");
        return "redirect:/movies/" + savedMovie.getId();
    }

    // Форма редактирования фильма
    @GetMapping("/{id}/edit")
    public String editMovieForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        model.addAttribute("allowedGenres", movieService.getAllowedGenres());
        return "movies/form";
    }

    // Обновление фильма
    @PutMapping("/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute Movie movie,
                              RedirectAttributes redirectAttributes) {
        movie.setId(id);
        movieService.updateMovie(movie);
        redirectAttributes.addFlashAttribute("message", "Фильм успешно обновлен!");
        return "redirect:/movies/" + id;
    }

    // Удаление фильма
    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        movieService.deleteMovie(id);
        redirectAttributes.addFlashAttribute("message", "Фильм успешно удален!");
        return "redirect:/movies";
    }

    // Страница с фильмами ужасов после 2000
    @GetMapping("/horror/after2000")
    public String getHorrorMoviesAfter2000(Model model) {
        var horrorMovies = movieService.filterByGenre("Horror");
        var filtered = horrorMovies.stream()
                .filter(m -> m.getYear() > 2000)
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .toList();

        double ratingThreshold = movieService.getRatingThreshold();
        long aboveThreshold = filtered.stream()
                .filter(m -> m.getRating() >= ratingThreshold)
                .count();
        double averageRating = filtered.stream()
                .mapToDouble(Movie::getRating)
                .average()
                .orElse(0.0);

        model.addAttribute("movies", filtered);
        model.addAttribute("totalMovies", filtered.size());
        model.addAttribute("ratingThreshold", ratingThreshold);
        model.addAttribute("aboveThreshold", aboveThreshold);
        model.addAttribute("averageRating", averageRating);

        return "movies/horror-after2000";
    }

    // Вспомогательный метод для поиска
    private List<Movie> performSearch(String genre, Integer minYear, Integer maxYear,
                                      Double minRating, Double maxRating) {
        List<Movie> movies = movieService.getAllMovies();

        if (genre != null && !genre.isEmpty()) {
            movies = movies.stream()
                    .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                    .toList();
        }

        if (minYear != null) {
            movies = movies.stream()
                    .filter(m -> m.getYear() >= minYear)
                    .toList();
        }

        if (maxYear != null) {
            movies = movies.stream()
                    .filter(m -> m.getYear() <= maxYear)
                    .toList();
        }

        if (minRating != null) {
            movies = movies.stream()
                    .filter(m -> m.getRating() >= minRating)
                    .toList();
        }

        if (maxRating != null) {
            movies = movies.stream()
                    .filter(m -> m.getRating() <= maxRating)
                    .toList();
        }

        return movies.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .toList();
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/movies";
    }

    @GetMapping("/test-template")
    public String testTemplate() {
        System.out.println("=== Test template called ===");
        return "movies/list";
    }
}