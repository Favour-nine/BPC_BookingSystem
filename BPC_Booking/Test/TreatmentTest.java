import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class TreatmentTest {

    @Test
    public void testBookingAndCanceling() {
        Treatment treatment = new Treatment("Acupuncture", "Pain relief", "Acupuncture");

        Physiotherapist physio = new Physiotherapist("Dr. Max", "0000000000", "Zone A",
                java.util.List.of("Acupuncture"));
        Appointment appointment = new Appointment("APT123", new Date(), "14:00", treatment, physio, null);

        treatment.bookTreatment(appointment);
        assertEquals(1, treatment.getBookedAppointments().size());

        boolean cancelled = treatment.cancelBooking("APT123");
        assertTrue(cancelled);
        assertTrue(treatment.getBookedAppointments().isEmpty());
    }
}
