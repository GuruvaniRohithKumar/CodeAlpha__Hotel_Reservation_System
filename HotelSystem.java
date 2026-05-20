import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;

    public HotelSystem() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        initializeDummyRooms(); 
    }

    private void initializeDummyRooms() {
        // --- 10 STANDARD ROOMS ---
        rooms.add(new Room(101, Room.RoomType.STANDARD, 50.0));
        rooms.add(new Room(102, Room.RoomType.STANDARD, 50.0));
        rooms.add(new Room(103, Room.RoomType.STANDARD, 55.0));
        rooms.add(new Room(104, Room.RoomType.STANDARD, 55.0));
        rooms.add(new Room(105, Room.RoomType.STANDARD, 60.0));
        rooms.add(new Room(106, Room.RoomType.STANDARD, 60.0));
        rooms.add(new Room(107, Room.RoomType.STANDARD, 65.0));
        rooms.add(new Room(108, Room.RoomType.STANDARD, 65.0));
        rooms.add(new Room(109, Room.RoomType.STANDARD, 70.0));
        rooms.add(new Room(110, Room.RoomType.STANDARD, 70.0));

        // --- 10 DELUXE ROOMS ---
        rooms.add(new Room(201, Room.RoomType.DELUXE, 100.0));
        rooms.add(new Room(202, Room.RoomType.DELUXE, 100.0));
        rooms.add(new Room(203, Room.RoomType.DELUXE, 110.0));
        rooms.add(new Room(204, Room.RoomType.DELUXE, 110.0));
        rooms.add(new Room(205, Room.RoomType.DELUXE, 120.0));
        rooms.add(new Room(206, Room.RoomType.DELUXE, 120.0));
        rooms.add(new Room(207, Room.RoomType.DELUXE, 130.0));
        rooms.add(new Room(208, Room.RoomType.DELUXE, 130.0));
        rooms.add(new Room(209, Room.RoomType.DELUXE, 140.0));
        rooms.add(new Room(210, Room.RoomType.DELUXE, 140.0));

        // --- 10 SUITE ROOMS ---
        rooms.add(new Room(301, Room.RoomType.SUITE, 250.0));
        rooms.add(new Room(302, Room.RoomType.SUITE, 250.0));
        rooms.add(new Room(303, Room.RoomType.SUITE, 275.0));
        rooms.add(new Room(304, Room.RoomType.SUITE, 275.0));
        rooms.add(new Room(305, Room.RoomType.SUITE, 300.0));
        rooms.add(new Room(306, Room.RoomType.SUITE, 300.0));
        rooms.add(new Room(307, Room.RoomType.SUITE, 325.0));
        rooms.add(new Room(308, Room.RoomType.SUITE, 325.0));
        rooms.add(new Room(309, Room.RoomType.SUITE, 350.0));
        rooms.add(new Room(310, Room.RoomType.SUITE, 350.0));
    }

    public List<Room> searchAvailableRooms(Room.RoomType type) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getType() == type && room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public synchronized Reservation bookRoom(String guestName, String contactNumber, int roomNumber, int nights, String paymentMethod) {
        Room room = findRoom(roomNumber);
        
        if (room == null || !room.isAvailable()) {
            return null;
        }

        room.setAvailable(false);
        String reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        
        Reservation newBooking = new Reservation(reservationId, guestName, contactNumber, room, nights, paymentMethod);
        reservations.add(newBooking);
        FileHandler.saveData(reservations);
        
        return newBooking;
    }

    // FIXED: Added missing method required by Main.java console file
    public synchronized void viewBookingDetails(String reservationId) {
        boolean found = false;
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId)) {
                System.out.println("\n====================================");
                System.out.println(res.toString());
                System.out.println("====================================");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("❌ Error: No booking registry records match ID: " + reservationId);
        }
    }

    public boolean cancelBooking(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId) && !res.isCancelled()) {
                res.cancel();
                res.getRoom().setAvailable(true);
                FileHandler.saveData(reservations); 
                return true;
            }
        }
        return false;
    }
    
    public List<Room> getAllRooms() { return rooms; }
    public List<Reservation> getAllReservations() { return reservations; }
}