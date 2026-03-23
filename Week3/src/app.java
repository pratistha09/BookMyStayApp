import java.util.*;

public class app {

    public static void main(String[] args) {

        Reservation r1 = new Reservation("RES101", "Alice", "Single Room");
        Reservation r2 = new Reservation("RES102", "Bob", "Double Room");
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite Room");

        BookingHistory history = new BookingHistory();

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        history.displayHistory();

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllReservations());
    }
}


class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
        System.out.println("Added to history: " + r.getReservationId());
    }

    public void displayHistory() {

        System.out.println("\n===== BOOKING HISTORY =====");

        for (Reservation r : reservations) {
            System.out.println(
                    r.getReservationId() + " | " +
                            r.getGuestName() + " | " +
                            r.getRoomType()
            );
        }
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n===== BOOKING REPORT =====");

        System.out.println("Total Bookings: " + reservations.size());

        HashMap<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {

            String type = r.getRoomType();

            if (!roomCount.containsKey(type)) {
                roomCount.put(type, 1);
            } else {
                roomCount.put(type, roomCount.get(type) + 1);
            }
        }

        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}