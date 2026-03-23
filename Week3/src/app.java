import java.util.*;

public class app {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);
        CancellationService cancelService = new CancellationService(bookingService, inventory);

        try {
            bookingService.bookRoom("RES101", "Alice", "Single Room");
            bookingService.bookRoom("RES102", "Bob", "Suite Room");

            cancelService.cancelBooking("RES102");

            cancelService.cancelBooking("RES999"); // should fail

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nSystem running with consistent state.");
    }
}

class Reservation {

    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increment(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }
}

class BookingService {

    private HashMap<String, Reservation> bookings;
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        bookings = new HashMap<>();
    }

    public void bookRoom(String resId, String guest, String type) throws Exception {

        if (inventory.getAvailability(type) <= 0) {
            throw new Exception("No rooms available for " + type);
        }

        String roomId = type.replace(" ", "").toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);

        Reservation r = new Reservation(resId, guest, type, roomId);

        bookings.put(resId, r);
        inventory.decrement(type);

        System.out.println("Booked: " + resId + " | Room ID: " + roomId);
    }

    public Reservation getReservation(String resId) {
        return bookings.get(resId);
    }

    public void removeReservation(String resId) {
        bookings.remove(resId);
    }
}

class CancellationService {

    private BookingService bookingService;
    private RoomInventory inventory;

    private Stack<String> rollbackStack;

    public CancellationService(BookingService bookingService, RoomInventory inventory) {
        this.bookingService = bookingService;
        this.inventory = inventory;
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String resId) throws Exception {

        Reservation r = bookingService.getReservation(resId);

        if (r == null) {
            throw new Exception("Reservation not found: " + resId);
        }

        rollbackStack.push(r.roomId);

        inventory.increment(r.roomType);

        bookingService.removeReservation(resId);

        System.out.println("Cancelled: " + resId + " | Rolled back Room ID: " + r.roomId);
    }
}