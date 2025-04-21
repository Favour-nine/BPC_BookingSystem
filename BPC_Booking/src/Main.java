import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final BookingSystem bookingSystem = new BookingSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
// Load existing data or initialize fresh sample data
        @SuppressWarnings("unchecked")
        List<Physiotherapist> loadedPhysios = (List<Physiotherapist>) bookingSystem.loadData("physiotherapists.dat");
        @SuppressWarnings("unchecked")
        List<Patient> loadedPatients = (List<Patient>) bookingSystem.loadData("patients.dat");
        @SuppressWarnings("unchecked")
        List<Treatment> loadedTreatments = (List<Treatment>) bookingSystem.loadData("treatments.dat");
        @SuppressWarnings("unchecked")
        List<Appointment> loadedAppointments = (List<Appointment>) bookingSystem.loadData("appointments.dat");


        if (loadedPhysios != null) {
            for (Physiotherapist physio : loadedPhysios) {
                bookingSystem.addPhysiotherapist(physio);
            }
        }

        if (loadedPatients != null) {
            for (Patient p : loadedPatients) {
                bookingSystem.addPatient(p);
            }
        }

        if (loadedTreatments != null) {
            for (Treatment t : loadedTreatments) {
                bookingSystem.addTreatment(t);
            }
        }

        if (loadedAppointments != null) {
            for (Appointment a : loadedAppointments) {
                a.getPhysiotherapist().addAppointment(bookingSystem.getWeekFromDate(a.getDate()), a);
                a.getPatient().bookAppointment(a);
            }
        }

        boolean anyDataMissing = loadedAppointments == null || loadedPhysios == null || loadedPatients == null || loadedTreatments == null;

        if (anyDataMissing) {
            initializeSampleData();
        } else {
            boolean timetableRegenerated = false;

            for (Physiotherapist physio : bookingSystem.getAllPhysiotherapists()) {
                boolean hasSlots = false;
                for (int week = 1; week <= 4; week++) {
                    if (!physio.getAvailableAppointments(week).isEmpty()) {
                        hasSlots = true;
                        break;
                    }
                }
                if (!hasSlots) {
                    physio.generateWeeklySchedule();
                    timetableRegenerated = true;
                }
            }

            if (timetableRegenerated) {
                bookingSystem.generateTreatmentTimetable(bookingSystem.getAllTreatments());
            }
        }




        while (true) {
            System.out.println("""
                    
                    
                    
                    █████████     █████████     █████████
                    ██      ██    ██      ██   ██       \s
                    ██      ██    ██      ██  ██        \s
                    █████████     █████████  ██         \s
                    ██      ██    ██          ██        \s
                    ██      ██    ██           ██       \s
                    █████████     ██            █████████
                    
                    ========================================
                      WELCOME TO BPC PHYSIOTHERAPY SYSTEM
                    ========================================
                    
                    """);
            System.out.println("[1] Patient Self-Service");
            System.out.println("[2] Booking Management");
            System.out.println("[3] Search & Discovery");
            System.out.println("[4] Generate appointment report");
            System.out.println("[5] Export report to file");
            System.out.println("[6] Admin Panel");
            System.out.println("[0] Exit");
            System.out.print("\nEnter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> patientSelfServiceMenu();
                case 2 -> bookingManagementMenu();
                case 3 -> searchMenu();
                case 4 -> reportMenu();
                case 5 -> {
                    System.out.print("\nEnter filename to save report (e.g., report.txt): ");
                    String filename = scanner.nextLine();
                    bookingSystem.exportReportToFile(filename);
                }
                case 6 -> adminMenu();
                case 0 -> {
                    bookingSystem.saveData(bookingSystem.getPatients(), "patients.dat");
                    bookingSystem.saveData(bookingSystem.getAppointments(), "appointments.dat");
                    bookingSystem.saveData(bookingSystem.getAllPhysiotherapists(), "physiotherapists.dat");
                    bookingSystem.saveData(bookingSystem.getAllTreatments(), "treatments.dat");
                    System.out.println("Data saved. Exiting... Goodbye!");
                    System.exit(0);

                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

    }

    //  Preload some sample physiotherapists, patients, and treatments
    private static void initializeSampleData() {
        // Testing Physiotherapists
        Physiotherapist physio1 = new Physiotherapist("Dr. James Smith", "123-456-7890", "123 Main St", Arrays.asList("Rehabilitation", "Osteopathy"));
        Physiotherapist physio2 = new Physiotherapist("Dr. Sarah Johnson", "987-654-3210", "456 Elm St", Arrays.asList("Physiotherapy", "Massage"));
        Physiotherapist physio3 = new Physiotherapist("Dr. Emily Clarke", "555-111-2222", "789 Birch St", Arrays.asList("Acupuncture", "Neural Mobilisation"));
        Physiotherapist physio4 = new Physiotherapist("Dr. Michael Lee", "555-333-4444", "321 Pine St", Arrays.asList("Spine Mobilisation", "Massage"));
        Physiotherapist physio5 = new Physiotherapist("Dr. Priya Patel", "555-777-8888", "654 Willow St", Arrays.asList("Pool Therapy", "Rehabilitation"));

        bookingSystem.addPhysiotherapist(physio1);
        bookingSystem.addPhysiotherapist(physio2);
        bookingSystem.addPhysiotherapist(physio3);
        bookingSystem.addPhysiotherapist(physio4);
        bookingSystem.addPhysiotherapist(physio5);

        // Sample Patients
        //Patient patient1 = new Patient("Alice Brown", "111-222-3333", "789 Maple St");
        //Patient patient2 = new Patient("Bob White", "444-555-6666", "321 Oak St");

        //bookingSystem.addPatient(patient1);
        //bookingSystem.addPatient(patient2);

        // Testing Treatments
        bookingSystem.addTreatment(new Treatment("Massage", "Relieves muscle tension", "Massage"));
        bookingSystem.addTreatment(new Treatment("Rehabilitation", "Post-injury recovery", "Rehabilitation"));
        bookingSystem.addTreatment(new Treatment("Acupuncture", "Stimulates nerves and reduces pain", "Acupuncture"));
        bookingSystem.addTreatment(new Treatment("Spine Mobilisation", "Mobilises spine and joints", "Spine Mobilisation"));
        bookingSystem.addTreatment(new Treatment("Pool Therapy", "Water-based physical therapy", "Pool Therapy"));
        bookingSystem.addTreatment(new Treatment("Neural Mobilisation", "Enhances nerve movement", "Neural Mobilisation"));

        // Generate a timetable for all physiotherapists using these treatments
        bookingSystem.generateTreatmentTimetable(bookingSystem.getAllTreatments());

        System.out.println("Sample data initialized successfully!");
    }

    //Patient Self service option
    private static void patientSelfServiceMenu() {
        while (true) {
            System.out.println("════════════════════════════════");
            System.out.println("""
                    === Patient Self-Service ===
                    """);
            System.out.println("[1] Register as a new patient");
            System.out.println("[2] View my appointments");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

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
            System.out.println("════════════════════════════════");
            System.out.println("\n=== Booking Management ===");
            System.out.println("\n[1] Book an appointment");
            System.out.println("[2] Cancel an appointment");
            System.out.println("[3] Check in to an appointment");
            System.out.println("[4] Reschedule an appointment");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> bookAppointment();
                    case 2 -> cancelAppointment();
                    case 3 -> checkInAppointment();
                    case 4 -> rescheduleAppointment();
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
            System.out.println("\n════════════════════════════════");
            System.out.println("\n=== Search & Discovery ===");
            System.out.println("\n[1] View all physiotherapist and their expertise");
            System.out.println("[2] View all treatments offered by the clinic");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> searchPhysiotherapists();
                    case 2 -> viewAllTreatments();
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
            System.out.println("\n════════════════════════════════");
            System.out.println("\n=== Reports ===");
            System.out.println("[1] Generate appointment report");
            System.out.println("[2] Generate analytics report");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> bookingSystem.generateReport();
                    case 2 -> bookingSystem.generateAnalyticsReport();
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
            System.out.println("\n════════════════════════════════");
            System.out.print("\nEnter full name: ");
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
            System.out.print("\nEnter phone number: ");
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
            System.out.print("\nEnter address: ");
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
        System.out.println("\nYour Patient ID is: " + newPatient.getUniqueId());
        System.out.println("Please keep your Patient ID safe and do not share it with anyone.");
    }

    // Book an appointment
    private static void bookAppointment() {
        System.out.println("\n════════════════════════════════");
        System.out.println("Book an Appointment");
        System.out.println("════════════════════════════════");

        // Step 1: Get patient by ID
        System.out.print("\nEnter your Patient ID: ");
        String patientID = scanner.nextLine().trim();
        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equalsIgnoreCase(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        // Step 2: Ask for treatment type
        System.out.print("Enter treatment name you're interested in: ");
        String treatmentName = scanner.nextLine().trim().toLowerCase();

        // Step 3: Collect and deduplicate available slots
        List<Appointment> matchingAppointments = new ArrayList<>();
        Set<String> seenSlots = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Physiotherapist physio : bookingSystem.getAllPhysiotherapists()) {
            for (Appointment app : physio.getAllAppointments()) {
                if (app.getTreatment().getTreatmentName().toLowerCase().contains(treatmentName)) {
                    String slotKey = physio.getFullName() + "|" + sdf.format(app.getDate()) + "|" + app.getTime();
                    if (!seenSlots.contains(slotKey)) {
                        seenSlots.add(slotKey);
                        matchingAppointments.add(app);
                    } else {
                        // Replace existing with available version if found
                        for (int i = 0; i < matchingAppointments.size(); i++) {
                            Appointment existing = matchingAppointments.get(i);
                            if (existing.getPhysiotherapist().getFullName().equals(app.getPhysiotherapist().getFullName())
                                    && sdf.format(existing.getDate()).equals(sdf.format(app.getDate()))
                                    && existing.getTime().equals(app.getTime())) {

                                if (existing.getPatient() != null && app.getPatient() == null) {
                                    matchingAppointments.set(i, app); // Replace with available
                                }
                            }
                        }
                    }

                }
            }
        }

        if (matchingAppointments.isEmpty()) {
            System.out.println("No appointments found for treatment: " + treatmentName);
            return;
        }

        // Step 4: Display matching appointments
        System.out.println("\nMatching Appointments:");
        for (int i = 0; i < matchingAppointments.size(); i++) {
            Appointment a = matchingAppointments.get(i);
            String status = a.getStatus();
            System.out.printf("%d. %s | %s | %s | %s | Status: %s%n",
                    i + 1,
                    a.getPhysiotherapist().getFullName(),
                    sdf.format(a.getDate()),
                    a.getTime(),
                    a.getTreatment().getTreatmentName(),
                    status);
        }

        // Step 5: Let user choose one
        System.out.print("\nSelect an available appointment to book (enter number): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > matchingAppointments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        Appointment selected = matchingAppointments.get(choice - 1);
        if (selected.getPatient() != null) {
            System.out.println("Slot already taken.\nFailed to book appointment. It may no longer be available.");
            return;
        }

        // Step 6: Proceed to book via BookingSystem
        Physiotherapist physio = selected.getPhysiotherapist();
        Treatment treatment = selected.getTreatment();
        Date date = selected.getDate();
        String time = selected.getTime();
        int week = bookingSystem.getWeekFromDate(date);
        String formattedDate = sdf.format(date);

        Appointment booked = bookingSystem.finalizeBooking(selected, patient);

        if (booked != null) {
            System.out.println("\nAppointment booked successfully!");
            System.out.println("Appointment ID: " + booked.getAppointmentID());
            System.out.println("Keep this ID safe.");
        } else {
            System.out.println("Failed to book appointment. It may no longer be available.");
        }
    }




    // Cancel an appointment
    private static void cancelAppointment() {
        System.out.println("\n════════════════════════════════");
        System.out.print("\nEnter Appointment ID to cancel: ");
        String appointmentID = scanner.nextLine();

        boolean success = bookingSystem.cancelAppointment(appointmentID);

        if (success) {
            System.out.println("\nAppointment cancelled successfully!");
        } else {
            System.out.println("\nAppointment not found!");
        }
    }

    //  Search physiotherapists by expertise
    private static void searchPhysiotherapists() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("All Available Physiotherapists & Expertise");
        System.out.println("══════════════════════════════════════════");

        List<Physiotherapist> physios = bookingSystem.getAllPhysiotherapists();

        if (physios.isEmpty()) {
            System.out.println("No physiotherapists found.");
            return;
        }

        for (int i = 0; i < physios.size(); i++) {
            Physiotherapist physio = physios.get(i);
            System.out.println((i + 1) + ". " + physio.getFullName());
            System.out.println("   Expertise: " + physio.getExpertise());
            System.out.println();
        }

        System.out.println("══════════════════════════════════════════");
        System.out.print("Would you like to book an appointment with one of them? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            bookAppointment();
        } else {
            System.out.println("Returning to main menu...");
        }
    }


    private static void viewAllTreatments() {
        System.out.println("\n════════════════════════════════");
        System.out.println("Treatments Offered by the Clinic:\n");

        List<Treatment> treatments = bookingSystem.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments available.");
            return;
        }

        for (Treatment t : treatments) {
            System.out.println("- " + t.getTreatmentName() + " : " + t.getDescription());
        }
    }





    // View all appointments for a given patient by ID
    private static void viewAppointmentsForPatient() {
        System.out.print("\nEnter your Patient ID: ");
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
            System.out.println("\nYour Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("- Date: " + appointment.getDate() +
                        " | Time: " + appointment.getTime() +
                        " | Treatment: " + appointment.getTreatment().getTreatmentName() +
                        " | Physiotherapist: " + appointment.getPhysiotherapist().getFullName() +
                        " | Status: " + appointment.getStatus() +
                        " | Appointment ID: "+appointment.getAppointmentID() );
            }
        }
    }
    // Allow a patient to check in to an upcoming appointment
    private static void checkInAppointment() {
        System.out.print("\nEnter your Patient ID: ");
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

    private static void  rescheduleAppointment () {
        System.out.print("\nEnter your Patient ID: ");
        String patientID = scanner.nextLine().trim();

        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equalsIgnoreCase(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        List<Appointment> appointments = patient.getAppointments().stream()
                .filter(app -> !"Attended".equalsIgnoreCase(app.getStatus()) &&
                        !"Cancelled".equalsIgnoreCase(app.getStatus()))
                .toList();

        if (appointments.isEmpty()) {
            System.out.println("You have no appointments to reschedule.");
            return;
        }

        System.out.println("Appointments available to reschedule:");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment app = appointments.get(i);
            System.out.println((i + 1) + ". " + app.getDate() + " | " + app.getTime()
                    + " | " + app.getTreatment().getTreatmentName()
                    + " | Physio: " + app.getPhysiotherapist().getFullName()
                    + " | ID: " + app.getAppointmentID());
        }

        System.out.print("\nSelect appointment to reschedule: ");
        int index;
        try {
            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= appointments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Appointment oldAppointment = appointments.get(index);
        Physiotherapist physio = oldAppointment.getPhysiotherapist();
        Treatment treatment = oldAppointment.getTreatment();

        // Collect new date/time
        System.out.print("Enter new date (yyyy-MM-dd): ");
        String newDateStr = scanner.nextLine().trim();
        System.out.print("Enter new time (e.g., 10:00): ");
        String newTime = scanner.nextLine().trim();

        Date newDate;
        try {
            newDate = new SimpleDateFormat("yyyy-MM-dd").parse(newDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        int newWeek = bookingSystem.getWeekFromDate(newDate);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(newDate);

        Appointment newAppointment = null;

        for (Appointment slot : physio.getAllAppointments()) {
            boolean sameTime = slot.getTime().equalsIgnoreCase(newTime);
            boolean sameDate = new SimpleDateFormat("yyyy-MM-dd").format(slot.getDate()).equals(formattedDate);
            if (sameTime && sameDate && slot.getPatient() == null) {
                newAppointment = bookingSystem.finalizeBooking(slot, patient);
                break;
            }
        }
;
        if (newAppointment != null) {
            oldAppointment.setStatus("Cancelled");
            patient.cancelAppointment(oldAppointment.getAppointmentID());

            System.out.println("Appointment rescheduled successfully!");
            System.out.println("New Appointment ID: " + newAppointment.getAppointmentID());
        } else {
            System.out.println("Failed to reschedule. New slot might be taken.");
        }

    }


    private static void adminMenu() {
        System.out.print("\nEnter admin PIN: ");
        String pin = scanner.nextLine().trim();
        if (!pin.equals("0000")) {
            System.out.println("Access denied.");
            return;
        }

        while (true) {
            System.out.println("════════════════════════════════");
            System.out.println("\n=== Admin Panel ===");
            System.out.println("\n[1] View all patients");
            System.out.println("[2] View all physiotherapists");
            System.out.println("[3] Add new physiotherapist");
            System.out.println("[4] Remove a physiotherapist");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> viewAllPatients();
                    case 2 -> viewAllPhysiotherapists();
                    case 3 -> addNewPhysiotherapist();
                    case 4 -> removePhysiotherapist();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = bookingSystem.getPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (Patient p : patients) {
                System.out.println("- " + p.getFullName() + " | ID: " + p.getUniqueId() + " | Phone: " + p.getPhoneNumber());
            }
        }
    }

    private static void viewAllPhysiotherapists() {
        List<Physiotherapist> physios = bookingSystem.getAllPhysiotherapists();
        if (physios.isEmpty()) {
            System.out.println("No physiotherapists found.");
        } else {
            for (Physiotherapist p : physios) {
                System.out.println("- " + p.getFullName() + " | Expertise: " + p.getExpertise());
            }
        }
    }

    private static void addNewPhysiotherapist() {
        System.out.println("\n════════════════════════════════");
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        System.out.print("\nEnter expertise areas (comma-separated): ");
        String[] expertise = scanner.nextLine().split(",");
        List<String> expertiseList = new ArrayList<>();
        for (String exp : expertise) {
            expertiseList.add(exp.trim());
        }

        Physiotherapist newPhysio = new Physiotherapist(name, phone, address, expertiseList);
        bookingSystem.addPhysiotherapist(newPhysio);

        System.out.println("New physiotherapist added successfully!");
    }

    private static void removePhysiotherapist() {
        System.out.print("\n1Enter full name of physiotherapist to remove: ");
        String name = scanner.nextLine().trim();
        Physiotherapist physio = bookingSystem.getPhysiotherapistByName(name);

        if (physio != null) {
            bookingSystem.getAllPhysiotherapists().remove(physio); // removes from the internal list
            System.out.println("Physiotherapist removed.");
        } else {
            System.out.println("Physiotherapist not found.");
        }
    }


}