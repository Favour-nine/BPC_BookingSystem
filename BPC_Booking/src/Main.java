import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final BookingSystem bookingSystem = new BookingSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData(); // Preload data for testing

        while (true) {
            System.out.println("\n=== Physiotherapy Booking System ===");
            System.out.println("[1] Patient Self-Service");
            System.out.println("[2] Booking Management");
            System.out.println("[3] Search & Discovery");
            System.out.println("[4] Reports");
            System.out.println("[0] Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> patientSelfServiceMenu();
                case 2 -> bookingManagementMenu();
                case 3 -> searchMenu();
                case 4 -> reportMenu();
                case 0 -> {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

    }

    //  Preload some sample physiotherapists, patients, and treatments
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

    //Patient Self service option
    private static void patientSelfServiceMenu() {
        while (true) {
            System.out.println("\n=== Patient Self-Service ===");
            System.out.println("[1] Register as a new patient");
            System.out.println("[2] View my appointments");
            System.out.println("[0] Back to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> registerNewPatient();
                    case 2 -> viewAppointmentsForPatient();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    //Booking management option
    private static void bookingManagementMenu() {
        while (true) {
            System.out.println("\n=== Booking Management ===");
            System.out.println("[1] Book an appointment");
            System.out.println("[2] Cancel an appointment");
            System.out.println("[3] Check in to an appointment");
            System.out.println("[0] Back to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> bookAppointment();
                    case 2 -> cancelAppointment();
                    case 3 -> checkInAppointment();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void searchMenu() {
        while (true) {
            System.out.println("\n=== Search & Discovery ===");
            System.out.println("[1] Find physiotherapists by expertise");
            System.out.println("[2] View physiotherapist availability");
            System.out.println("[3] Search appointments by treatment");
            System.out.println("[0] Back to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> searchPhysiotherapists();
                    case 2 -> viewAppointmentsForPhysiotherapist();
                    case 3 -> searchAppointmentsByTreatment();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void reportMenu() {
        while (true) {
            System.out.println("\n=== Reports ===");
            System.out.println("[1] Generate appointment report");
            System.out.println("[0] Back to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> bookingSystem.generateReport();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }




    // Register a new patient and show their unique ID
// Register a new patient and show their unique ID with input validation
    private static void registerNewPatient() {
        String fullName;
        // Validate full name (letters only and at least two words)
        while (true) {
            System.out.print("Enter full name: ");
            fullName = scanner.nextLine().trim();
            // Regex check: only letters and spaces, and at least two words
            if (!fullName.matches("^[A-Za-z]+(?:\\s+[A-Za-z]+)+$")) {
                System.out.println(" Full name must contain only letters and include at least a first and last name. Please try again.");
            } else {
                break;
            }
        }

        String phone;
        // Validate phone number (numeric and >= 10 digits)
        while (true) {
            System.out.print("Enter phone number: ");
            phone = scanner.nextLine().trim();
            // Regex check: only digits and at least 10 of them
            if (!phone.matches("^\\d{10,}$")) {
                System.out.println(" Phone number must be numeric and at least 10 digits long. Please try again.");
            } else {
                break;
            }
        }

        String address;
        // Validate address (non-empty and sufficiently long)
        while (true) {
            System.out.print("Enter address: ");
            address = scanner.nextLine().trim();
            if (address.length() < 4) {  // "more than a few characters" – here we require at least 4
                System.out.println(" Address must be longer than a few characters. Please try again.");
            } else {
                break;
            }
        }

        // All inputs are valid at this point, proceed to create the patient
        Patient newPatient = new Patient(fullName, phone, address);
        bookingSystem.addPatient(newPatient);

        System.out.println("\n Registration successful!");
        System.out.println("Your Patient ID is: " + newPatient.getUniqueId());
        System.out.println("Please keep your Patient ID safe and do not share it with anyone.");
    }

    // Book an appointment
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

        int week;
        try {
            System.out.print("Enter Week Number (1-4): ");
            week = Integer.parseInt(scanner.nextLine().trim());
            if (week < 1 || week > 4) {
                System.out.println("Invalid week number. Please enter a number between 1 and 4.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        System.out.print("Enter Date (e.g., 2025-04-15): ");
        String date = scanner.nextLine();

        System.out.print("Enter Time (e.g., 10:00 AM): ");
        String time = scanner.nextLine();

        // Get available treatments from the system
        List<Treatment> availableTreatments = bookingSystem.getAllTreatments();
        System.out.println("Available Treatments:");
        for (int i = 0; i < availableTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + availableTreatments.get(i).getTreatmentName());
        }
        int treatmentIndex;
        try {
            System.out.print("Select treatment number: ");
            treatmentIndex = Integer.parseInt(scanner.nextLine().trim());
            if (treatmentIndex < 1 || treatmentIndex > availableTreatments.size()) {
                System.out.println("Invalid treatment selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid treatment number.");
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

    // Cancel an appointment
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

    //  Search physiotherapists by expertise
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

    // Display all appointments for a physiotherapist (by week)
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

        List<Appointment> matchedAppointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Collect all matching, available appointments
        for (Physiotherapist physio : bookingSystem.getAllPhysiotherapists()) {
            for (int week = 1; week <= 4; week++) {
                for (Appointment app : physio.getAvailableAppointments(week)) {
                    if (app.getPatient() == null &&
                            app.getTreatment().getTreatmentName().toLowerCase().contains(treatmentName)) {

                        matchedAppointments.add(app);
                    }
                }
            }
        }

        if (matchedAppointments.isEmpty()) {
            System.out.println("No available appointments found for treatment: " + treatmentName);
            return;
        }

        // Show all matched appointments
        System.out.println("\nAvailable Appointments:");
        for (int i = 0; i < matchedAppointments.size(); i++) {
            Appointment a = matchedAppointments.get(i);
            System.out.println((i + 1) + ". " + a.getPhysiotherapist().getFullName()
                    + " | " + sdf.format(a.getDate()) + " | " + a.getTime()
                    + " | " + a.getTreatment().getTreatmentName());
        }

        // Let user select one
        int selection;
        try {
            System.out.print("\nEnter the number of the appointment you want to book: ");
            selection = Integer.parseInt(scanner.nextLine().trim());
            if (selection < 1 || selection > matchedAppointments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        Appointment chosen = matchedAppointments.get(selection - 1);
        Physiotherapist physio = chosen.getPhysiotherapist();
        Treatment treatment = chosen.getTreatment();
        Date date = chosen.getDate();
        String time = chosen.getTime();

        // Ask for patient ID
        System.out.print("Enter your Patient ID: ");
        String patientID = scanner.nextLine().trim();
        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equalsIgnoreCase(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        // Derive week from the chosen appointment
        int week = bookingSystem.getWeekFromDate(date);
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
        boolean success = bookingSystem.bookAppointment(patient, physio, week, sdfOut.format(date), time, treatment);

        if (success) {
            System.out.println(" Appointment booked successfully!");
        } else {
            System.out.println(" Failed to book appointment. It may already be taken.");
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
    // Allow a patient to check in to an upcoming appointment
    private static void checkInAppointment() {
        System.out.print("Enter your Patient ID: ");
        String patientID = scanner.nextLine().trim();

        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equalsIgnoreCase(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        List<Appointment> appointments = patient.getAppointments();

        List<Appointment> upcoming = appointments.stream()
                .filter(app -> !"Attended".equalsIgnoreCase(app.getStatus()))
                .toList();

        if (upcoming.isEmpty()) {
            System.out.println("You have no upcoming appointments to check in.");
            return;
        }

        System.out.println("Upcoming Appointments:");
        for (int i = 0; i < upcoming.size(); i++) {
            Appointment app = upcoming.get(i);
            System.out.println((i + 1) + ". " + app.getDate() + " | " + app.getTime() + " | " +
                    app.getTreatment().getTreatmentName() + " | Physiotherapist: " +
                    app.getPhysiotherapist().getFullName() + " | Status: " + app.getStatus());
        }

        System.out.print("Select appointment number to check in: ");
        int choice;
        try {
            System.out.print("Select appointment number to check in: ");
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > upcoming.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }


        Appointment selected = upcoming.get(choice - 1);
        selected.setStatus("Attended");

        System.out.println("You are now checked in for your appointment.");
    }



}