package com.example.projectgachihaja.Together;

public enum TogetherType {
    CLUB("동호회"), STUDY("스터디");

    private String krName;
    TogetherType(String krName) {
        this.krName =krName;
    }

    public String getKrName() {
        return krName;
    }
}
