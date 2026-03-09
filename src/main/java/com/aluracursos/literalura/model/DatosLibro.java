package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("summaries") List<String> resumen,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Integer numeroDescargas,
        @JsonAlias("authors") List<DatosAutor> autores) {

    public String getResumen() {
        if (resumen != null && !resumen.isEmpty()) {
            return resumen.get(0);
        }
        return "";
    }
    public String getIdioma() {
        if (idioma != null && !idioma.isEmpty()) {
            return idioma.get(0);
        }
        return "";
    }
    public DatosAutor getAutor() {
        if (autores != null && !autores.isEmpty()) {
            return autores.get(0);
        }
        return new DatosAutor("desconocido",null,null);
    }
}
