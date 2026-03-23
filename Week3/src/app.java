import java.util.*;

public class app {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        try {
            BookingService.bookRoom("Alice", "Single Room", inventory);

            BookingService.bookRoom("Bob", "Luxury Room", inventory);

            BookingService.bookRoom("Charlie", "Suite Room", inventory);
            BookingService.bookRoom("David", "Suite Room", inventory); // should fail

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nSystem is still running safely.");
    }
}

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
    }
}

class BookingService {

    public static void bookRoom(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.decrement(roomType);

        System.out.println("Booking Confirmed for " + guestName +
                " (" + roomType + ")");
    }
}