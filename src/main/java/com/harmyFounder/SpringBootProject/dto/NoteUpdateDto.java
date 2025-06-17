package com.harmyFounder.SpringBootProject.dto;

public class NoteUpdateDto {
    private String tittle;
    private String text;


    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "NoteUpdateDto{" +
                "tittle='" + tittle + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}