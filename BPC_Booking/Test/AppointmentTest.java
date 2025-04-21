import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class AppointmentTest {

    @Test
    public void testBookingAndStatus() {
        Physiotherapist physio = new Physiotherapist("Dr. Ken", "1112223333", "Road B",
                java.util.List.of("Spine Mobilisation"));
        Treatment treatment = new Treatment("Spine Mobilisation", "Back pain", "Spine Mobilisation");

        Appointment appointment = new Appointment("APT999", new Date(), "11:00", treatment, physio, null);
        assertEquals("Available", appointment.getStatus());

        Patient patient = new Patient("Anna Ray", "3334445555", "Flat C");
        appointment.setPatient(patient);
        assertEquals("Booked", appointment.getStatus());

        appointment.cancel();
        assertEquals("Cancelled", appointment.getStatus());
    }
}
