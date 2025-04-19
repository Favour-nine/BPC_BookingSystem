import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {
    private Patient patient;
    private Physiotherapist physio;
    private Treatment treatment;
    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        patient = new Patient("John Doe", "1234567890", "Test Address");
        physio = new Physiotherapist("Dr. Max", "9876543210", "Clinic Ave", List.of("Rehabilitation"));
        treatment = new Treatment("Massage", "Relaxation");

        appointment = new Appointment(
                "APT12345",
                new Date(),
                "10:00 AM",
                treatment,
                physio,
                patient
        );
    }

    @Test
    public void testBookAppointment() {
        boolean result = patient.bookAppointment(appointment);
        assertTrue(result, "Appointment should be booked successfully");
        assertEquals(1, patient.getAppointments().size(), "Appointment list should contain one entry");
    }

    @Test
    public void testCancelAppointment() {
        patient.bookAppointment(appointment);
        boolean result = patient.cancelAppointment(appointment.getAppointmentID());
        assertTrue(result, "Appointment should be cancelled");
        assertTrue(patient.getAppointments().isEmpty(), "Appointment list should be empty after cancellation");
    }

    @Test
    public void testChangeAppointment() {
        patient.bookAppointment(appointment);

        Appointment newAppointment = new Appointment(
                "APT56789",
                new Date(),
                "11:00 AM",
                treatment,
                physio,
                patient
        );

        boolean result = patient.changeAppointment(appointment.getAppointmentID(), newAppointment);
        assertTrue(result, "Appointment should be changed");
        assertEquals("APT56789", patient.getAppointments().get(0).getAppointmentID(), "Updated appointment ID should match");
    }

    @Test
    public void testChangeAppointmentNotFound() {
        boolean result = patient.changeAppointment("NON_EXISTENT_ID", appointment);
        assertFalse(result, "Changing a non-existent appointment should return false");
    }
}
