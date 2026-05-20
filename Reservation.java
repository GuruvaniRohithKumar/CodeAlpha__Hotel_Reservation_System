public class Reservation {
    private String reservationId;
    private String guestName;
    private String contactNumber; 
    private Room room;
    private int nights;
    private boolean isCancelled;
    private String paymentMethod; // New field tracking choice

    public Reservation(String reservationId, String guestName, String contactNumber, Room room, int nights, String paymentMethod) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.contactNumber = contactNumber; 
        this.room = room;
        this.nights = nights;
        this.isCancelled = false;
        this.paymentMethod = paymentMethod;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getContactNumber() { return contactNumber; } 
    public Room getRoom() { return room; }
    public int getNights() { return nights; }
    public boolean isCancelled() { return isCancelled; }
    public String getPaymentMethod() { return paymentMethod; }
    
    public void cancel() { this.isCancelled = true; }

    public double calculateTotalBill() {
        return room.getPricePerNight() * nights;
    }

    @Override
    public String toString() {
        return "Booking ID: " + reservationId + "\n" +
               "Guest Name: " + guestName + "\n" +
               "Contact Number: " + contactNumber + "\n" + 
               "Room Details: Room " + room.getRoomNumber() + " (" + room.getType() + ")\n" +
               "Duration: " + nights + " nights\n" +
               "Total Bill: $" + calculateTotalBill() + "\n" +
               "Payment Method: " + paymentMethod + "\n" +
               "Status: " + (isCancelled ? "CANCELLED" : "CONFIRMED");
    }
}