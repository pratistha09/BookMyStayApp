import java.util.*;

public class app {

    public static void main(String[] args) {

        Reservation r1 = new Reservation("RES101", "Alice", "Single Room");

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(r1.getReservationId(), new AddOnService("Breakfast", 20));
        manager.addService(r1.getReservationId(), new AddOnService("Airport Pickup", 50));
        manager.addService(r1.getReservationId(), new AddOnService("Extra Bed", 30));

        manager.displayServices(r1.getReservationId());

        double totalCost = manager.calculateTotalCost(r1.getReservationId());

        System.out.println("\nTotal Add-On Cost: $" + totalCost);
    }
}

class Reservation {

    private final String reservationId;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
    }

    public String getReservationId() {
        return reservationId;
    }
}

class AddOnService {

    private final String serviceName;
    private final double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {

    private final HashMap<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {

        if (!serviceMap.containsKey(reservationId)) {
            serviceMap.put(reservationId, new ArrayList<>());
        }

        serviceMap.get(reservationId).add(service);

        System.out.println("Added: " + service.getServiceName());
    }

    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list == null || list.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : list) {
            System.out.println(s.getServiceName() + " ($" + s.getCost() + ")");
        }
    }

    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list != null) {
            for (AddOnService s : list) {
                total += s.getCost();
            }
        }

        return total;
    }
}