public class BoardGame extends Item {

    // unique attributes
    private int numPlayers;
    private String publisher;

    //constructor
    public BoardGame(String title, String genre, int numPlayers, String publisher, int stock) {
        super(title, genre, stock);
        this.numPlayers = numPlayers;
        this.publisher = publisher;
    }

    //getters and setters
    public int getNumPlayers() {
        return numPlayers;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    //overridden toString
    @Override
    public String toString() {
        return String.format("%s | Num Players: %d | Publisher: %s",
                super.toString(), this.getNumPlayers(), this.getPublisher());
    }

    @Override
    // Custom method to get rented game details
    public String getRentedGameDetails() {
        return String.format("%s | Num Players: %d | Publisher: %s",
                super.getRentedGameDetails(), this.getNumPlayers(), this.getPublisher());
    }
}
