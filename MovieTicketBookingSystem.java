import java.util.Scanner;
import java.util.HashMap;

public class MovieTicketBookingSystem {
    static int bookedSeat = 0;
    static int prizeOfTicket = 0;
    static int totalIncome = 0;
    static int row;
    static int seats;
    static int totalSeat;
    static String[][] bookedTicketPerson;
    static HashMap<String, HashMap<String, String>> tableOfChart;
    static HashMap<String, String> users;

    static String currentUser;

    static class Chart {
        static HashMap<String, HashMap<String, String>> chartMaker() {
            HashMap<String, HashMap<String, String>> seatsChart = new HashMap<>();
            for (int i = 0; i < row; i++) {
                HashMap<String, String> seatsInRow = new HashMap<>();
                for (int j = 0; j < seats; j++) {
                    seatsInRow.put(Integer.toString(j + 1), "S");
                }
                seatsChart.put(Integer.toString(i), seatsInRow);
            }
            return seatsChart;
        }

        static double findPercentage() {
            return (double) (bookedSeat * 100) / totalSeat;
        }
    }

    // Initialize hardcoded admin credentials
    static {
        users = new HashMap<>();
        users.put("admin", "password");
    }

    // Method to create a new user
    static void createUser(String username, String password) {
        users.put(username, password);
        System.out.println("User created successfully!");
    }

    // Method to validate login credentials
    static boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of Rows: ");
        row = scanner.nextInt();
        System.out.println("Enter number of seats in a Row: ");
        seats = scanner.nextInt();
        totalSeat = row * seats;

        bookedTicketPerson = new String[row][seats];
        tableOfChart = Chart.chartMaker();

        while (true) {
            // Prompt for login or user creation
            System.out.println("Welcome to Movie Ticket Booking System");
            System.out.println("1. Log in");
            System.out.println("2. Create a new user");
            System.out.println("0. Exit");
            System.out.print("Select option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (option == 0) {
                System.out.println("Exiting...");
                break;
            }

            switch (option) {
                case 1:
                    // Log in
                    System.out.print("Username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Password: ");
                    String loginPassword = scanner.nextLine();
                    if (login(loginUsername, loginPassword)) {
                        currentUser = loginUsername;
                        System.out.println("Login successful!");
                        runBookingSystem(scanner);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                case 2:
                    // Create a new user
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    if (users.containsKey(newUsername)) {
                        System.out.println("Username already exists. Please choose a different username.");
                        continue;
                    }
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    createUser(newUsername, newPassword);
                    break;

                default:
                    System.out.println("Invalid option. Exiting...");
                    break;
            }
        }
    }

    static void runBookingSystem(Scanner scanner) {
        while (true) {
            if (currentUser.equals("admin")) {
                System.out.println("1. Show the seats\n2. Statistics\n" +
                        "3. Show booked Tickets User Info\n4. Show all booked Tickets User Info\n5. Logout\n0. Exit");
            } else {
                System.out.println("1. Show the seats\n2. Buy a Ticket\n3. Logout\n0. Exit");
            }
            System.out.print("Select Option: ");
            int option = scanner.nextInt();
            if (option == 0) {
                System.out.println("Exiting...");
                break;
            } else if (currentUser.equals("admin")) {
                if (option == 1) {
                    showSeats();
                } else if (option == 2) {
                    showStatistics();
                } else if (option == 3) {
                    showBookedTicketUserInfo(scanner);
                } else if (option == 4) {
                    showAllBookedTicketUserInfo();
                } else if (option == 5) {
                    System.out.println("Logging out...");
                    currentUser = null;
                    break;
                } else {
                    System.out.println("*** Invalid Input ***");
                }
            } else {
                if (option == 1) {
                    showSeats();
                } else if (option == 2) {
                    buyTicket(scanner);
                } else if (option == 3) {
                    System.out.println("Logging out...");
                    currentUser = null;
                    break;
                } else {
                    System.out.println("*** Invalid Input ***");
                }
            }
        }
    }

