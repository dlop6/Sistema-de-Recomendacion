package com.ValidacionDatos;


import java.util.ArrayList;

public class User {

    public String name;
    public ArrayList<String>  movies = new ArrayList<String>();
    public ArrayList<String>  favorites = new ArrayList<String>();
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getMovies() {
        return movies;
    }
    public void setMovies(ArrayList<String> movies) {
        this.movies = movies;
    }
    public ArrayList<String> getFavorites() {
        return favorites;
    }
    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }
    
}
