import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class PatientTest {
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        patient = new Patient("John Doe", "1234567890", "123 Street");
        Physiotherapist physio = new Physiotherapist("Dr. Jane", "9876543210", "456 Avenue",
                java.util.List.of("Massage"));
        Treatment treatment = new Treatment("Massage", "Relaxing therapy", "Massage");
        appointment = new Appointment("APT001", new Date(), "10:00", treatment, physio, null);
    }

    @Test
    public void testBookAppointment() {
        boolean result = patient.bookAppointment(appointment);
        assertTrue(result);
        assertEquals(1, patient.getAppointments().size());
    }

    @Test
    public void testCancelAppointment() {
        patient.bookAppointment(appointment);
        boolean result = patient.cancelAppointment("APT001");
        assertTrue(result);
        assertTrue(patient.getAppointments().isEmpty());
    }

    @Test
    public void testChangeAppointment() {
        patient.bookAppointment(appointment);
        Appointment newAppointment = new Appointment("APT002", new Date(), "12:00",
                appointment.getTreatment(), appointment.getPhysiotherapist(), null);
        boolean result = patient.changeAppointment("APT001", newAppointment);
        assertTrue(result);
        assertEquals("APT002", patient.getAppointments().get(0).getAppointmentID());
    }
}
