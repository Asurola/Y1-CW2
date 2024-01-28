import java.util.*;

public class Store {

    //attributes and arraylist initialisation
    // arraylist for games
    private static List<Item> gameList = new ArrayList<>();

    //arraylist for merchandise
    private static List<Merchandise> merchList = new ArrayList<>();

    //array list for customers
    private static List<Customer> registeredCustomers = new ArrayList<>();

    //hashmap as chosen data structure for customer details
    protected static final HashMap<String, String> userCredentials = new HashMap<>();

    //main()
    public static void main(String[] args) {

        // code to ensure that the data in the file is deleted properly in between every debugging
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Customer.deleteDataFile()));
//        Test print statement
//        System.out.println("Data file deleted");

        Scanner scanner = new Scanner(System.in);
        Clerk adminClerk = new Clerk("John");

        //pre-defined items added for testing
        gameList.add(new BoardGame("Monopoly", "Real-estate", 4, "Hasbro", 1));
        gameList.add(new VideoGame("Super Mario Bros", "Platformer", Arrays.asList("Nintendo Switch", "Nintendo Wii"), 1985, 4));
        merchList.add(new Merchandise("Halo Hoodie", "Wearable apparel", 29.99F, 4));
        merchList.add(new Merchandise("Gaming Mouse", "Electronic accessory", 49.99F, 8));
        merchList.add(new Merchandise("Star Wars T-Shirt", "Apparel", 19.99F, 10));


        // Main menu loop
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Clerk Login");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume the newline after int

                switch (choice) {
                    case 1:
                        if (adminClerk.login()) {
                            adminClerk.clerkMenu(gameList, merchList);
                        }
                        break;
                    case 2:
                        customerMainMenu(gameList);
                        break;
                    case 3:
                        System.out.println("Exiting the program. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number. Press enter to continue and try again");
                scanner.nextLine();  // consume the invalid input
            } finally {

            }
        }
    }


    //method for customer main menu
    private static void customerMainMenu(List<Item> gameList) {
        Scanner scanner = new Scanner(System.in);
        Customer customer = null;


        // customer menu loop
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Customer Login");
            System.out.println("2. Register as a new customer");
            System.out.println("3. Forget ID");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume the newline after int

            switch (choice) {
                case 1:
                    customer = loginCustomer();
                    if (customer != null) {
                        customer.customerMenu(gameList, merchList);
                    }
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    forgetIDCustomer();
                    break;
                case 4:
                    return;  // return to the main menu
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    // login customer method
    private static Customer loginCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine().toLowerCase();

        System.out.print("Enter your ID: ");
        String enteredID = scanner.nextLine();

        // check if the entered ID matches the stored ID for the given username
        if (userCredentials.containsKey(username) &&
                userCredentials.get(username).equals(enteredID)) {
            System.out.println("Login successful!");

            // search for the existing Customer instance
            Customer customer = findCustomerByUsername(username);

            if (customer == null) {
                System.out.println("Customer not found.");
                return null;
            }

            // update available games list based on rented games
            updateAvailableGames(customer, gameList);

            return customer;
        } else {
            System.out.println("Incorrect username or ID. Login failed.");
            return null;
        }
    }

    // method to find a customer by username
    private static Customer findCustomerByUsername(String username) {
        for (Customer customer : registeredCustomers) {
            if (customer.getName().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    //register customer method
    private static void registerCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter your desired username: ");
        String username = scanner.nextLine().toLowerCase();

        // check if the username is already taken
        if (userCredentials.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }

        // generate a random ID for the new user
        String newID = user.generateRandomID();

        // create a new Customer instance
        Customer newCustomer = new Customer(username);

        // store the new user's credentials
        userCredentials.put(username, newID);

        // store the new Customer instance
        registeredCustomers.add(newCustomer);

        System.out.println("Registration successful! Your ID is: " + newID);
    }

    private static void forgetIDCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine().toLowerCase();

        // check if the entered username exists
        if (userCredentials.containsKey(username)) {
            String storedID = userCredentials.get(username);
            System.out.println("Your ID is: " + storedID);
        } else {
            System.out.println("Username not found. Please register or try again.");
        }
    }

    // method to update available games based on rented games
    private static void updateAvailableGames(Customer customer, List<Item> gameList) {
        List<Item> rentedGames = customer.getRentedGames();

        for (Item rentedGame : rentedGames) {
            Item availableGame = findItemByID(rentedGame.getID(), gameList);

            if (availableGame != null) {
                // Decrease stock and update availability
                availableGame.setStock(availableGame.getStock());

                if (availableGame.getStock() == 0) {
                    availableGame.setAvailable(false);
                }
            }
        }
    }

    // method to find a game by ID in a list similar to clerk one
    private static Item findItemByID(int itemID, List<Item> inventory) {
        for (Item item : inventory) {
            if (item.getID() == itemID) {
                return item;
            }
        }
        return null;
    }
}
