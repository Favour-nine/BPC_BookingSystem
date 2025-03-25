import java.util.*;
public class BookingSystem {
    // Attributes
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;

    // Constructor
    public BookingSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Add a new patient
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    // Remove a patient
    public boolean removePatient(String patientID) {
        return patients.removeIf(patient -> patient.getUniqueId().equals(patientID));
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

    // Book an appointment
    public boolean bookAppointment(Patient patient, Physiotherapist physio, int week, String date, String time, Treatment treatment) {
        Appointment newAppointment = new Appointment(UUID.randomUUID().toString(), new Date(), time, treatment, physio, patient);
        if (!physio.getAvailableAppointments(week).contains(newAppointment)) {
            physio.addAppointment(week, newAppointment);
            patient.bookAppointment(newAppointment);
            appointments.add(newAppointment);
            return true;  // Booking successful
        }
        return false;  // Slot already taken
    }

    // Cancel an appointment
    public boolean cancelAppointment(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.cancel();
                return true;
            }
        }
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



}
