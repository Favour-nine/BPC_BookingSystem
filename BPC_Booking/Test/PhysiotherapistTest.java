import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PhysiotherapistTest {

    private Physiotherapist physio;
    private Treatment treatment;
    private Patient patient;

    @BeforeEach
    public void setUp() {
        physio = new Physiotherapist("Dr. Max", "9876543210", "Clinic Ave", List.of("Massage", "Rehabilitation"));
        treatment = new Treatment("Massage", "Muscle relaxation");
        patient = new Patient("John Doe", "1234567890", "123 Street");
    }

    @Test
    public void testConstructorInitializesCorrectly() {
        assertEquals("Dr. Max", physio.getFullName());
        assertEquals("9876543210", physio.getPhoneNumber());
        assertEquals("Clinic Ave", physio.getAddress());
        assertTrue(physio.getExpertise().contains("Massage"));
        assertTrue(physio.getExpertise().contains("Rehabilitation"));
    }

    @Test
    public void testAddAppointmentAndGetAvailable() {
        Appointment appointment = new Appointment("APT1", new Date(), "10:00 AM", treatment, physio, null);
        physio.addAppointment(1, appointment);

        List<Appointment> available = physio.getAvailableAppointments(1);
        assertEquals(1, available.size());
        assertEquals("APT1", available.getFirst().getAppointmentID());
    }

    @Test
    public void testGetAvailableAppointmentsNoneAvailable() {
        Appointment booked = new Appointment("APT2", new Date(), "11:00 AM", treatment, physio, patient);
        physio.addAppointment(1, booked);

        List<Appointment> available = physio.getAvailableAppointments(1);
        assertTrue(available.isEmpty());
    }

    @Test
    public void testGetAvailableAppointmentsEmptyWeek() {
        List<Appointment> available = physio.getAvailableAppointments(2);
        assertTrue(available.isEmpty());
    }
}
