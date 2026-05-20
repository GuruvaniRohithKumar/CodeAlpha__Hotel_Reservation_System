import java.io.*;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "bookings.txt";

    public static synchronized void saveData(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, false)))) {
            for (Reservation res : reservations) {
                writer.println(res.getReservationId() + "," +
                               res.getGuestName() + "," +
                               res.getContactNumber() + "," +
                               res.getRoom().getRoomNumber() + "," +
                               res.getNights() + "," +
                               res.isCancelled() + "," +
                               res.getPaymentMethod()); // Storing payment type
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("⚠️ Critical Error: Could not permanently save data to disk: " + e.getMessage());
        }
    }

    public static synchronized void loadData(HotelSystem system) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length == 7) { // 7 parameters parsed
                    String id = parts[0];
                    String name = parts[1];
                    String phone = parts[2]; 
                    int roomNum = Integer.parseInt(parts[3]);
                    int nights = Integer.parseInt(parts[4]);
                    boolean isCancelled = Boolean.parseBoolean(parts[5]);
                    String paymentMethod = parts[6];

                    Room room = system.findRoom(roomNum);
                    if (room != null) {
                        Reservation res = new Reservation(id, name, phone, room, nights, paymentMethod);
                        if (isCancelled) {
                            res.cancel();
                        } else {
                            room.setAvailable(false);
                        }
                        
                        boolean exists = false;
                        for (Reservation existing : system.getAllReservations()) {
                            if (existing.getReservationId().equalsIgnoreCase(id)) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            system.getAllReservations().add(res);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Warning: Data parsing mismatch.");
        }
    }
}