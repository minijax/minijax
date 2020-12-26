package org.minijax.rs.cdi.tck;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.Test;
import org.minijax.cdi.MinijaxInjector;

class TckTest {

    private static Car getCar() {
        try (MinijaxInjector injector = new MinijaxInjector()) {
            return injector
                    .bind(SpareTire.class, Tire.class, "spare")
                    .bind(DriversSeat.class, Seat.class, Drivers.class)
                    .bind(V8Engine.class, Engine.class)
                    .bind(Convertible.class, Car.class)
                    .getResource(Car.class);
        }
    }

    public static junit.framework.Test suite() {
        return Tck.testsFor(getCar(), false, true);
    }

    @Test
    void testCar() {
        assertNotNull(getCar());
    }
}
