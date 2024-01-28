
public class Merchandise extends Item {
    private float price;

    public Merchandise(String title, String genre, float price, int stock) {
        super(title, genre, stock);
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}