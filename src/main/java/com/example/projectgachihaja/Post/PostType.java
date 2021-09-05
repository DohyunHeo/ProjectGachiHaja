package com.example.projectgachihaja.Post;

public enum PostType {
    NOTICE("공지","manager"),
    CHAT("잡담","member"),
    QUESTION("질문","member");

    private String krName;

    private String type;

    PostType(String krName,String type) {
        this.krName = krName;
        this.type = type;
    }

    public String getKrName(){ return krName;}
    public String getType(){ return type;}
}
