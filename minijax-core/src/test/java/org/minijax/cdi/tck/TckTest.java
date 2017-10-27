package org.minijax.cdi.tck;

import static org.junit.Assert.*;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.SpareTire;
import org.junit.Test;
import org.minijax.cdi.MinijaxInjector;

public class TckTest {

    public static Car getCar() {
        return new MinijaxInjector()
                .register(SpareTire.class, Tire.class, "spare")
                .register(DriversSeat.class, Seat.class, Drivers.class)
                .register(V8Engine.class, Engine.class)
                .register(Convertible.class, Car.class)
                .get(Car.class);
    }

    public static junit.framework.Test suite() {
        return Tck.testsFor(getCar(), false, true);
    }

    @Test
    public void testCar() {
        assertNotNull(getCar());
    }
}
