import java.util.*;
import java.io.Serializable;
public class Physiotherapist extends Member implements Serializable {
    private static final long serialVersionUID = 1L;
    // Additional attributes
    private List<String> expertise;
    private Map<Integer, List<Appointment>> schedule;// Weekly schedule (Week number → Appointments)

    // Constructor
    public Physiotherapist(String fullName, String phoneNumber, String address, List<String> expertise){
        super(fullName, phoneNumber, address, generateUniqueId());
        this.expertise = expertise;
        this.schedule = new HashMap<>();
    }

    // Get Expertise
    public List<String> getExpertise() {
        return expertise;
    }

    //Generate Unique ID for physiotherapist
    private static String generateUniqueId() {
        return "PHY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /* Add appointment to schedule
    public boolean addAppointment(int week, Appointment appointment) {
        schedule.putIfAbsent(week, new ArrayList<>());

        // Check for conflicts (To be implemented later)
        schedule.get(week).add(appointment);
        return true;
    }*/

    // Get available appointments for a given week
    public List<Appointment> getAvailableAppointments(int week){
        return schedule.getOrDefault(week, new ArrayList<>());
    }

    // Add an appointment to the schedule
    public boolean addAppointment(int week, Appointment appointment) {
        schedule.putIfAbsent(week, new ArrayList<>());
        return schedule.get(week).add(appointment);
    }

    // Generate weekly schedule
    public void generateWeeklySchedule() {
        for (int week = 1; week <= 4; week++) {
            schedule.put(week, new ArrayList<>());
        }
    }
    public List<Appointment> getAllAppointments() {
        List<Appointment> allAppointments = new ArrayList<>();
        for (List<Appointment> weekList : schedule.values()) {
            allAppointments.addAll(weekList);
        }
        return allAppointments;
    }


    // Display physiotherapist details
    public String getPhysiotherapistDetails() {
        return "Physiotherapist: " + getFullName() + "\nExpertise: " + String.join(", ", expertise);
    }

}
