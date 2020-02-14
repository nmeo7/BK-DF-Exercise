package com.example.filmfan;

public class Actor {

    private String name;
    private String character;
    private String picture;

    public Actor(String name, String character, String picture ) {
        this.name=name;
        this.character=character;
        this.picture=picture;

    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getPicture() {
        return picture;
    }
}