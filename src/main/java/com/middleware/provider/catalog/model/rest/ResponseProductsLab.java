package com.middleware.provider.catalog.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseProductsLab {

    private String codref;
    private String codsku;
    private String depart;
    private String cat;
    private String descr;
    private String skuname;
    private String productname;
    private String descrip;
    @JsonProperty("preu") 
    private float preu;
    @JsonProperty("peso")
    private float peso;
    @JsonProperty("largo")
    private float largo;
    @JsonProperty("ancho")
    private float ancho;
    @JsonProperty("alto")
    private float alto;
    private String tamano;
    private String marc;
    private String titulo;
    private String subtitulo;
    private String dicen;
    private String empleo;
    private String reco;
    private String propi;
    private String glosa;
    private String usr_001;
    private String usr_002;
    private String usr_003;
    private String usr_015;
    private String usr_016;
    private String usr_017;
    private String usr_018;
    private String umed;
    private String fecreg;
    private boolean verweb;

    public ResponseProductsLab(String codref, String codsku, String depart, String cat, String descr, String skuname, String productname, String descrip, float preu, float peso, float largo, float ancho, float alto, String tamano, String marc, String titulo, String subtitulo, String dicen, String empleo, String reco, String propi, String glosa, String usr_001, String usr_002, String usr_003, String usr_015, String usr_016, String usr_017, String usr_018, String umed, String fecreg, boolean verweb) {
        this.codref = codref;
        this.codsku = codsku;
        this.depart = depart;
        this.cat = cat;
        this.descr = descr;
        this.skuname = skuname;
        this.productname = productname;
        this.descrip = descrip;
        this.preu = preu;
        this.peso = peso;
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
        this.tamano = tamano;
        this.marc = marc;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.dicen = dicen;
        this.empleo = empleo;
        this.reco = reco;
        this.propi = propi;
        this.glosa = glosa;
        this.usr_001 = usr_001;
        this.usr_002 = usr_002;
        this.usr_003 = usr_003;
        this.usr_015 = usr_015;
        this.usr_016 = usr_016;
        this.usr_017 = usr_017;
        this.usr_018 = usr_018;
        this.umed = umed;
        this.fecreg = fecreg;
        this.verweb = verweb;
    }

    
    @Override
    public String toString() {
        return "ResponseProductsLab{" + "codref=" + codref + ", codsku=" + codsku + ", depart=" + depart + ", cat=" + cat + ", descr=" + descr + ", skuname=" + skuname + ", productname=" + productname + ", descrip=" + descrip + ", preu=" + preu + ", peso=" + peso + ", largo=" + largo + ", ancho=" + ancho + ", alto=" + alto + ", tamano=" + tamano + ", marc=" + marc + ", titulo=" + titulo + ", subtitulo=" + subtitulo + ", dicen=" + dicen + ", empleo=" + empleo + ", reco=" + reco + ", propi=" + propi + ", glosa=" + glosa + ", usr_001=" + usr_001 + ", usr_002=" + usr_002 + ", usr_003=" + usr_003 + ", usr_015=" + usr_015 + ", usr_016=" + usr_016 + ", usr_017=" + usr_017 + ", usr_018=" + usr_018 + ", umed=" + umed + ", fecreg=" + fecreg + ", verweb=" + verweb + '}';
    }

}
