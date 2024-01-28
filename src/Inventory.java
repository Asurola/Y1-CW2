import java.util.List;

public interface Inventory {

    void addGame(List<Item> gameList);
    void removeGame(List<Item> gameList);
    void displayInventory(List<Item> gameList);
}
