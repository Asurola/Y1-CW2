import java.util.List;

public class VideoGame extends Item {

    //unique attributes
    private List<String> platforms;
    private int releaseYear;

    //constructors
    public VideoGame(String title, String genre, List<String> platforms, int releaseYear, int stock) {
        super(title, genre, stock);
        this.platforms = platforms;
        this.releaseYear = releaseYear;
    }

    //getters and setters
    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    //overridden toString
    @Override
    public String toString() {
        return String.format("%s | Release Year: %d | Platforms: %s",
                super.toString(), this.getReleaseYear(), this.getPlatforms());
    }

    @Override
    // Custom method to get rented game details
    public String getRentedGameDetails() {
        return String.format("%s | Release Year: %d | Platforms: %s",
                super.getRentedGameDetails(), this.getReleaseYear(), this.getPlatforms());
    }
}
