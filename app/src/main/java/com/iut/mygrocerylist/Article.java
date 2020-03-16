package com.iut.mygrocerylist;

public class Article {

    private String nom, remarques, id, quantite, priorite, recupere;

    public Article(String id) {
        this.id = id;
    }

    public String getRecupere() {
        return recupere;
    }

    public void setRecupere(String recupere) {
        this.recupere = recupere;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public String getId() {
        return id;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getPriorite() {
        if(priorite == null) priorite = "2";
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }
}
