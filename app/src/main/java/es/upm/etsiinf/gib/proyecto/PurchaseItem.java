package es.upm.etsiinf.gib.proyecto;

public class PurchaseItem {

    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private int quantity;

    public PurchaseItem(String title, double price, String description,
                        String category, String image, int quantity) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.quantity = quantity;
    }

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public int getQuantity() { return quantity; }
}
