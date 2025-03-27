import java.util.*;
public class Patient extends Member {
    // List of appointments
    private List<Appointment> appointmentList;

    // Constructor
    public Patient(String fullName, String phoneNumber, String address){
        super(fullName, phoneNumber, address, generateUniqueId());
        this.appointmentList = new ArrayList<>();
    }

    // Generate unique ID
    private static String generateUniqueId() {
        return "PAT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }



    // Book an appointment
    public boolean bookAppointment(Appointment appointment){
        // Check for conflicts (To be implemented later)
        appointmentList.add(appointment);
        return true;
    }
    // Cancel an appointment
    public boolean cancelAppointment(String appointmentID){
        return appointmentList.removeIf(appointment -> appointment.getAppointmentID().equals(appointmentID));
    }

    // Change an appointment
    public boolean changeAppointment(String appointmentID, Appointment newAppointment){
        for (int i = 0; i < appointmentList.size(); i++){
            if (appointmentList.get(i).getAppointmentID().equals(appointmentID)){
                appointmentList.set(i, newAppointment);
                return true;
            }
        }
        return false;
    }

    // Get all appointments
    public List<Appointment> getAppointments(){
        return appointmentList;
    }
}
