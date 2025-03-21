import java.util.*;
public class Physiotherapist extends Member {
    // Additional attributes
    private List<String> expertise;
    private Map<Integer, List<Appointment>> schedule;

    // Constructor
    public Physiotherapist(String fullName, String phoneNumber, String address, String uniqueId, List<String> expertise){
        super(fullName, phoneNumber, address, uniqueId);
        this.expertise = expertise;
        this.schedule = new HashMap<>();
    }

    // Get Expertise
    public List<String> getExpertise() {
        return expertise;
    }

    // Add appointment to schedule
    public boolean addAppointment(int week, Appointment appointment) {
        schedule.putIfAbsent(week, new ArrayList<>());

        // Check for conflicts (To be implemented later)
        schedule.get(week).add(appointment);
        return true;
    }
    // Get available appointments for a week
    public List<Appointment> getAvailableAppointments(int week) {
        return schedule.getOrDefault(week, new ArrayList<>());
    }
}