    static void showSeats() {
        for (int seat = 0; seat < seats; seat++) {
            System.out.print(seat + " ");
        }
        System.out.println(seats);

        for (String num : tableOfChart.keySet()) {
            System.out.print((Integer.parseInt(num) + 1) + " ");
            for (String no : tableOfChart.get(num).values()) {
                System.out.print(no + " ");
            }
            System.out.println();
        }
        System.out.println("Vacant Seats = " + (totalSeat - bookedSeat) + "\n");
    }

    static void buyTicket(Scanner scanner) {
        System.out.println("Enter Row Number: ");
        int rowNumber = scanner.nextInt();
        System.out.println("Enter Column Number: ");
        int columnNumber = scanner.nextInt();
        if (rowNumber >= 1 && rowNumber <= row && columnNumber >= 1 && columnNumber <= seats) {
            if (tableOfChart.get(Integer.toString(rowNumber - 1)).get(Integer.toString(columnNumber)).equals("S")) {
                if (row * seats <= 60) {
                    prizeOfTicket = 10;
                } else if (rowNumber <= row / 2) {
                    prizeOfTicket = 10;
                } else {
                    prizeOfTicket = 8;
                }
                // Calculate percentage of vacant seats
                double vacantPercentage = ((double) (totalSeat - bookedSeat) / totalSeat) * 100;

                // Adjust ticket price based on vacant seats percentage
                if (vacantPercentage <= 20) {
                    prizeOfTicket *= 1.3;
                } else if (vacantPercentage <= 50) {
                    prizeOfTicket *= 1.2;
                }
                System.out.println("Ticket Price - $" + prizeOfTicket);
                System.out.println("Confirm booking (yes/no): ");
                String confirm = scanner.next();
                if (confirm.equalsIgnoreCase("yes")) {
                    HashMap<String, String> personDetail = new HashMap<>();
                    System.out.println("Enter Name: ");
                    personDetail.put("Name", scanner.next());
                    System.out.println("Enter Gender: ");
                    personDetail.put("Gender", scanner.next());
                    System.out.println("Enter Age: ");
                    personDetail.put("Age", scanner.next());
                    System.out.println("Enter Phone number: ");
                    personDetail.put("Phone_No", scanner.next());
                    personDetail.put("Ticket_Price", Integer.toString(prizeOfTicket));
                    tableOfChart.get(Integer.toString(rowNumber - 1)).put(Integer.toString(columnNumber), "B");
                    bookedSeat++;
                    totalIncome += prizeOfTicket;
                    bookedTicketPerson[rowNumber - 1][columnNumber - 1] = personDetail.toString();
                    System.out.println("Booked Successfully");
                }
            } else {
                System.out.println("This seat is already booked by someone.");
            }
        } else {
            System.out.println("*** Invalid Input ***");
        }
    }

    static void showStatistics() {
        System.out.println("Number of purchased Tickets - " + bookedSeat);
        System.out.println("Percentage - " + Chart.findPercentage() + "%");
        System.out.println("Current Income - $" + prizeOfTicket);
        System.out.println("Total Income - $" + totalIncome);
    }

    static void showBookedTicketUserInfo(Scanner scanner) {
        System.out.println("Enter Row number: ");
        int enterRow = scanner.nextInt();
        System.out.println("Enter Column number: ");
        int enterColumn = scanner.nextInt();
        if (enterRow >= 1 && enterRow <= row && enterColumn >= 1 && enterColumn <= seats) {
            if (tableOfChart.get(Integer.toString(enterRow - 1)).get(Integer.toString(enterColumn)).equals("B")) {
                String person = bookedTicketPerson[enterRow - 1][enterColumn - 1];
                System.out.println(person);
            } else {
                System.out.println("---**---  Vacant seat  ---**---");
            }
        } else {
            System.out.println("*** Invalid Input ***");
        }
    }

    static void showAllBookedTicketUserInfo() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < seats; j++) {
                if (bookedTicketPerson[i][j] != null) {
                    System.out.println("Seat (" + (i + 1) + ", " + (j + 1) + "): " + bookedTicketPerson[i][j]);
                }
            }
        }
    }
}


