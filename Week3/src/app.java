import java.util.*;

public class app {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingProcessor processor = new BookingProcessor(inventory);

        processor.addRequest(new Reservation("Alice", "Single Room"));
        processor.addRequest(new Reservation("Bob", "Single Room"));
        processor.addRequest(new Reservation("Charlie", "Single Room"));

        Thread t1 = new Thread(processor);
        Thread t2 = new Thread(processor);

        t1.start();
        t2.start();
    }
}

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2); // limited rooms
    }

    public synchronized boolean allocateRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }

        return false;
    }
}

class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    public BookingProcessor(RoomInventory inventory) {
        this.inventory = inventory;
        queue = new LinkedList<>();
    }

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public void run() {

        while (true) {

            Reservation r;

            synchronized (this) {
                if (queue.isEmpty()) {
                    return;
                }
                r = queue.poll();
            }

            boolean success = inventory.allocateRoom(r.roomType);

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " → Booking Confirmed for " + r.guestName);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " → Booking FAILED for " + r.guestName);
            }
        }
    }
}