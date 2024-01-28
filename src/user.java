import java.util.Random;

public abstract class user {
    // attributes
    private String name;
    private String ID;

    //constructor
    public user(String name) {
        this.name = name;
        this.ID = generateRandomID();
    }

    //getters
    public String getName() {
        return name;
    }

    //abstract methods
    public abstract boolean login();

    public abstract void register();

    public abstract void forgetID();

    // generate randomID method
    protected static String generateRandomID() {
        // Generate a random alphanumeric ID for the user
        Random rand = new Random();
        int num = rand.nextInt(10000);
        return String.format("%04d", num);
    }
}