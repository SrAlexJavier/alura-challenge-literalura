package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    Optional<Libro> findByTituloAndAutor_Nombre(String titulo, String nombreAutor);

    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findAll();

    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> findDistinctIdiomas();
    List<Libro> findByIdiomaIgnoreCase(String idioma);
}
