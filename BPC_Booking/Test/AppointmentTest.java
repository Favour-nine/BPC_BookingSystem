import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

    private Appointment appointment;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private Treatment treatment;
    private Date date;

    @BeforeEach
    public void setUp() {
        date = new Date();
        physiotherapist = new Physiotherapist("Dr. Smith", "1234567890", "Clinic Avenue",
                java.util.List.of("Rehabilitation"));
        patient = new Patient("John Doe", "9876543210", "123 Main St");
        treatment = new Treatment("Massage Therapy", "Relieves muscle tension and promotes relaxation");

        appointment = new Appointment("APT001", date, "10:00 AM", treatment, physiotherapist, patient);
    }

    @Test
    public void testAppointmentConstruction() {
        assertNotNull(appointment);
        assertEquals("APT001", appointment.getAppointmentID());
        assertEquals(date, appointment.getDate());
        assertEquals("10:00 AM", appointment.getTime());
        assertEquals(treatment, appointment.getTreatment());
        assertEquals(physiotherapist, appointment.getPhysiotherapist());
        assertEquals(patient, appointment.getPatient());
        assertEquals("Booked", appointment.getStatus());
    }

    @Test
    public void testGetters() {
        assertEquals("APT001", appointment.getAppointmentID());
        assertEquals(date, appointment.getDate());
        assertEquals("10:00 AM", appointment.getTime());
        assertEquals(treatment, appointment.getTreatment());
        assertEquals(physiotherapist, appointment.getPhysiotherapist());
        assertEquals(patient, appointment.getPatient());
        assertEquals("Booked", appointment.getStatus());
    }

    @Test
    public void testSetStatus() {
        appointment.setStatus("Attended");
        assertEquals("Attended", appointment.getStatus());
    }

    @Test
    public void testToStringFormat() {
        String expected = "Appointment ID: APT001, Date: " + appointment.getDate()
                + ", Time: 10:00 AM, Treatment: Massage Therapy, Physiotherapist: Dr. Smith, Patient: John Doe, Status: Booked";
        assertEquals(expected, appointment.toString());
    }

    @Test
    public void testAvailableAppointmentWithoutPatient() {
        Appointment availableSlot = new Appointment("APT002", new Date(), "11:00 AM", treatment, physiotherapist, null);
        assertNull(availableSlot.getPatient());
        assertEquals("Available", availableSlot.getStatus());
    }


}
