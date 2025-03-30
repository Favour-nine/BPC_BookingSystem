import java.util.*;

public class Main {
    private static final BookingSystem bookingSystem = new BookingSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData(); // Preload data for testing

        while (true) {
            System.out.println("\n=== Physiotherapy Booking System ===");
            System.out.println("1. Book an appointment");
            System.out.println("2. Cancel an appointment");
            System.out.println("3. View available physiotherapists by expertise");
            System.out.println("4. Generate report");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> bookAppointment();
                case 2 -> cancelAppointment();
                case 3 -> searchPhysiotherapists();
                case 4 -> bookingSystem.generateReport();
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ✅ Preload some sample physiotherapists, patients, and treatments
    private static void initializeSampleData() {
        // Sample Physiotherapists
        Physiotherapist physio1 = new Physiotherapist("Dr. James Smith", "123-456-7890", "123 Main St", Arrays.asList("Rehabilitation", "Osteopathy"));
        Physiotherapist physio2 = new Physiotherapist("Dr. Sarah Johnson", "987-654-3210", "456 Elm St", Arrays.asList("Physiotherapy", "Massage"));

        bookingSystem.addPhysiotherapist(physio1);
        bookingSystem.addPhysiotherapist(physio2);

        // Sample Patients
        Patient patient1 = new Patient("Alice Brown", "111-222-3333", "789 Maple St");
        Patient patient2 = new Patient("Bob White", "444-555-6666", "321 Oak St");

        bookingSystem.addPatient(patient1);
        bookingSystem.addPatient(patient2);

        // Sample Treatments
        Treatment treatment1 = new Treatment("Massage", "Relieves muscle tension");
        Treatment treatment2 = new Treatment("Rehabilitation", "Post-injury recovery");

        System.out.println("Sample data initialized successfully!");
    }

    // ✅ Book an appointment
    private static void bookAppointment() {
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equals(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter Physiotherapist Name: ");
        String physioName = scanner.nextLine();
        Physiotherapist physio = bookingSystem.getPhysiotherapistByName(physioName);

        if (physio == null) {
            System.out.println("Physiotherapist not found!");
            return;
        }

        System.out.print("Enter Week Number (1-4): ");
        int week = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Date (e.g., 2025-04-15): ");
        String date = scanner.nextLine();

        System.out.print("Enter Time (e.g., 10:00 AM): ");
        String time = scanner.nextLine();

        Treatment treatment = new Treatment("Massage", "Relieves muscle tension"); // Placeholder

        boolean success = bookingSystem.bookAppointment(patient, physio, week, date, time, treatment);

        if (success) {
            System.out.println("Appointment booked successfully!");
        } else {
            System.out.println("Failed to book appointment. Slot may be taken.");
        }
    }

    // ✅ Cancel an appointment
    private static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        String appointmentID = scanner.nextLine();

        boolean success = bookingSystem.cancelAppointment(appointmentID);

        if (success) {
            System.out.println("Appointment cancelled successfully!");
        } else {
            System.out.println("Appointment not found!");
        }
    }

    // ✅ Search physiotherapists by expertise
    private static void searchPhysiotherapists() {
        System.out.print("Enter area of expertise: ");
        String expertise = scanner.nextLine();

        List<Physiotherapist> results = bookingSystem.getPhysiotherapistsByExpertise(expertise);

        if (results.isEmpty()) {
            System.out.println("No physiotherapists found for that expertise.");
        } else {
            System.out.println("Available Physiotherapists:");
            for (Physiotherapist physio : results) {
                System.out.println("- " + physio.getFullName());
            }
        }
    }
}