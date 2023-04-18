package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

    Point point = new Point(2, 8);
    Point point2 = new Point(5, 10);

    @Test
    public void testDistance1() {
        Assert.assertEquals(ClassMain.distance(point, point2), 3.605551275463989,
                "Assertion failed");
    }

    @Test
    public void testDistance2() {
        Assert.assertEquals(point.distance(point2), 3.605551275463989,
                "Assertion failed");
    }

}
