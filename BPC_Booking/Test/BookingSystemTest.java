import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTest {

    private BookingSystem system;
    private Patient patient;
    private Physiotherapist physio;
    private Treatment treatment;
    private Appointment slot;

    @BeforeEach
    public void setup() {
        system = new BookingSystem();
        patient = new Patient("Test Patient", "1234567890", "Test Address");
        physio = new Physiotherapist("Dr. Test", "0987654321", "Test Lane", java.util.List.of("Massage"));
        treatment = new Treatment("Massage", "Relaxation", "Massage");

        system.addPatient(patient);
        system.addPhysiotherapist(physio);
        system.addTreatment(treatment);

        slot = new Appointment("SLOT1", new Date(), "09:00", treatment, physio, null);
        physio.addAppointment(1, slot);
    }

    @Test
    public void testFinalizeBooking() {
        Appointment booked = system.finalizeBooking(slot, patient);
        assertNotNull(booked);
        assertEquals("Booked", booked.getStatus());
        assertEquals(patient, booked.getPatient());
    }

    @Test
    public void testCancelAppointment() {
        Appointment booked = system.finalizeBooking(slot, patient);
        boolean result = system.cancelAppointment(booked.getAppointmentID());
        assertTrue(result);
        assertEquals("Cancelled", booked.getStatus());
    }

    @Test
    public void testRemovePhysiotherapist() {
        Appointment booked = system.finalizeBooking(slot, patient);
        boolean removed = system.removePhysiotherapistByName("Dr. Test");
        assertTrue(removed);
        assertTrue(system.getAllPhysiotherapists().isEmpty());
    }
}
