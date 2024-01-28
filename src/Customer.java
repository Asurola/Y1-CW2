import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Customer extends user implements Serializable {
    //attributes
    private String customerID;

    //specifies the file to be in root
    private static final String DATA_FILE_PATH = "customer_data.ser";

    //attribute list for the customers rentedGamers
    private List<Item> rentedGames;

    // attribute to store the merchandise items to a cart for consecutive purchases
    private List<Merchandise> shoppingCart;

    // attribute to store the original indexes of merchandise before adding to the cart to reset if purchase cancel
    private final List<Integer> originalMerchandiseIndexes = new ArrayList<>();

    // attribute to store removed merchandise temporarily to allow restoration of stock depleted items
    private List<Merchandise> removedMerchandise = new ArrayList<>();

    // attribute to store the original stock of merchandise before adding to the cart becuase stock resets for some reason
    private List<Integer> originalMerchandiseStocks = new ArrayList<>();


    //constructor
    public Customer(String name) {
        super(name);
        this.customerID = generateCustomerID();
        this.rentedGames = loadCustomerData().getOrDefault(name, new ArrayList<>());
    }

    // getter for rentedGame
    public List<Item> getRentedGames() {
        return rentedGames;
    }

    // overridden login function and other inherited functions
    @Override
    public boolean login() {
        return false;
    }

    @Override
    public void register() {
    }

    //method to remind user of ID
    @Override
    public void forgetID() {
    }


    // method to generate random customer ID
    private String generateCustomerID() {
        // Generate a random alphanumeric customer ID
        Random rand = new Random();
        int num = rand.nextInt(10000);
        return String.format("C%04d", num);
    }

    // load customer data from file
    // @SuppressWarnings ("unchecked") is necessary to suppress compiler warnings
    @SuppressWarnings("unchecked")
    private static HashMap<String, List<Item>> loadCustomerData() {
        // creates the ois (objectInputStream) wrapped around the fis (FileInputStream)
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE_PATH))) {
            //returns the hashmap with string as customer username and Item as the game
            return (HashMap<String, List<Item>>) ois.readObject();
        } catch (FileNotFoundException e) {
            //if not founds returns a new and empty hashmap
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            //prints stack trace during error and does the same thing
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // save customer data to file
    private static void saveCustomerData(HashMap<String, List<Item>> customerData) {
        // does the same thing as mentioned above
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_PATH))) {
            //writes the provided hashmap to the output through serialization
            oos.writeObject(customerData);
        } catch (IOException e) {
            // in case of error prints stack trace which is used for error handling and analysis
            e.printStackTrace();
        }
    }

    // method to save rented games to file
    private void saveRentedGames() {
        HashMap<String, List<Item>> customerData = loadCustomerData();
        // adds the rented games contents to the hashmap before serialization
        customerData.put(getName(), rentedGames);
        saveCustomerData(customerData);
    }

    //method to make sure the data in the file is reset when the program closes
    public static void deleteDataFile() {
        File file = new File(DATA_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    //customer menu
    public void customerMenu(List<Item> gameList, List<Merchandise> merchList) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Rent a Game");
            System.out.println("2. Return a Game");
            System.out.println("3. Browse Available Games");
            System.out.println("4. View Rented Games");
            System.out.println("5. Search for a Game");
            System.out.println("6. Buy Merchandise");
            System.out.println("7. Log Out");
            System.out.print("Enter your choice: ");

            //try-catch error handling
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume the newline after int

                switch (choice) {
                    case 1:
                        rentGame(gameList);
                        break;
                    case 2:
                        returnGame();
                        break;
                    case 3:
                        loadCustomerData();
                        browseAvailableGames(gameList);
                        break;
                    case 4:
                        viewRentedGames();
                        break;
                    case 5:
                        searchGame(gameList);
                        break;
                    case 6:
                        purchaseMerchandise(merchList);
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        return;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number. Press enter to continue and try again");
                scanner.nextLine();  // consume the invalid input
            }
        }
    }

    // method to rent a game
    public void rentGame(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);

        // prompt user to enter the ID of the game they want to rent
        System.out.print("Enter the ID of the game you want to rent: ");
        int gameIDToRent = scanner.nextInt();
        scanner.nextLine();

        // find the game in the inventory using a similar method to what the clerk uses
        Item gameToRent = findItemByID(gameIDToRent, gameList);

        // rent the game if found and available and error handling
        if (gameToRent != null && gameToRent.isAvailable()) {
            // adds game to rented games arraylist
            rentedGames.add(gameToRent);
            saveRentedGames();
            //Test print statement
            //System.out.println("file saved");
            // changes game stock and available if affected
            gameToRent.setStock(gameToRent.getStock() - 1);
            // Check availability
            if (gameToRent.getStock() == 0) {
                gameToRent.setAvailable(false);
            }
            // confirmation statement
            System.out.println("Game rented successfully: " + gameToRent.getTitle());
        } else if (gameToRent != null && gameToRent.getStock() == 0) {
            System.out.println("The selected game is out of stock.");
        } else if (gameToRent != null) {
            System.out.println("The selected game is not available for rent.");
        } else {
            System.out.println("Game not found in inventory. Please use option 3 to view games.");
        }
    }

    // method to return a game
    public void returnGame() {
        Scanner scanner = new Scanner(System.in);

        // prompt user to enter the ID of the game they want to return
        System.out.print("Enter the ID of the game you want to return: ");
        int gameIDToReturn = scanner.nextInt();

        // find the game in the rented games list
        Item gameToReturn = findItemByID(gameIDToReturn, rentedGames);

        // return the game if found
        if (gameToReturn != null) {
            // remove object from rented games arraylist
            rentedGames.remove(gameToReturn);
            saveRentedGames();
            //Test print statement
            //System.out.println("file saved");
            // increase stock by 1
            gameToReturn.setStock(gameToReturn.getStock() + 1);
            // change parameter to available if stock was previously 0
            if (gameToReturn.getStock() > 0 && !gameToReturn.isAvailable()) {
                gameToReturn.setAvailable(true);
            }
            System.out.println("Game returned successfully: " + gameToReturn.getTitle());
        } else {
            System.out.println("Game not found in rented games list.");
        }
    }

    // method to browse available games
    public void browseAvailableGames(List<Item> gameList) {
        System.out.println("\nAvailable Games:");
        displayInventory(gameList);
    }

    // method to view currently rented games
    public void viewRentedGames() {
        System.out.println("\nCurrently Rented Games:");
        if (rentedGames.isEmpty()) {
            System.out.println("Nothing here.");
        } else {
            // same formatted outputting as the clerk method
            System.out.printf("%-5s | %-16s | %-15s | %-25s%n","ID", "Title", "Genre", "Additional Details");
            System.out.println("----------------------------------------------------------------------------");
            for (Item game : rentedGames) {
                System.out.println(game.getRentedGameDetails());
            }
        }
    }


    // method to search for a game by title
    public void searchGame(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);

        // prompt user to enter the title of the game they want to search
        System.out.print("Enter the title of the game you want to search: ");
        String titleToSearch = scanner.nextLine().toLowerCase();  // Keep the original input

        // search for the game in the inventory
        List<Item> foundGame = findGameByTitle(titleToSearch, gameList);

        // display the search result
        if (!foundGame.isEmpty()) {
            System.out.println("\nGame Details:");
            System.out.printf("%-8s | %-27s | %-22s | %-8s | %-15s | %-20s%n","ID", "Title", "Genre", "Stock", "Available", "Additional Details");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            for (Item game : foundGame) {
                System.out.println(game);
            }
        } else {
            System.out.println("Game not found in inventory. Please use option 3 to view games.");
        }
    }

    // method to display the arraylists
    private void displayInventory(List<Item> games) {
        if (games.isEmpty()) {
            System.out.println("Nothing here.");
        } else {
            // same formatted outputting as the clerk method
            System.out.printf("%-8s | %-27s | %-22s | %-8s | %-15s | %-20s%n","ID", "Title", "Genre", "Stock", "Available", "Additional Details");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
            for (Item game : games) {
                System.out.println(game.toString());
            }
        }
    }

    // method to find a game by ID in a list similar to clerk one
    private Item findItemByID(int itemID, List<Item> inventory) {
        //for loop to find object based on ID
        for (Item item : inventory) {
            if (item.getID() == itemID) {
                return item;
            }
        }
        return null;
    }

    // method to find a game by title in a list
    private List<Item> findGameByTitle(String title, List<Item> gameList) {
        // Filter the games based on the modified criteria
        return gameList.stream()
                .filter(game -> Pattern.compile("\\s").matcher(game.getTitle().toLowerCase()).replaceAll("")
                        .contains(Pattern.compile("\\s").matcher(title).replaceAll("")))
                .toList();
    }

    // method to handle merchandise purchasing
    public void purchaseMerchandise(List<Merchandise> merchandiseList) {
        Scanner scanner = new Scanner(System.in);
        shoppingCart = new ArrayList<>();

        // while loop to enable continuous purchasing
        while (true) {
            System.out.println("\nMerchandise Menu:");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Go Back");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume the newline after int

                switch (choice) {
                    case 1:
                        addMerchandiseToCart(merchandiseList);
                        break;
                    case 2:
                        viewShoppingCart();
                        break;
                    case 3:
                        finishShopping(merchandiseList);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number. Press enter to continue and try again");
                scanner.nextLine();  // Consume the invalid input
            }
        }
    }

    // method to add merchandise items to the shopping cart
    private void addMerchandiseToCart(List<Merchandise> merchandiseList) {
        Scanner scanner =  new Scanner(System.in);

        System.out.println("Available Merchandise:");
        displayMerchandiseList(merchandiseList);
        System.out.println("---------------------------------------------------------------------------------------------");

        System.out.print("Enter the ID of the merchandise you want to add to the cart: ");
        int merchID = scanner.nextInt();

        // assigning merchandise for further operation and parsing it through finder
        Merchandise selectedMerchandise = findMerchandiseByID(merchID, merchandiseList);

        if (selectedMerchandise != null) {
            // save the original index before adding to the cart
            originalMerchandiseIndexes.add(merchandiseList.indexOf(selectedMerchandise));

            // save stock to new list
            originalMerchandiseStocks.add(selectedMerchandise.getStock());


            // decrease the stock by 1
            selectedMerchandise.setStock(selectedMerchandise.getStock() - 1);

            // if logic for when stock hits zero to remove from merchList
            if (selectedMerchandise.getStock() == 0) {
                int indexToRemove = merchandiseList.indexOf(selectedMerchandise);
                removedMerchandise.add(merchandiseList.remove(indexToRemove));

                // for logic to adjust original indexes for removed items
                for (int i = 0; i < originalMerchandiseIndexes.size(); i++) {
                    if (originalMerchandiseIndexes.get(i) > indexToRemove) {
                        originalMerchandiseIndexes.set(i, originalMerchandiseIndexes.get(i) - 1);
                    }
                }
            }

            // add item to the cart
            shoppingCart.add(selectedMerchandise);
            // confirmation statement
            System.out.println(selectedMerchandise.getTitle() + " added to the cart.");
        } else {
            System.out.println("Invalid merchandise ID. Please try again.");
        }
    }


    // method to cancel the purchase and revert the merchandise stock
    private void cancelPurchase(List<Merchandise> merchandiseList) {
        // confirmation statement
        System.out.println("Canceling purchase. Reverting merchandise stock...");

        // for logic to restore stock
        for (Integer originalIndex : originalMerchandiseIndexes) {
            Merchandise merchandise = merchandiseList.get(originalIndex);
            merchandise.setStock(merchandise.getStock() + 1);
            // Check if the merchandise was previously removed

            if (removedMerchandise.contains(merchandise)) {
                int indexOfRemoved = removedMerchandise.indexOf(merchandise);
                merchandise.setStock(originalMerchandiseStocks.get(indexOfRemoved));
            }
        }

        // Add removed items back to the merchandise list
        merchandiseList.addAll(removedMerchandise);
        removedMerchandise.clear();
        // reset arraylist for next operation
        originalMerchandiseIndexes.clear();
        originalMerchandiseStocks.clear();
        // confirmation statement
        System.out.println("Purchase canceled. Cart cleared.");
        // reset cart
        shoppingCart.clear();
    }

    // method to view the shopping cart
    private void viewShoppingCart() {
        System.out.println("\nShopping Cart:");
        if (shoppingCart.isEmpty()) {
            System.out.println("Your cart is empty. Please add items using option 1.");
        } else {
            System.out.printf("%-9s | %-20s | %-20s | %-10s\n", "ID", "Name", "Merch Type", "Price");
            System.out.println("-------------------------------------------------------------------");
            for (Merchandise merchandise : shoppingCart) {
                System.out.printf("%-9s | %-20s | %-20s | $%-10.2f\n",
                        merchandise.getID(), merchandise.getTitle(), merchandise.getGenre(), merchandise.getPrice());
            }
            System.out.println("-------------------------------------------------------------------");
        }
    }


    // New method to finish shopping and proceed with the purchase
    private void finishShopping(List <Merchandise> merchList) {
        // if logic to check if there are items in cart
        if (shoppingCart.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items using option 1.");
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\nFinal Shopping Cart:");
            viewShoppingCart();

            // calculate total price of cart items
            float totalPrice = calculateTotalPrice(shoppingCart);
            System.out.println("Total Price: $" + totalPrice);

            System.out.print("Do you want to proceed with the purchase? (yes/no): ");
            String purchaseDecision = scanner.nextLine().toLowerCase();

            if ("yes".equals(purchaseDecision)) {
                makePurchase();
            } else {
                // Cancel the purchase and revert merchandise stock
                cancelPurchase(merchList);
            }
        }
    }

    // method to make the purchase and display a receipt
    private void makePurchase() {
        // display receipt
        System.out.println("\nPurchase Receipt:");
        viewShoppingCart();
        float totalPrice = calculateTotalPrice(shoppingCart);
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("Thank you for your purchase!");

        // clear the shopping cart after a successful purchase
        shoppingCart.clear();
    }

    // helper method to display a list of merchandise items
    private void displayMerchandiseList(List<Merchandise> merchandiseList) {
        System.out.printf("%-5s | %-20s | %-30s | %-11s | %-10s\n", "ID", "Name", "Merchandise Type", "Price", "Stock");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Merchandise merchandise : merchandiseList) {
            System.out.printf("%-5s | %-20s | %-30s | $%-10.2f | %-10s\n",
                    merchandise.getID(), merchandise.getTitle(), merchandise.getGenre(), merchandise.getPrice(), merchandise.getStock());
        }
    }

    // helper method to find merchandise by ID
    private Merchandise findMerchandiseByID(int merchID, List<Merchandise> merchandiseList) {
        for (Merchandise merchandise : merchandiseList) {
            if (merchandise.getID() == merchID) {
                return merchandise;
            }
        }
        return null;
    }

    // helper method to calculate the total price of merchandise in the shopping cart
    private float calculateTotalPrice(List<Merchandise> merchandiseList) {
        float totalPrice = 0;
        for (Merchandise merchandise : merchandiseList) {
            totalPrice += merchandise.getPrice();
        }
        return totalPrice;
    }
}

