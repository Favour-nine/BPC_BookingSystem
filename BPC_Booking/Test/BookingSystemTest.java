import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.*;
public class BookingSystemTest {
    private BookingSystem bookingSystem;
    private Patient patient;
    private Physiotherapist physio;
    private Treatment treatment;


    @BeforeEach
    public void setUp() {
        bookingSystem = new BookingSystem();

        patient = new Patient("John Doe", "1234567890", "123 Street");
        physio = new Physiotherapist("Jane Smith", "0987654321", "456 Avenue", Arrays.asList("Massage", "Rehabilitation"));
        treatment = new Treatment("Massage", "Relieves tension");

        bookingSystem.addPatient(patient);
        bookingSystem.addPhysiotherapist(physio);
        bookingSystem.addTreatment(treatment);
    }

    @Test
    public void testAddPatient() {
        assertTrue(bookingSystem.getPatients().contains(patient));
    }

    @Test
    public void testRegisterPatientAddsToList() {
        Patient patient = bookingSystem.registerPatient("John Doe", "1234567890", "10 Downing St");
        assertTrue(bookingSystem.getPatients().contains(patient));
    }


    @Test
    public void testAddPhysiotherapist() {
        assertTrue(bookingSystem.getAllPhysiotherapists().contains(physio));
    }

    @Test
    public void testAddTreatment() {
        assertTrue(bookingSystem.getAllTreatments().contains(treatment));
    }

    @Test
    public void testGetPhysiotherapistsByExpertise() {
        List<Physiotherapist> results = bookingSystem.getPhysiotherapistsByExpertise("Massage");
        assertFalse(results.isEmpty());
        assertEquals("Dr. Max", results.get(0).getFullName());
    }



    @Test
    public void testGetPhysiotherapistByName() {
        Physiotherapist found = bookingSystem.getPhysiotherapistByName("Dr. Max");
        assertNotNull(found);
        assertEquals("Dr. Max", found.getFullName());
    }

    @Test
    public void testBookAppointmentSuccess() {
        List<Appointment> slots = physio.getAvailableAppointments(1);
        Appointment slot = slots.stream().filter(a -> a.getPatient() == null).findFirst().orElse(null);

        assertNotNull(slot);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(slot.getDate());
        Appointment result = bookingSystem.bookAppointment(patient, physio, 1, dateStr, slot.getTime(), treatment);

        assertNotNull(result);
        assertEquals(patient, result.getPatient());
        assertEquals("Booked", result.getStatus());
    }

    @Test
    public void testBookAppointmentSlotTaken() {
        List<Appointment> slots = physio.getAvailableAppointments(1);
        Appointment slot = slots.stream().filter(a -> a.getPatient() == null).findFirst().orElse(null);

        assertNotNull(slot);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(slot.getDate());
        bookingSystem.bookAppointment(patient, physio, 1, dateStr, slot.getTime(), treatment);

        Appointment result = bookingSystem.bookAppointment(new Patient("Jane", "2223334444", "Park Lane"),
                physio, 1, dateStr, slot.getTime(), treatment);

        assertNull(result);
    }

    @Test
    public void testCancelExistingAppointment() {
        List<Appointment> slots = physio.getAvailableAppointments(1);
        Appointment slot = slots.stream().filter(a -> a.getPatient() == null).findFirst().orElse(null);

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(slot.getDate());
        Appointment booked = bookingSystem.bookAppointment(patient, physio, 1, dateStr, slot.getTime(), treatment);

        boolean result = bookingSystem.cancelAppointment(booked.getAppointmentID());
        assertTrue(result);
        assertEquals("Cancelled", booked.getStatus());
    }



    @Test
    public void testCancelAppointmentSuccess() {
        int week = 1;
        String date = "2025-05-01";
        String time = "10:00 AM";

        Appointment appointment = bookingSystem.bookAppointment(patient, physio, week, date, time, treatment);
        boolean result = bookingSystem.cancelAppointment(appointment.getAppointmentID());

        assertTrue(result);
        assertEquals("Cancelled", appointment.getStatus());
    }

    @Test
    public void testCancelNonExistentAppointment() {
        boolean result = bookingSystem.cancelAppointment("NON_EXISTENT_ID");
        assertFalse(result);
    }

}