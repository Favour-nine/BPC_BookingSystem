import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;


public class BookingSystem {
    // Attributes
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;
    private List<Treatment> treatments = new ArrayList<>();

    // Constructor
    public BookingSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Save object (e.g., patient list or appointment list) to file
    public void saveData(Object data, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(data);
            System.out.println(" Data saved to " + filename);
        } catch (IOException e) {
            System.out.println(" Failed to save data to " + filename + ": " + e.getMessage());
        }
    }

    // Load object from file
    public Object loadData(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Failed to load data from " + filename + ": " + e.getMessage());
            return null;
        }
    }


    // Register new patient
    public Patient registerPatient(String fullName, String phoneNumber, String address) {
        Patient newPatient = new Patient(fullName, phoneNumber, address);
        patients.add(newPatient);
        System.out.println("Patient registered successfully. ID: " + newPatient.getUniqueId());
        return newPatient;
    }

    // Add a new patient
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    // Remove a patient
    public boolean removePatient(String patientID) {
        return patients.removeIf(patient -> patient.getUniqueId().equals(patientID));
    }

    public List<Treatment> getAllTreatments() {
        return new ArrayList<>(treatments); // Avoids direct modification
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments); // Return a copy for safety
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients); // Returns a copy to prevent modification outside the class
    }

    public List<Physiotherapist> getAllPhysiotherapists() {
        return new ArrayList<>(physiotherapists);
    }

    // Add a physiotherapist
    public void addPhysiotherapist(Physiotherapist physiotherapist) {
        physiotherapists.add(physiotherapist);
    }

    // Search for physiotherapists by expertise
    public List<Physiotherapist> getPhysiotherapistsByExpertise(String expertise) {
        List<Physiotherapist> result = new ArrayList<>();
        for (Physiotherapist physio : physiotherapists) {
            if (physio.getExpertise().contains(expertise)) {
                result.add(physio);
            }
        }
        return result;
    }

    // Search for a physiotherapist by name
    public Physiotherapist getPhysiotherapistByName(String name) {
        for (Physiotherapist physio : physiotherapists) {
            if (physio.getFullName().equalsIgnoreCase(name)) {
                return physio;
            }
        }
        return null;
    }
    // Adds a new treatment to the system
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    // Book an appointment only if no time conflict exists for the physiotherapist at the specified week/date/time
    public Appointment bookAppointment(Patient patient, Physiotherapist physio, int week, String dateStr, String time, Treatment treatment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
            return null;
        }

        // Reject weekends
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            System.out.println("\nCannot book appointment on a weekend. Physiotherapists work only Monday to Friday.");
            return null;
        }

        // Check physiotherapist expertise
        boolean hasExpertise = physio.getExpertise().stream()
                .anyMatch(exp -> exp.equalsIgnoreCase(treatment.getTreatmentName()));
        if (!hasExpertise) {
            System.out.println("\nCannot book appointment: Physiotherapist " + physio.getFullName() +
                    " does not have expertise in \"" + treatment.getTreatmentName() + "\".\n");
            return null;
        }

        // ✅ Find the exact existing appointment slot
        List<Appointment> physioAppointments = physio.getAllAppointments();
        for (Appointment existing : physioAppointments) {
            if (existing.getDate().equals(date) && existing.getTime().equalsIgnoreCase(time)) {
                if (existing.getPatient() != null) {
                    System.out.println("Slot already taken.");
                    return null;
                }

                // Optionally: Add 1-hour break buffer check here if needed

                // ✅ Book the slot
                existing.setPatient(patient);
                existing.setTreatment(treatment);
                existing.setStatus("Booked");

                patient.bookAppointment(existing);
                appointments.add(existing); // Track it globally
                return existing;
            }
        }

        // 🚫 Don't create a new slot – return failure
        System.out.println("Selected appointment slot not found in the existing schedule.");
        return null;
    }

    public Appointment finalizeBooking(Appointment appointment, Patient patient) {
        if (appointment == null || patient == null) {
            return null;
        }

        if (appointment.getPatient() != null) {
            System.out.println("This appointment is already booked.");
            return null;
        }

        appointment.setPatient(patient);
        appointment.setStatus("Booked");
        patient.bookAppointment(appointment);
        appointments.add(appointment); // Add to global list if not already

        return appointment;
    }



    // Cancel an appointment
    public boolean cancelAppointment(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.cancel();
                return true;
            }
        }
        return false;
    }

    // Generate a report of all appointments
    public void generateReport() {
        System.out.println("=== Appointment Report ===");
        for (Appointment appointment : appointments){
            System.out.println(
                    "Date: " + appointment.getDate() +
                            ", Time: " + appointment.getTime() +
                            ", Physiotherapist: " + appointment.getPhysiotherapist().getFullName() +
                            ", Patient: " + appointment.getPatient().getFullName() +
                            ", Status: " + appointment.getStatus() +
                            "Appointment ID: " + appointment.getAppointmentID()
            );
        }
    }

    public void exportReportToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== Appointment Report ===");

            for (Appointment appointment : appointments) {
                writer.println("Appointment ID: " + appointment.getAppointmentID());
                writer.println("Date: " + appointment.getDate());
                writer.println("Time: " + appointment.getTime());
                writer.println("Patient: " + appointment.getPatient().getFullName());
                writer.println("Physiotherapist: " + appointment.getPhysiotherapist().getFullName());
                writer.println("Treatment: " + appointment.getTreatment().getTreatmentName());
                writer.println("Status: " + appointment.getStatus());
                writer.println("------------------------------");
            }

            System.out.println(" Report successfully exported to: " + filename);
        } catch (IOException e) {
            System.out.println(" Failed to write report: " + e.getMessage());
        }
    }


    // Generate 4-week timetable with treatments for all physiotherapists
    public void generateTreatmentTimetable(List<Treatment> treatments) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MAY, 5); // Start from May 5, 2025 (Monday)

        for (Physiotherapist physio : physiotherapists) {
            physio.generateWeeklySchedule();

            Calendar weekStart = (Calendar) calendar.clone();

            for (int week = 1; week <= 4; week++) {
                Calendar dayPointer = (Calendar) weekStart.clone();

                for (int day = 0; day < 5; day++) { // Monday to Friday
                    for (int hour = 9; hour <= 15; hour += 2) { // 1-hour break between = every 2 hours
                        Date date = (Date) dayPointer.getTime();
                        String time = hour + ":00";

                        // Only assign treatments matching physio's expertise
                        List<Treatment> validTreatments = treatments.stream()
                                .filter(t -> physio.getExpertise().contains(t.getRequiredExpertise()))
                                .toList();

                        if (validTreatments.isEmpty()) continue;

                        Treatment treatment = validTreatments.get(new Random().nextInt(validTreatments.size()));

                        Appointment slot = new Appointment(
                                UUID.randomUUID().toString(),
                                date,
                                time,
                                treatment,
                                physio,
                                null // no patient yet
                        );
                        physio.addAppointment(week, slot);
                    }

                    dayPointer.add(Calendar.DATE, 1); // next weekday
                }

                weekStart.add(Calendar.DATE, 7); // move to the next week
            }
        }

        System.out.println("Weekday schedule generated (Mon–Fri, with 1-hour breaks) for all physiotherapists.");
    }


    public int getWeekFromDate(Date date) {
        Calendar base = Calendar.getInstance();
        base.set(2025, Calendar.MAY, 5); // Week 1 start

        Calendar target = Calendar.getInstance();
        target.setTime(date);

        long diff = target.getTimeInMillis() - base.getTimeInMillis();
        int days = (int) (diff / (1000 * 60 * 60 * 24));

        return (days / 7) + 1;
    }

    public void generateAnalyticsReport() {
        System.out.println("\n=== Analytics Report ===");

        // 1. Appointments by Treatment
        Map<String, Integer> treatmentCounts = new HashMap<>();
        for (Appointment a : appointments) {
            String name = a.getTreatment().getTreatmentName();
            treatmentCounts.put(name, treatmentCounts.getOrDefault(name, 0) + 1);
        }

        System.out.println("\nAppointments by Treatment:");
        for (Map.Entry<String, Integer> entry : treatmentCounts.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }

        // 2. Appointments by Physiotherapist
        Map<String, Integer> physioCounts = new HashMap<>();
        for (Appointment a : appointments) {
            String name = a.getPhysiotherapist().getFullName();
            physioCounts.put(name, physioCounts.getOrDefault(name, 0) + 1);
        }

        System.out.println("\nAppointments by Physiotherapist:");
        for (Map.Entry<String, Integer> entry : physioCounts.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }

        // 3. Appointments per Week
        Map<Integer, Integer> weekCounts = new HashMap<>();
        for (Appointment a : appointments) {
            int week = getWeekFromDate(a.getDate());
            weekCounts.put(week, weekCounts.getOrDefault(week, 0) + 1);
        }

        System.out.println("\nAppointments per Week:");
        for (Map.Entry<Integer, Integer> entry : weekCounts.entrySet()) {
            System.out.println("- Week " + entry.getKey() + ": " + entry.getValue());
        }
    }





}
