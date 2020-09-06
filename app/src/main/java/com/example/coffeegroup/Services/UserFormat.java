package com.example.coffeegroup.Services;

public class UserFormat
{
    private String name;
    private int sugar;
    private int brew_level;
    private int want;

    public UserFormat() {
    }

    public UserFormat(String name, int sugar, int brew_level, int want)
    {
        this.name = name;
        this.sugar = sugar;
        this.brew_level = brew_level;
        this.want = want;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getBrew_level() {
        return brew_level;
    }

    public void setBrew_level(int brew_level) {
        this.brew_level = brew_level;
    }

    public int getWant() {
        return want;
    }

    public void setWant(int want) {
        this.want = want;
    }
}
