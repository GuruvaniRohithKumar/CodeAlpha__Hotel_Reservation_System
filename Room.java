rmpublic class Room {
    // Enum for categorization
    public enum RoomType {
        STANDARD, DELUXE, SUITE
    }

    private int roomNumber;
    private RoomType type;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, RoomType type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true; // Rooms are available by default
    }

    // Getters and Setters
    public int getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public double getPricePerNight() { return pricePerNight; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Room #" + roomNumber + " [" + type + "] - $" + pricePerNight + "/night (" + 
               (isAvailable ? "Available" : "Booked") + ")";
    }
}