import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreatmentTest {
    private Treatment treatment;

    @BeforeEach
    public void setUp() {
        treatment = new Treatment("Massage Therapy", "Relieves muscle tension and promotes relaxation");
    }

    @Test
    public void testGetTreatmentName() {
        assertEquals("Massage Therapy", treatment.getTreatmentName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Relieves muscle tension and promotes relaxation", treatment.getDescription());
    }



    @Test
    public void testToStringFormat() {
        String expected = "Massage Therapy - Relieves muscle tension and promotes relaxation";
        assertEquals(expected, treatment.toString());
    }
}
