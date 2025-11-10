package org.alessipg.server.app.service;

import org.alessipg.shared.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        // Criando MovieService com null repository já que não vamos usar operações de repositório
        movieService = new MovieService(null);
    }

    @Test
    void testMapGenres_withValidGenres() {
        // Arrange
        List<String> genreStrings = Arrays.asList("Ação", "Comédia", "Drama");

        // Act
        List<Genre> result = movieService.mapGenres(genreStrings);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Genre.ACAO, result.get(0));
        assertEquals(Genre.COMEDIA, result.get(1));
        assertEquals(Genre.DRAMA, result.get(2));
    }

    @Test
    void testMapGenres_withSingleGenre() {
        // Arrange
        List<String> genreStrings = Arrays.asList("Terror");

        // Act
        List<Genre> result = movieService.mapGenres(genreStrings);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Genre.TERROR, result.get(0));
    }

    @Test
    void testMapGenres_withAllGenres() {
        // Arrange
        List<String> genreStrings = Arrays.asList(
                "Ação", "Animação", "Aventura", "Comédia", "Documentário",
                "Drama", "Fantasia", "Ficção Científica", "Musical", "Romance", "Terror"
        );

        // Act
        List<Genre> result = movieService.mapGenres(genreStrings);

        // Assert
        assertNotNull(result);
        assertEquals(11, result.size());
        assertEquals(Genre.ACAO, result.get(0));
        assertEquals(Genre.ANIMACAO, result.get(1));
        assertEquals(Genre.AVENTURA, result.get(2));
        assertEquals(Genre.COMEDIA, result.get(3));
        assertEquals(Genre.DOCUMENTARIO, result.get(4));
        assertEquals(Genre.DRAMA, result.get(5));
        assertEquals(Genre.FANTASIA, result.get(6));
        assertEquals(Genre.FICCAO_CIENTIFICA, result.get(7));
        assertEquals(Genre.MUSICAL, result.get(8));
        assertEquals(Genre.ROMANCE, result.get(9));
        assertEquals(Genre.TERROR, result.get(10));
    }

    @Test
    void testMapGenres_withEmptyList() {
        // Arrange
        List<String> genreStrings = Arrays.asList();

        // Act
        List<Genre> result = movieService.mapGenres(genreStrings);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMapGenres_withInvalidGenre() {
        // Arrange
        List<String> genreStrings = Arrays.asList("Gênero Inválido");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieService.mapGenres(genreStrings);
        });
    }

    @Test
    void testMapGenres_withMixedValidAndInvalidGenres() {
        // Arrange
        List<String> genreStrings = Arrays.asList("Ação", "Gênero Inválido", "Drama");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieService.mapGenres(genreStrings);
        });
    }
}

