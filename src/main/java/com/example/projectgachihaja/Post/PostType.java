package com.example.projectgachihaja.Post;

public enum PostType {
    CHAT("잡담"),
    NOTICE("공지");

    private String krName;

    PostType(String krName) {
        this.krName = krName;
    }

    public String getKrName(){ return krName;}
}
