package es.upm.etsiinf.gib.proyecto;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    public static class Rating implements Serializable {
        private double rate;
        private int count;

        public double getRate() { return rate; }
        public int getCount() { return count; }
    }

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public Rating getRating() { return rating; }
}

