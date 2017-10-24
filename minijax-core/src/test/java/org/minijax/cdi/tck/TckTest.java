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
import org.minijax.cdi.Key;
import org.minijax.cdi.MinijaxInjector;

public class TckTest {

    public static junit.framework.Test suite() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.register(SpareTire.class, Key.of(Tire.class, "spare"));
        injector.register(DriversSeat.class, Key.of(Seat.class, Drivers.class));
        injector.register(V8Engine.class, Engine.class);
        injector.register(Convertible.class, Car.class);

        final Car car = injector.get(Car.class);
        assertNotNull(car);

        return Tck.testsFor(car, false, false);
    }
}
