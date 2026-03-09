package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    @Column(length = 1500)
    private String resumen;
    private String idioma;
    private Integer numeroDescargas;
    @ManyToOne()
    private Autor autor;


    public Libro(DatosLibro primerLibro, Autor autor) {
        this.titulo = primerLibro.titulo();
        this.resumen = primerLibro.getResumen();
        this.idioma = primerLibro.getIdioma();
        this.numeroDescargas = primerLibro.numeroDescargas();
        this.autor = autor;
    }
    public Libro(){}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "------------LIBRO-----------" + '\n' +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor.getNombre() + '\n' +
                "Idiom: " + idioma + '\n' +
                "Numero de descargas: " + numeroDescargas + '\n' +
                "---------------------------"+'\n' ;
    }
}
