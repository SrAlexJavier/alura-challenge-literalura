package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexResponse {
    private Long count;
    private String next;
    private String previous;
    private List<DatosLibro> results;

    public Long getCount() {
        return count;
    }
    public List<DatosLibro> getResults() {
        return results;
    }
}
