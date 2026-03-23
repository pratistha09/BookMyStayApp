import java.io.*;
import java.util.*;

public class app {

    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        SystemState state = service.loadState();

        if (state == null) {
            System.out.println("No previous data found. Starting fresh.");

            state = new SystemState();

            state.inventory.put("Single Room", 2);
            state.inventory.put("Suite Room", 1);

            state.bookings.put("RES101", "Single Room");
            state.bookings.put("RES102", "Suite Room");

        } else {
            System.out.println("System recovered successfully!");
        }

        System.out.println("\nCurrent Inventory: " + state.inventory);
        System.out.println("Current Bookings: " + state.bookings);

        service.saveState(state);

        System.out.println("\nState saved. You can restart program to see recovery.");
    }
}

class SystemState implements Serializable {

    HashMap<String, Integer> inventory;
    HashMap<String, String> bookings;

    public SystemState() {
        inventory = new HashMap<>();
        bookings = new HashMap<>();
    }
}

class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    public void saveState(SystemState state) {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nState persisted to file.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public SystemState loadState() {

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            return null;

        } catch (Exception e) {
            System.out.println("Recovery failed. Starting fresh.");
            return null;
        }
    }
}