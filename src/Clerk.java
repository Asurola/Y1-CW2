import java.util.*;

public class Clerk extends user implements Inventory {
    //attributes
    private final String clerkPassword = "admin123"; // Pre-defined password for the clerk

    //constructor
    public Clerk(String name) {
        super(name);
    }

    // overridden login function
    @Override
    public boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n(For the sake of practicality the password set to be fixed as 'admin123')");
        System.out.print("\nEnter password: ");
        String passwordAttempt = scanner.nextLine();

        if (passwordAttempt.equals(clerkPassword)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Incorrect password. Login failed.");
        }
        return false;
    }

    //overridden register
    @Override
    public void register() {
        // Clerks cannot register
        System.out.println("clerks cannot register.");
    }

    //overridden extra function
    @Override
    public void forgetID() {
        System.out.println("clerks cannot forget their ID.");
    }

    //clerk menu
    public void clerkMenu(List<Item> gameList, List<Merchandise> merchandiseList) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nClerk Menu:");
            System.out.println("1. Add Game");
            System.out.println("2. Remove Game");
            System.out.println("3. Display Game Inventory");
            System.out.println("4. Modify Game(s)");
            System.out.println("5. Add Merchandise");
            System.out.println("6. Remove Merchandise");
            System.out.println("7. Browse Merchandise Inventory");
            System.out.println("8. Modify Merchandise");
            System.out.println("9. Log Out");
            System.out.print("Enter your choice: ");


            //try-catch for input validation
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume the new line after an integer

                // options redirect to another menu
                switch (choice) {
                    case 1 -> addGame(gameList);
                    case 2 -> removeGame(gameList);
                    case 3 -> displayInventory(gameList);
                    case 4 -> modifyGame(gameList);
                    case 5 -> addMerchandise(merchandiseList);
                    case 6 -> removeMerchandiseByID(merchandiseList);
                    case 7 -> displayMerchandise(merchandiseList);
                    case 8 -> modifyMerchandiseByID(merchandiseList);
                    case 9 -> {
                        System.out.println("Logging out...");
                        return;  // return to the previous menu
                    }
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number. Press enter to continue and try again");
                scanner.nextLine();  // consume the invalid input
            }
        }
    }

    // overrides interface
    @Override
    public void addGame(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nSelect the type of game:");
        System.out.println("1. Board Game");
        System.out.println("2. Video Game");
        System.out.print("Enter your choice: ");

        // try-catch for input validation
        try {
            int gameTypeChoice = scanner.nextInt();
            scanner.nextLine();  // consume the newline after integer

            switch (gameTypeChoice) {
                case 1:
                    addBoardGame(gameList);
                    break;
                case 2:
                    addVideoGame(gameList);
                    break;
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number. Press enter to continue and try again");
            scanner.nextLine();  // consume the invalid input
        }
    }

    // method to add specific item
    public void addBoardGame(List<Item> gameList) {
        String title;
        Scanner scanner = new Scanner(System.in);

        // game input validation for name
        while (true) {
            boolean notFound = true;
            System.out.print("Enter the title of the board game: ");
            title = scanner.nextLine();

            for (Item game : gameList) {
                if (game instanceof BoardGame && title.equalsIgnoreCase(game.getTitle())) {
                    System.out.println("There is a board game that already exists with this name. Please enter another name.");
                    notFound = false;
                    break;
                }
            }

            if (notFound) {
                break;
            }
        }

        System.out.print("Enter the genre of the board game: ");
        String genre = scanner.nextLine();

        // Input validation to ensure input is an integer using try-catch
        int numPlayers = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter the number of players for the board game: ");
                numPlayers = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for the number of players.");
            }
        }

        System.out.print("Enter the publisher of the board game: ");
        String publisher = scanner.nextLine();

        System.out.print("Enter how much stock is available for the board game: ");
        int stock = scanner.nextInt();

        // creates a new BoardGame object (ID automatically created and default for availability is true)
        Item newGame = new BoardGame(title, genre, numPlayers, publisher, stock);

        // adds the new game to the gameList arraylist
        gameList.add(newGame);
        // confirmation statement
        System.out.println("Board Game added to inventory: " + newGame.getTitle());
    }

    // method to add a video game object
    public void addVideoGame(List<Item> gameList) {
        String title;
        Scanner scanner = new Scanner(System.in);

        // game input validation for name
        while (true) {
            boolean notFound = true;
            System.out.print("Enter the title of the video game: ");
            title = scanner.nextLine();

            for (Item game : gameList) {
                if (game instanceof VideoGame && title.equalsIgnoreCase(game.getTitle())) {
                    System.out.println("There is a video game that already exists with this name. Please enter another name.");
                    notFound = false;
                    break;
                }
            }

            if (notFound) {
                break;
            }
        }

        System.out.print("Enter the genre of the video game: ");
        String genre = scanner.nextLine();


        // input validation for the release year, restricting it to 4 digits. Uses try-catch
        int releaseYear = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter the release year of the video game: ");
                releaseYear = Integer.parseInt(scanner.nextLine());
                if (String.valueOf(releaseYear).length() == 4) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a 4-digit year.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for the release year.");
            }
        }

        // platforms input. Creates a list for it and allows individual input of multiple platforms
        System.out.print("Enter the platform(s) of the video game (please use commas to separate the platforms): ");
        // Sets the platform to uppercase the platforms for more consistency
        String platform = scanner.nextLine().toUpperCase(Locale.ROOT);
        List<String> platforms = new ArrayList<>(Arrays.asList(platform.split(",")));

        System.out.print("Enter how much stock is available for the video game: ");
        int stock = scanner.nextInt();

        // creates a new VideoGame object
        Item newGame = new VideoGame(title, genre, platforms, releaseYear, stock);

        // adds the new game to the gameList arraylist
        gameList.add(newGame);
        //confirmation statement
        System.out.println("Video Game added to inventory: " + newGame.getTitle());
    }

    //removeGame method
    @Override
    public void removeGame(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the game to remove: ");
        int gameIDToRemove = scanner.nextInt();

        //passes enter ID through function that can locate the object
        Item gameToRemove = findItemByID(gameIDToRemove, gameList);

        //logic to remove and error validation
        if (gameToRemove != null) {
            //removes object from arraylist
            gameList.remove(gameToRemove);
            //confirmation statement
            System.out.println("Game removed from inventory: " + gameToRemove.getTitle());
        } else {
            //error statement
            System.out.println("Game not found in inventory.");
        }
    }

    // method to modify game details in gameList
    public void modifyGame(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the game you want to modify: ");
        int gameID = scanner.nextInt();

        // uses the helper emthod findGameByID
        Item game = findItemByID(gameID, gameList);

        // error-handling if-loop
        if (game != null) {
            System.out.println("\nGame found. Current details:");
            System.out.printf("%-8s | %-27s | %-22s | %-8s | %-15s | %-20s%n","ID", "Title", "Genre", "Stock", "Available", "Additional Details");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(game);

            // prompt the clerk to select which attribute to change
            System.out.println("\nSelect the attribute to change:");
            System.out.println("1. Title");
            System.out.println("2. Genre");
            System.out.println("3. Stock");

            // displays additional options based on the type of object identified
            if (game instanceof BoardGame) {
                System.out.println("4. Number of Players");
                System.out.println("5. Publisher");
            } else if (game instanceof VideoGame) {
                System.out.println("4. Release Year");
                System.out.println("5. Platform");
            }

            System.out.print("Enter your choice: ");
            int attributeChoice = scanner.nextInt();
            scanner.nextLine();  // consume the newline space after integer input

            // update the selected attribute based on clerk choice
            switch (attributeChoice) {
                case 1 -> {
                    System.out.print("Enter the new title: ");
                    String newTitle = scanner.nextLine();
                    game.setTitle(newTitle);
                }
                case 2 -> {
                    System.out.print("Enter the new genre: ");
                    String newGenre = scanner.nextLine();
                    game.setGenre(newGenre);
                }
                case 3 -> {
                    System.out.print("Enter the new stock: ");
                    int newStock = scanner.nextInt();
                    game.setStock(newStock);
                    // logic to manage stock and available interaction
                    if (game.getStock() == 0) {
                        game.setAvailable(false);
                    } else if (game.getStock() > 0) {
                        game.setAvailable(true);
                    }
                }
                case 4 -> {
                    // object specific options
                    if (game instanceof BoardGame) {
                        System.out.print("Enter the new number of players: ");
                        int newNumPlayers = scanner.nextInt();
                        ((BoardGame) game).setNumPlayers(newNumPlayers);
                    } else if (game instanceof VideoGame) {
                        System.out.print("Enter the new release year: ");
                        int newReleaseYear = scanner.nextInt();
                        ((VideoGame) game).setReleaseYear(newReleaseYear);
                    }
                }
                case 5 -> {
                    // object specific options
                    if (game instanceof BoardGame) {
                        System.out.print("Enter the new publisher: ");
                        String newPublisher = scanner.nextLine();
                        ((BoardGame) game).setPublisher(newPublisher);
                    } else if (game instanceof VideoGame) {
                        System.out.print("Enter the new platform (please use commas to separate the platforms): ");
                        List<String> newPlatform = Collections.singletonList(scanner.nextLine().toUpperCase());
                        ((VideoGame) game).setPlatforms(newPlatform);
                    }
                }
                default -> {
                    System.out.println("Invalid choice. No changes made.");
                    return;
                }
            }
            System.out.println("Game details updated successfully.");
        } else {
            System.out.println("Game not found.");
        }
    }

    // method to add merchandise to the merchList
    public void addMerchandise(List<Merchandise> merchList) {
        String title;
        Scanner scanner = new Scanner(System.in);

        // merch input validation for name
        while(true) {
            boolean notFound = false;
            System.out.print("Enter the name of the merchandise: ");
            title = scanner.nextLine();

            for (Item merch : merchList) {
                if (title.equalsIgnoreCase(merch.getTitle())) {
                    System.out.println("There is a merch that already exists with this name. Please enter another name");
                    notFound = true;
                }
            }
            if (!notFound){
                break;
            }
        }

        System.out.print("Enter the type of the merchandise: ");
        String genre = scanner.nextLine();

        System.out.print("Enter the price of the merchandise: ");
        float price = scanner.nextFloat();
        scanner.nextLine();  // consume the newline after integer input

        System.out.println("Enter how much stock is available for the merchandise: ");
        int stock = scanner.nextInt();

        // create a new Merchandise object
        Merchandise newMerchandise = new Merchandise(title, genre, price, stock);

        // add the new merchandise to the merchList
        merchList.add(newMerchandise);
        System.out.println("Merchandise added to inventory: " + newMerchandise.getTitle());
    }

    // method to remove merchandise from the inventory
    public void removeMerchandise(Item merchandise, List<Merchandise> merchList) {
        if (merchList.contains(merchandise)) {
            merchList.remove(merchandise);
            System.out.println("Merchandise removed from inventory: " + merchandise.getTitle());
        } else {
            System.out.println("Merchandise not found in inventory.");
        }
    }

    // helper method to remove merchandise from the inventory
    public void removeMerchandiseByID(List<Merchandise> inventory) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the merchandise to remove: ");
        int merchID = scanner.nextInt();

        // assignment and finder
        Item merchandise = findMerchandiseByID(merchID, inventory);

        if (merchandise != null) {
            removeMerchandise(merchandise, inventory);
        } else {
            System.out.println("Merchandise not found in inventory.");
        }
    }

    // method to display the merchList
    public static void displayMerchandise(List<Merchandise> merchList) {
        if (!merchList.isEmpty()) {
            System.out.println("\nMerchandise Inventory:");
            System.out.printf("%-10s | %-20s | %-20s | %-10s | %-10s\n", "ID", "Name", "Merchandise Type", "Price", "Stock");
            System.out.println("-------------------------------------------------------------------------------------------");
            for (Item item : merchList) {
                if (item instanceof Merchandise merchandise) {
                    System.out.printf("%-10s | %-20s | %-20s | $%-9.2f | %-10s\n",
                            merchandise.getID(), merchandise.getTitle(), merchandise.getGenre(), merchandise.getPrice(), merchandise.getStock());
                }
                }
            } else {
            System.out.println("\nMerchandise List is empty");
        }
    }


    // method to modify merchandise details
    public void modifyMerchandise(Item merchandise, List<Merchandise> merchList) {
        Scanner scanner = new Scanner(System.in);

        // if logic for modify menu
        if (merchList.contains(merchandise)) {
            // display merchandise details
            System.out.println("\nMerchandise Details:");
            System.out.println("Name: " + merchandise.getTitle());
            System.out.println("Merchandise Type: " + merchandise.getGenre());
            System.out.println("Price: $" + ((Merchandise) merchandise).getPrice());
            System.out.println("Stock: " + merchandise.getStock());

            // prompt clerk to select which detail to change
            System.out.println("\nSelect the attribute to modify:");
            System.out.println("1. Name");
            System.out.println("2. Merchandise Type");
            System.out.println("3. Price");
            System.out.println("4. Stock");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume the newline after integer input

            switch (choice) {
                case 1:
                    System.out.print("Enter the new name: ");
                    merchandise.setTitle(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter the new type: ");
                    merchandise.setGenre(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter the new price: ");
                    ((Merchandise) merchandise).setPrice(scanner.nextFloat());
                    scanner.nextLine();  // consume the newline after integer input
                    break;
                case 4:
                    System.out.print("Enter the new stock: ");
                    //stock and removal interaction
                    merchandise.setStock(scanner.nextInt());
                    if (merchandise.getStock() == 0) {
                        merchList.remove(merchandise);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. No modifications made.");
            }

            System.out.println("Merchandise details modified successfully.");
        } else {
            System.out.println("Merchandise not found in inventory.");
        }
    }

    // helper method to modify merchandise details by ID
    public void modifyMerchandiseByID(List<Merchandise> merchList) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter the ID of the merchandise to modify: ");
        int merchID = scanner.nextInt();

        //assignment and finder
        Item merchandise = findMerchandiseByID(merchID, merchList);

        if (merchandise != null) {
            modifyMerchandise(merchandise, merchList);
        } else {
            System.out.println("Merchandise not found in inventory.");
        }
    }

    // object locator search function
    private Item findMerchandiseByID(int itemID, List<Merchandise> inventory) {
        //for loop to find object based on ID
        for (Item item : inventory) {
            if (item.getID() == itemID) {
                return item;
            }
        }
        return null;
    }

    // object locator search function
    private Item findItemByID(int itemID, List<Item> inventory) {
        //for loop to find object based on ID
        for (Item item : inventory) {
            if (item.getID() == itemID) {
                return item;
            }
        }
        return null;
    }

    // method to display the inventory (arraylist) in a formatted output
    @Override
    public void displayInventory(List<Item> gameList) {
        System.out.println("\nInventory:");

        // if loop to handle an empty arraylist if scenario is such
        if (gameList.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            // formatted output
            System.out.printf("%-8s | %-27s | %-22s | %-8s | %-15s | %-20s%n","ID", "Title", "Genre", "Stock", "Available", "Additional Details");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (Item game : gameList) {
                //using the toString function defined in the abstract class
                System.out.println(game.toString());
            }
        }
    }
}
