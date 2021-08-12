package com.middleware.lab.model.db;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Specification {

    @Id
    private String idProduct;
    private String objetivos;
    private String tamanos;
    private String marca;
    private String origen;
    private String sabores;
    private String bannerDescripcion;
    private String objetivo;
    private String titulo;
    private String subtitulo;
    private String dicen;
    private String propiedades;
    private String empleo;
    private String recomendaciones;
    private String glosario;
    private String campoUno;
    private String campoDos;
    private String campoTres;
    private Boolean isCompleteInfo;   
    private Boolean isSynchronizedVtex;

    @Override
    public String toString() {
        return "Specification{" + "idProduct=" + idProduct + ", objetivos=" + objetivos + ", tamanos=" + tamanos + ", marca=" + marca + ", origen=" + origen + ", sabores=" + sabores + ", bannerDescripcion=" + bannerDescripcion + ", objetivo=" + objetivo + ", titulo=" + titulo + ", subtitulo=" + subtitulo + ", dicen=" + dicen + ", propiedades=" + propiedades + ", empleo=" + empleo + ", recomendaciones=" + recomendaciones + ", glosario=" + glosario + ", campoUno=" + campoUno + ", campoDos=" + campoDos + ", campoTres=" + campoTres + ", isCompleteInfo=" + isCompleteInfo + ", isSynchronizedVtex=" + isSynchronizedVtex + '}';
    }

}
