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

        if (loadedAppointments == null || loadedPhysios == null || loadedPatients == null || loadedTreatments == null ) {
            initializeSampleData(); // only if no saved data
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
        // Sample Physiotherapists
        //Physiotherapist physio1 = new Physiotherapist("Dr. James Smith", "123-456-7890", "123 Main St", Arrays.asList("Rehabilitation", "Osteopathy"));
        //Physiotherapist physio2 = new Physiotherapist("Dr. Sarah Johnson", "987-654-3210", "456 Elm St", Arrays.asList("Physiotherapy", "Massage"));

        //bookingSystem.addPhysiotherapist(physio1);
        //bookingSystem.addPhysiotherapist(physio2);

        // Sample Patients
        //Patient patient1 = new Patient("Alice Brown", "111-222-3333", "789 Maple St");
        //Patient patient2 = new Patient("Bob White", "444-555-6666", "321 Oak St");

        //bookingSystem.addPatient(patient1);
        //bookingSystem.addPatient(patient2);

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
            System.out.println("\n[1] Find physiotherapists by expertise");
            System.out.println("[2] View physiotherapist availability");
            System.out.println("[3] Search appointments by treatment");
            System.out.println("[4] View all treatments offered by the clinic");
            System.out.println("[5] View all physiotherapists and their expertise");
            System.out.println("[0] Back to main menu");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> searchPhysiotherapists();
                    case 2 -> viewAppointmentsForPhysiotherapist();
                    case 3 -> searchAppointmentsByTreatment();
                    case 4 -> viewAllTreatments();
                    case 5 -> viewAllPhysiosWithExpertise();
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
        System.out.print("\nEnter Patient ID: ");
        String patientID = scanner.nextLine();
        Patient patient = bookingSystem.getPatients().stream()
                .filter(p -> p.getUniqueId().equals(patientID))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }



        // Show physiotherapists list
        List<Physiotherapist> physios = bookingSystem.getAllPhysiotherapists();
        if (physios.isEmpty()) {
            System.out.println("No physiotherapists available.");
            return;
        }

        System.out.println("\nSelect a Physiotherapist:");
        for (int i = 0; i < physios.size(); i++) {
            System.out.println((i + 1) + ". " + physios.get(i).getFullName());
        }

        Physiotherapist physio;
        try {
            System.out.print("\nEnter physiotherapist number: ");
            int physioIndex = Integer.parseInt(scanner.nextLine().trim());
            if (physioIndex < 1 || physioIndex > physios.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            physio = physios.get(physioIndex - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            System.out.print("\nEnter Date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine().trim();
            date = sdf.parse(dateInput);

            Calendar minDate = Calendar.getInstance();
            minDate.set(2025, Calendar.APRIL, 28);

            Calendar maxDate = Calendar.getInstance();
            maxDate.set(2025, Calendar.MAY, 25);

            if (date.before(minDate.getTime()) || date.after(maxDate.getTime())) {
                System.out.println("\nAppointments can only be booked between 28 April 2025 and 25 May 2025.");
                return;
            }

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        int week = bookingSystem.getWeekFromDate(date);

        System.out.print("\nEnter Time (e.g., 10:00 AM): ");
        String time = scanner.nextLine().trim();

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date parsedTime = timeFormat.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedTime);
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            if (hour < 9 || hour > 15) {
                System.out.println("\nAppointments can only be scheduled between 9:00 AM and 3:00 PM.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("\nInvalid time format. Please enter a valid time like '10:00 AM'.");
            return;
        }


        // Show treatments based on physiotherapist's expertise
        List<Treatment> availableTreatments = bookingSystem.getAllTreatments().stream()
                .filter(t -> physio.getExpertise().stream()
                        .anyMatch(e -> e.equalsIgnoreCase(t.getTreatmentName())))
                .toList();

        if (availableTreatments.isEmpty()) {
            System.out.println("\nThis physiotherapist has no matching treatments assigned.");
            return;
        }

        System.out.println("\nTreatments offered by " + physio.getFullName() + ":");
        for (int i = 0; i < availableTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + availableTreatments.get(i).getTreatmentName());
        }

        Treatment selectedTreatment;
        try {
            System.out.print("\nSelect treatment number: ");
            int treatmentIndex = Integer.parseInt(scanner.nextLine().trim());
            if (treatmentIndex < 1 || treatmentIndex > availableTreatments.size()) {
                System.out.println("Invalid treatment selection.");
                return;
            }
            selectedTreatment = availableTreatments.get(treatmentIndex - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        String formattedDate = sdf.format(date);
        Appointment booked = bookingSystem.bookAppointment(patient, physio, week, formattedDate, time, selectedTreatment);

        if (booked != null) {
            System.out.println("\n════════════════════════════════");
            System.out.println("Appointment booked successfully!");
            System.out.println("Appointment ID: " + booked.getAppointmentID());
            System.out.println("Keep this ID safe.");
        } else {
            System.out.println("Failed to book appointment.");
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
        System.out.println("\n════════════════════════════════");
        System.out.print("\nEnter area of expertise: ");
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
        System.out.println("\n════════════════════════════════");
        System.out.print("\nEnter physiotherapist's full name: ");
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
        System.out.println("\n════════════════════════════════");
        System.out.print("\nEnter treatment name: ");
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

        // Derive week from the chosen appointment
        int week = bookingSystem.getWeekFromDate(date);
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
        Appointment success = bookingSystem.bookAppointment(patient, physio, week, sdfOut.format(date), time, treatment);

        if (success != null) {
            System.out.println("✅ Appointment booked successfully!");
            System.out.println("Appointment ID: " + chosen.getAppointmentID());
            System.out.println("Keep this ID safe — you'll need it to cancel.");
        } else {
            System.out.println(" Failed to book appointment. It may already be taken.");
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

    private static void viewAllPhysiosWithExpertise() {
        System.out.println("\n════════════════════════════════");
        System.out.println("Physiotherapists and Their Expertise:\n");

        List<Physiotherapist> physios = bookingSystem.getAllPhysiotherapists();
        if (physios.isEmpty()) {
            System.out.println("No physiotherapists available.");
            return;
        }

        for (Physiotherapist p : physios) {
            System.out.println("- " + p.getFullName() + " | Expertise: " + String.join(", ", p.getExpertise()));
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

        Appointment newAppointment = bookingSystem.bookAppointment(patient, physio, newWeek, formattedDate, newTime, treatment);

        if (newAppointment != null) {
            // Cancel old appointment
            oldAppointment.setStatus("Cancelled");
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