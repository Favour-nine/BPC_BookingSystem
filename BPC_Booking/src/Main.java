import java.util.*;

public class Main {
    private static final BookingSystem bookingSystem = new BookingSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData(); // Preload data for testing

        while (true) {
            System.out.println("1. Register new patient");
            System.out.println("2. Book an appointment");
            System.out.println("3. Cancel an appointment");
            System.out.println("4. View available physiotherapists by expertise");
            System.out.println("5. View physiotherapist's available appointments");
            System.out.println("6. Search available appointments by treatment");
            System.out.println("7. Generate report");
            System.out.println("8. View your appointments");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> registerNewPatient();
                case 2 -> bookAppointment();
                case 3 -> cancelAppointment();
                case 4 -> searchPhysiotherapists();
                case 5 -> viewAppointmentsForPhysiotherapist();
                case 6 -> searchAppointmentsByTreatment(); // <-- new
                case 7 -> bookingSystem.generateReport();
                case 8 -> viewAppointmentsForPatient();
                case 9 -> {
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
        bookingSystem.addTreatment(new Treatment("Massage", "Relieves muscle tension"));
        bookingSystem.addTreatment(new Treatment("Rehabilitation", "Post-injury recovery"));
        bookingSystem.addTreatment(new Treatment("Acupuncture", "Stimulates nerves and reduces pain"));
        bookingSystem.addTreatment(new Treatment("Spine Mobilisation", "Mobilises spine and joints"));
        bookingSystem.addTreatment(new Treatment("Pool Therapy", "Water-based physical therapy"));
        bookingSystem.addTreatment(new Treatment("Neural Mobilisation", "Enhances nerve movement"));

        // Generate a timetable for all physiotherapists using these treatments
        bookingSystem.generateTreatmentTimetable(bookingSystem.getAllTreatments());

        System.out.println("Sample data initialized successfully!");
    }

    // Register a new patient and show their unique ID
    private static void registerNewPatient() {
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Patient newPatient = new Patient(fullName, phone, address);
        bookingSystem.addPatient(newPatient);

        System.out.println("\n✅ Registration successful!");
        System.out.println("Your Patient ID is: " + newPatient.getUniqueId());
        System.out.println("Please keep your Patient ID safe and do not share it with anyone.");
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

        // ✅ Get available treatments from the system
        List<Treatment> availableTreatments = bookingSystem.getAllTreatments();
        System.out.println("Available Treatments:");
        for (int i = 0; i < availableTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + availableTreatments.get(i).getTreatmentName());
        }
        System.out.print("Select treatment number: ");
        int treatmentIndex = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (treatmentIndex < 1 || treatmentIndex > availableTreatments.size()) {
            System.out.println("Invalid treatment selection.");
            return;
        }

        Treatment selectedTreatment = availableTreatments.get(treatmentIndex - 1);

        boolean success = bookingSystem.bookAppointment(patient, physio, week, date, time, selectedTreatment);

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

    // ✅ Display all appointments for a physiotherapist (by week)
    private static void viewAppointmentsForPhysiotherapist() {
        System.out.print("Enter physiotherapist's full name: ");
        String physioName = scanner.nextLine();
        Physiotherapist physio = bookingSystem.getPhysiotherapistByName(physioName);

        if (physio == null) {
            System.out.println("Physiotherapist not found.");
            return;
        }

        System.out.println("\nAvailable appointments for " + physio.getFullName() + ":");
        boolean found = false;

        for (int week = 1; week <= 4; week++) {
            List<Appointment> appointments = physio.getAvailableAppointments(week);
            List<Appointment> available = appointments.stream()
                    .filter(appointment -> appointment.getPatient() == null)
                    .toList();

            if (!available.isEmpty()) {
                found = true;
                System.out.println("\nWeek " + week + ":");
                for (Appointment appointment : available) {
                    System.out.println("- " + appointment.getDate() + " | " + appointment.getTime() + " | " +
                            appointment.getTreatment().getTreatmentName() + " | Status: Available");
                }
            }
        }

        if (!found) {
            System.out.println("No available appointments found.");
        }

    }

    // Search and display available appointments by treatment name
    private static void searchAppointmentsByTreatment() {
        System.out.print("Enter treatment name: ");
        String treatmentName = scanner.nextLine().trim().toLowerCase();

        List<Appointment> foundAppointments = new ArrayList<>();

        for (Physiotherapist physio : bookingSystem.getAllPhysiotherapists()) {
            for (int week = 1; week <= 4; week++) {
                List<Appointment> appointments = physio.getAvailableAppointments(week);
                for (Appointment appointment : appointments) {
                    if (appointment.getPatient() == null &&
                            appointment.getTreatment().getTreatmentName().toLowerCase().contains(treatmentName)) {
                        foundAppointments.add(appointment);
                        System.out.println("\nPhysiotherapist: " + physio.getFullName() +
                                "\nWeek: " + week +
                                "\nDate: " + appointment.getDate() +
                                "\nTime: " + appointment.getTime() +
                                "\nTreatment: " + appointment.getTreatment().getTreatmentName() +
                                "\nStatus: Available\n");
                    }
                }
            }
        }

        if (foundAppointments.isEmpty()) {
            System.out.println("No available appointments found for treatment: " + treatmentName);
            return;
        }

        System.out.print("Would you like to book one of these appointments? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (!response.equals("yes")) return;

        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equals(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Enter Physiotherapist Name: ");
        String physioName = scanner.nextLine();
        Physiotherapist physio = bookingSystem.getPhysiotherapistByName(physioName);
        if (physio == null) {
            System.out.println("Physiotherapist not found.");
            return;
        }

        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Enter Time (e.g., 10:00): ");
        String time = scanner.nextLine();

        // Guess the week from date — for simplicity, ask for it directly
        System.out.print("Enter Week Number (1–4): ");
        int week = Integer.parseInt(scanner.nextLine());

        // Find the treatment from available treatments again (or use fixed input)
        Treatment selectedTreatment = physio.getAvailableAppointments(week).stream()
                .filter(app -> app.getTime().equals(time) && app.getDate().toString().equals(date))
                .map(Appointment::getTreatment)
                .findFirst()
                .orElse(null);

        if (selectedTreatment == null) {
            System.out.println("No matching appointment found at that time.");
            return;
        }

        boolean success = bookingSystem.bookAppointment(patient, physio, week, date, time, selectedTreatment);

        if (success) {
            System.out.println("Appointment booked successfully!");
        } else {
            System.out.println("Failed to book. Appointment may have already been taken.");
        }
    }

    // View all appointments for a given patient by ID
    private static void viewAppointmentsForPatient() {
        System.out.print("Enter your Patient ID: ");
        String patientID = scanner.nextLine().trim();

        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equalsIgnoreCase(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found. Please check your ID.");
            return;
        }

        List<Appointment> appointments = patient.getAppointments();

        if (appointments.isEmpty()) {
            System.out.println("You have no appointments booked.");
        } else {
            System.out.println("Your Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("- Date: " + appointment.getDate() +
                        " | Time: " + appointment.getTime() +
                        " | Treatment: " + appointment.getTreatment().getTreatmentName() +
                        " | Physiotherapist: " + appointment.getPhysiotherapist().getFullName() +
                        " | Status: " + appointment.getStatus());
            }
        }
    }


}