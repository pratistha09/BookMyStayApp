import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        int count = inventory.get(roomType);
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        bookingQueue = new LinkedList<>();
        allocatedRooms = new HashMap<>();
    }

    public void addBookingRequest(Reservation reservation) {
        bookingQueue.add(reservation);
    }

    public void processBookings() {

        System.out.println("===== PROCESSING BOOKINGS =====\n");

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();
            String roomType = request.getRoomType();

            System.out.println("Processing request for " + request.getGuestName());

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = roomType.replace(" ", "").toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());

                allocatedRooms.get(roomType).add(roomId);

                inventory.decrementAvailability(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("Reservation Failed - No rooms available for " + roomType);
                System.out.println();
            }
        }
    }

    public void displayAllocatedRooms() {

        System.out.println("\n===== ALLOCATED ROOMS =====");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

public class app {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService(inventory);

        bookingService.addBookingRequest(new Reservation("Alice", "Single Room"));
        bookingService.addBookingRequest(new Reservation("Bob", "Double Room"));
        bookingService.addBookingRequest(new Reservation("Charlie", "Single Room"));
        bookingService.addBookingRequest(new Reservation("David", "Suite Room"));

        bookingService.processBookings();

        bookingService.displayAllocatedRooms();
        inventory.displayInventory();
    }
}