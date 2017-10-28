/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
package org.minijax.entity;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.minijax.util.IdUtils;

public class UuidConverterTest {
    private UuidConverter converter;


    @Before
    public void setUp() {
        converter = new UuidConverter();
    }


    @Test
    public void testNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }


    @Test
    public void testConvert() {
        final UUID uuid = IdUtils.create();
        final byte[] bytes = IdUtils.toBytes(uuid);

        assertArrayEquals(bytes, converter.convertToDatabaseColumn(uuid));
        assertEquals(uuid, converter.convertToEntityAttribute(bytes));
    }
}
