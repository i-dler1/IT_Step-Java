package org.example.hwSpring.controller;

import org.example.hwSpring.model.Movie;
import org.example.hwSpring.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieRestController {

    private final MovieService movieService;

    // GET все фильмы
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // GET фильм по ID
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // GET фильмы по жанру
    @GetMapping("/genre/{genre}")
    public List<Movie> getMoviesByGenre(@PathVariable String genre) {
        return movieService.filterByGenre(genre);
    }

    // GET фильмы по году с параметрами
    @GetMapping("/year")
    public List<Movie> getMoviesByYear(
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear) {
        if (minYear != null && maxYear != null) {
            return movieService.getByYear(minYear, maxYear);
        }
        return movieService.getAllMovies();
    }

    // GET фильмы по рейтингу
    @GetMapping("/rating")
    public List<Movie> getMoviesByRating(
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {
        if (minRating != null && maxRating != null) {
            return movieService.sortByRating(minRating, maxRating);
        }
        return movieService.getAllMovies();
    }

    // GET фильмы ужасов после 2000
    @GetMapping("/horror/after2000")
    public List<Movie> getHorrorMoviesAfter2000() {
        var horrorMovies = movieService.filterByGenre("Horror");
        return horrorMovies.stream()
                .filter(m -> m.getYear() > 2000)
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .toList();
    }

    // GET расширенный поиск
    @GetMapping("/search")
    public List<Movie> advancedSearch(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {

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

    // POST создать новый фильм
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    // PUT обновить фильм полностью
    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        movie.setId(id);
        return movieService.updateMovie(movie);
    }

    // PATCH частичное обновление фильма
    @PatchMapping("/{id}")
    public Movie patchMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Movie existingMovie = movieService.getMovieById(id);

        if (movie.getTitle() != null) {
            existingMovie.setTitle(movie.getTitle());
        }
        if (movie.getYear() != null) {
            existingMovie.setYear(movie.getYear());
        }
        if (movie.getGenre() != null) {
            existingMovie.setGenre(movie.getGenre());
        }
        if (movie.getRating() != null) {
            existingMovie.setRating(movie.getRating());
        }

        return movieService.updateMovie(existingMovie);
    }

    // DELETE удалить фильм
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}