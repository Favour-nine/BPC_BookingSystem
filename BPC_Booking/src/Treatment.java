import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.Serial;

public class Treatment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    // Attributes
    private final String treatmentName;
    private final String description;
    private final boolean isAvailable;
    private final List<Appointment> bookedAppointments;
    private final String requiredExpertise;


    // Constructor
    public Treatment(String treatmentName, String description, String requiredExpertise) {
        this.treatmentName = treatmentName;
        this.description = description;
        this.requiredExpertise = requiredExpertise;
        this.isAvailable = true;
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

    public String getRequiredExpertise() {
        return requiredExpertise;
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


    @Override
    public String toString() {
        return treatmentName + " - " + description;
    }
}
