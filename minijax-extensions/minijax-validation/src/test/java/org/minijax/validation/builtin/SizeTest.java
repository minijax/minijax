package org.minijax.validation.builtin;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Size;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.validation.builtin.SizeValidators;

public class SizeTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /*
     * Array
     */

    public static class ArrayWidget {
        @Size(min = 2, max = 4)
        public final int[] x;
        public ArrayWidget(final int[] x) {
            this.x = x;
        }
    }

    @Test
    public void testArray() {
        assertTrue(validator.validate(new ArrayWidget(null)).isEmpty());
        assertTrue(validator.validate(new ArrayWidget(new int[] { 1, 2 })).isEmpty());
        assertTrue(validator.validate(new ArrayWidget(new int[] { 1, 2, 3, 4 })).isEmpty());

        assertEquals(1, validator.validate(new ArrayWidget(new int[] {})).size());
        assertEquals(1, validator.validate(new ArrayWidget(new int[] { 1 })).size());
        assertEquals(1, validator.validate(new ArrayWidget(new int[] { 1, 2, 3, 4, 5 })).size());
    }

    /*
     * CharSequence
     */

    public static class CharSequenceWidget {
        @Size(min = 2, max = 4)
        public final String x;
        public CharSequenceWidget(final String x) {
            this.x = x;
        }
    }

    @Test
    public void testCharSequence() {
        assertTrue(validator.validate(new CharSequenceWidget(null)).isEmpty());
        assertTrue(validator.validate(new CharSequenceWidget("aa")).isEmpty());
        assertTrue(validator.validate(new CharSequenceWidget("aaaa")).isEmpty());

        assertEquals(1, validator.validate(new CharSequenceWidget("")).size());
        assertEquals(1, validator.validate(new CharSequenceWidget("a")).size());
        assertEquals(1, validator.validate(new CharSequenceWidget("aaaaa")).size());
    }

    /*
     * Collection
     */

    public static class CollectionWidget {
        @Size(min = 2, max = 4)
        public final Collection<?> x;
        public CollectionWidget(final Collection<?> x) {
            this.x = x;
        }
    }

    @Test
    public void testCollection() {
        assertTrue(validator.validate(new CollectionWidget(null)).isEmpty());
        assertTrue(validator.validate(new CollectionWidget(asList(1, 2))).isEmpty());
        assertTrue(validator.validate(new CollectionWidget(asList(1, 2, 3, 4))).isEmpty());

        assertEquals(1, validator.validate(new CollectionWidget(emptyList())).size());
        assertEquals(1, validator.validate(new CollectionWidget(singletonList(1))).size());
        assertEquals(1, validator.validate(new CollectionWidget(asList(1, 2, 3, 4, 5))).size());
    }

    /*
     * Map
     */

    public static class MapWidget {
        @Size(min = 2, max = 4)
        public final Map<?, ?> x;
        public MapWidget(final Map<?, ?> x) {
            this.x = x;
        }
    }

    public static Map<Integer, Integer> mapOf(final int... keyValues) {
        final Map<Integer, Integer> result = new HashMap<>();
        for (final int keyValue : keyValues) {
            result.put(keyValue, keyValue);
        }
        return result;
    }

    @Test
    public void testMap() {
        assertTrue(validator.validate(new MapWidget(null)).isEmpty());
        assertTrue(validator.validate(new MapWidget(mapOf(1, 2))).isEmpty());
        assertTrue(validator.validate(new MapWidget(mapOf(1, 2, 3, 4))).isEmpty());

        assertEquals(1, validator.validate(new MapWidget(emptyMap())).size());
        assertEquals(1, validator.validate(new MapWidget(mapOf(1))).size());
        assertEquals(1, validator.validate(new MapWidget(mapOf(1, 2, 3, 4, 5))).size());
    }

    /*
     * Invalid
     */

    public static class IntegerWidget {
        @Size(min = 2, max = 4)
        public int x;
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final IntegerWidget b = new IntegerWidget();
        b.x = 10;
        validator.validate(b).size();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new SizeValidators();
    }
}
