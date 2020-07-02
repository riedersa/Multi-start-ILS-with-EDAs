package DataStructures;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CalculationInstanceTest {

    @Test
    public void testConstructor() {
        CalculationInstance calculationInstance = new CalculationInstance("eda", "ls", "multi");
        Assertions.assertEquals("eda", calculationInstance.getEda());
        Assertions.assertEquals("ls", calculationInstance.getLs());
        Assertions.assertEquals("multi", calculationInstance.getMultiStartILS());
        Assertions.assertNotEquals(null, calculationInstance.getKinds());
        Assertions.assertNotEquals(null, calculationInstance.getTours());
        Assertions.assertEquals(null, calculationInstance.getMinimum());
    }

    @Test
    public void testSetMinimum(){
        CalculationInstance calculationInstance = new CalculationInstance("eda", "ls", "multi");
        TSPTour tour = new TSPTour(new int[]{});
        calculationInstance.setMinimum(tour);
        Assertions.assertEquals(tour, calculationInstance.getMinimum());
    }

    @Test
    public void testAddStep(){
        CalculationInstance calculationInstance = new CalculationInstance("eda", "ls", "multi");
        TSPTour tour = new TSPTour(new int[]{});
        calculationInstance.addStep(tour, CalculationInstance.CalculationKind.INIT);
        Assertions.assertTrue(calculationInstance.getTours().contains(tour));
        Assertions.assertEquals(tour, calculationInstance.getTours().get(0));
        Assertions.assertEquals(1, calculationInstance.getTours().size());
        Assertions.assertEquals(CalculationInstance.CalculationKind.INIT, calculationInstance.getKinds().get(0));
        Assertions.assertEquals(1, calculationInstance.getKinds().size());
    }

}