import java.util.Date;
import java.io.Serializable;

public class Appointment implements Serializable {
    // Attributes
    private String appointmentID;
    private Date date;
    private String time;
    private Treatment treatment;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private String status; // "Booked", "Cancelled", "Attended"

    // Constructor
    public Appointment(String appointmentID, Date date, String time, Treatment treatment, Physiotherapist physiotherapist, Patient patient){

        this.appointmentID = appointmentID;
        this.date = date;
        this.time = time;
        this.treatment = treatment;
        this.physiotherapist = physiotherapist;
        this.patient = patient;
        this.status = (patient == null) ? "Available" : "Booked"; // Default status when created
    }

    // Getter for appointmentID (fixing the error in Patient.java)
    public String getAppointmentID() {
        return appointmentID;
    }

    public Date getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }
    public Treatment getTreatment(){
        return treatment;
    }

    public Physiotherapist getPhysiotherapist(){
        return physiotherapist;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Cannot assign null to patient.");
        }
        this.patient = patient;
        this.status = "Booked";
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Methods to update appointment status
    public void book(){
        this.status = "Booked";
    }

    public void cancel(){
        this.status = "Cancelled";
    }

    public void attend(){
        this.status = "Attended";
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID +
                ", Date: " + date +
                ", Time: " + time +
                ", Treatment: " + treatment.getTreatmentName() +
                ", Physiotherapist: " + physiotherapist.getFullName() +
                (patient != null ? ", Patient: " + patient.getFullName() : ", Patient: N/A") +
                ", Status: " + status;
    }
}
