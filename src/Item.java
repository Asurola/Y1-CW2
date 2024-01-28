import java.io.Serializable;

public abstract class Item implements Serializable {
    // attributes
    private String title;
    private int ID;
    private String genre;
    private boolean available;

    private int stock;

    // item constructor
    public Item(String title, String genre, int stock) {
        this.title = title;
        // ID does not need to be defined in the constructor and will be displayed in the display function
        this.ID = generateRandomID();
        this.genre = genre;
        this.stock = stock;
        this.available = true;
    }

    // getters and setters
    public String getTitle() {
        return title;
    }

    public int getID() {
        return ID;
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // method to generate a random ID
    public static int generateRandomID() {
        return (int) (Math.random() * 1000);
    }

    // method for output print formatting for display function
    public String toString() {
        return String.format("ID: %-4s | Title: %-20s | Genre: %-15s | Stock: %-1s | Available: %-4s",
                this.getID(), this.getTitle(), this.getGenre(), this.getStock(), this.isAvailable());
    }

    // Custom method to get rented game details
    public String getRentedGameDetails() {
        return String.format("%-5s | %-16s | %-15s", ID, title, genre);
    }
}
