import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PhysiotherapistTest {

    @Test
    public void testAddAppointmentAndRetrieve() {
        // Setup physiotherapist
        Physiotherapist physio = new Physiotherapist(
                "Dr. Jane Smith", "1234567890", "Therapy Lane",
                List.of("Massage")
        );
        physio.generateWeeklySchedule();

        // Setup treatment and appointment
        Treatment treatment = new Treatment("Massage", "Muscle relaxation therapy", "Massage");
        Appointment appointment = new Appointment("APT001", new Date(), "09:00", treatment, physio, null);

        // Add appointment to week 1
        physio.addAppointment(1, appointment);

        // Assertions
        List<Appointment> weekAppointments = physio.getAvailableAppointments(1);
        assertTrue(weekAppointments.contains(appointment), "Appointment should be in week 1's schedule");

        List<Appointment> allAppointments = physio.getAllAppointments();
        assertTrue(allAppointments.contains(appointment), "Appointment should be in all appointments list");
    }

    @Test
    public void testExpertiseStorage() {
        List<String> expertise = List.of("Acupuncture", "Massage");
        Physiotherapist physio = new Physiotherapist("Dr. John Doe", "0987654321", "Wellness Road", expertise);

        assertEquals(expertise, physio.getExpertise(), "Expertise list should match input");
    }

    @Test
    public void testGenerateWeeklyScheduleInitializesWeeks() {
        Physiotherapist physio = new Physiotherapist("Dr. Alex", "1112223333", "Clinic Ave", List.of("Rehab"));
        physio.generateWeeklySchedule();

        for (int week = 1; week <= 4; week++) {
            assertNotNull(physio.getAvailableAppointments(week), "Each week's appointment list should be initialized");
        }
    }
}
