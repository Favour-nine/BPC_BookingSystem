import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Treatment implements Serializable {
    // Attributes
    private String treatmentName;
    private String description;
    private boolean isAvailable;
    private List<Appointment> bookedAppointments;

    // Constructor
    public Treatment(String treatmentName, String description) {
        this.treatmentName = treatmentName;
        this.description = description;
        this.isAvailable = true; // Default to available
        this.bookedAppointments = new ArrayList<>();
    }

    // Getter methods
    public String getTreatmentName() {
        return treatmentName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<Appointment> getBookedAppointments() {
        return bookedAppointments;
    }

    // Book a treatment (Adds an appointment)
    public void bookTreatment(Appointment appointment) {
        bookedAppointments.add(appointment);
    }

    // Cancel a booking
    public boolean cancelBooking(String appointmentID) {
        return bookedAppointments.removeIf(app -> app.getAppointmentID().equals(appointmentID));
    }

    // Get details about the treatment
    public String getTreatmentDetails() {
        return "Treatment: " + treatmentName + "\nDescription: " + description + "\nAvailable: " + isAvailable;
    }
}
