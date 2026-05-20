import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HotelSystem system = new HotelSystem();
        FileHandler.loadData(system); 
        
        // FIXED: Corrected System.in typo to resolve the 'util' field error
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(" Welcome to the Luxury Stay Text Terminal Deck");
        
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Reserve a Room");
            System.out.println("3. Look up Booking Invoice Details");
            System.out.println("4. Cancel Existing Reservation");
            System.out.println("5. Exit System Console");
            System.out.print("Select Operational Option: ");
            
            String choice = scanner.nextLine().trim();
            
            if (choice.equals("1")) {
                System.out.print("Enter Room Category Tier (STANDARD, DELUXE, SUITE): ");
                try {
                    Room.RoomType type = Room.RoomType.valueOf(scanner.nextLine().trim().toUpperCase());
                    List<Room> available = system.searchAvailableRooms(type);
                    if (available.isEmpty()) {
                        System.out.println("No open rooms listed under this tier category.");
                    } else {
                        for (Room r : available) {
                            System.out.println(" -> Room #" + r.getRoomNumber() + " ($" + r.getPricePerNight() + "/night)");
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(" Error: Invalid room tier category name entered.");
                }
                
            } else if (choice.equals("2")) {
                System.out.print("Enter Guest Full Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Enter Contact Number (10 digits): ");
                String phone = scanner.nextLine().trim();
                
                if (!phone.matches("\\d{10}")) {
                    System.out.println(" Validation Error: Mobile field must contain exactly 10 digits.");
                    continue;
                }
                
                System.out.print("Enter Target Room Number: ");
                int roomNum = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter Stay Duration (Nights): ");
                int nights = Integer.parseInt(scanner.nextLine().trim());
                
                // FIXED: Now correctly passes the payment method parameter string
                Reservation res = system.bookRoom(name, phone, roomNum, nights, "Console Cash");
                if (res != null) {
                    System.out.println("\n Room Reserved Successfully!");
                    System.out.println("Your Unique Booking Tracking ID is: " + res.getReservationId());
                } else {
                    System.out.println(" Booking Aborted: Target Room is either occupied or nonexistent.");
                }
                
            } else if (choice.equals("3")) {
                System.out.print("Enter Reservation ID: ");
                String resId = scanner.nextLine().trim();
                system.viewBookingDetails(resId);
                
            } else if (choice.equals("4")) {
                System.out.print("Enter Booking ID to Cancel: ");
                String resId = scanner.nextLine().trim();
                if (system.cancelBooking(resId)) {
                    System.out.println(" Reservation cancelled successfully. Room is vacant again.");
                } else {
                    System.out.println(" Could not process cancellation: ID not found or already cancelled.");
                }
                
            } else if (choice.equals("5")) {
                System.out.println("Shutting down terminal portal interaction. Goodbye!");
                break;
            } else {
                System.out.println("Invalid operational command choice.");
            }
        }
        scanner.close();
    }
}