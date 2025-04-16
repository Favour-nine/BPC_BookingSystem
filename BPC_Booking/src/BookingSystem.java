import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    // Returns a copy of the treatment list for display or booking
    public List<Treatment> getAllTreatments() {
        return new ArrayList<>(treatments); // Avoids direct modification
    }

    // Book an appointment only if no time conflict exists for the physiotherapist at the specified week/date/time
    public Appointment bookAppointment(Patient patient, Physiotherapist physio, int week, String dateStr, String time, Treatment treatment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            // Convert the input string to a proper Date object
            date = sdf.parse(dateStr);
        }
        catch (ParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
            return null;
        }

        // Create the new appointment to be booked
        Appointment newAppointment = new Appointment(UUID.randomUUID().toString(), date, time, treatment, physio, patient);
        List<Appointment> existingAppointments = physio.getAvailableAppointments(week);

        // Ensure there is no existing appointment with the same time and date
        boolean timeConflict = existingAppointments.stream()
                .anyMatch(app -> app.getTime().equals(time) && app.getDate().equals(date));

        if (!timeConflict) {
            // If no conflict, add to physiotherapist, patient, and system-wide appointment list
            physio.addAppointment(week, newAppointment);
            patient.bookAppointment(newAppointment);
            appointments.add(newAppointment);
            return newAppointment;
        }
        return null;

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
                            ", Status: " + appointment.getStatus()
            );
        }
    }

    // Generate 4-week timetable with treatments for all physiotherapists
    public void generateTreatmentTimetable(List<Treatment> treatments) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MAY, 5); // week 1 starts May 5, 2025

        for (Physiotherapist physio : physiotherapists) {
            physio.generateWeeklySchedule();
            for (int week = 1; week <= 4; week++) {// weeks in a month
                for (int day = 0; day < 5; day++) {// monday to friday
                    for (int hour = 9; hour < 16; hour++){ // 7 sessions per day from 9 am to 3 pm
                        String time = hour + ":00";
                        Treatment treatment = treatments.get(new Random().nextInt(treatments.size()));
                        Date date = calendar.getTime();
                        Appointment slot = new Appointment(UUID.randomUUID().toString(), date, time, treatment, physio, null);
                        physio.addAppointment(week, slot);
                    }
                    calendar.add(Calendar.DATE, 1);
                }
                calendar.add(Calendar.DATE, 2); // skip saturday and sunday
            }
        }
        System.out.println("Weekday schedule (7 sessions/day, Mon–Fri) generated for all physiotherapists.");
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




}
