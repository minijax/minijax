package org.minijax.rs.util;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;
import org.minijax.rs.util.NetworkUtils;

public class NetworkUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new NetworkUtils();
    }

    @Test
    public void testEmptyInterfaces() {
        final Enumeration<NetworkInterface> nis = enumeration(emptyList());
        assertNull(NetworkUtils.getIpAddress(nis));
    }

    @Test
    public void testLoopbackOnly() {
        final Enumeration<NetworkInterface> nis = enumeration(singletonList(createInterface("127.0.0.1")));
        assertNull(NetworkUtils.getIpAddress(nis));
    }

    @Test
    public void testLinkLocalOnly() {
        final Enumeration<NetworkInterface> nis = enumeration(singletonList(createInterface("169.254.172.42")));
        assertNull(NetworkUtils.getIpAddress(nis));
    }

    @Test
    public void testIpv6Only() {
        final Enumeration<NetworkInterface> nis = enumeration(singletonList(createInterface("2001:0db8:85a3:0000:0000:8a2e:0370:7334")));
        assertNull(NetworkUtils.getIpAddress(nis));
    }

    @Test
    public void testFargate() {
        final Enumeration<NetworkInterface> nis = enumeration(asList(
                createInterface("127.0.0.1"),
                createInterface("169.254.172.42"),
                createInterface("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "10.0.0.1")));
        assertEquals("10.0.0.1", NetworkUtils.getIpAddress(nis));
    }

    private static NetworkInterface createInterface(final String... addresses) {
        final List<InterfaceAddress> ifaces = new ArrayList<>();

        for (final String address : addresses) {
            final InetAddress inetAddress = mock(InetAddress.class);
            when(inetAddress.getHostAddress()).thenReturn(address);

            final InterfaceAddress iface = mock(InterfaceAddress.class);
            when(iface.getAddress()).thenReturn(inetAddress);

            ifaces.add(iface);
        }

        final NetworkInterface ni = mock(NetworkInterface.class);
        when(ni.getInterfaceAddresses()).thenReturn(ifaces);
        return ni;
    }
}
