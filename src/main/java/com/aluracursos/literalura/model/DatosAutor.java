package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaNacimiento,
        @JsonAlias("death_year") Integer fechaFallecimiento
) {

    public String getName() {
            return nombre != null ? nombre : "";
        }

        public Integer getBirthYear() {
            return fechaNacimiento;
        }

        public Integer getDeathYear() {
            return fechaFallecimiento;
        }
}
