/*
 * Copyright (c) 2020, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

import static jdk.test.lib.net.IPSupport.hasIPv6;
import static jdk.test.lib.net.IPSupport.hasIPv4;
import static java.net.StandardProtocolFamily.INET;
import static java.net.StandardProtocolFamily.INET6;
import static org.testng.Assert.assertThrows;

/*
 * @test
 * @bug 8240533
 * @library /test/lib
 * @build jdk.test.lib.net.IPSupport
 * @summary Check that DatagramChannel throws expected Exception when connecting to port 0
 * @run testng ConnectPortZero
 * @run testng/othervm -Djava.net.preferIPv4Stack=true ConnectPortZero
 */

public class ConnectPortZero {
    private InetSocketAddress loopbackZeroAddr, wildcardZeroAddr;
    private DatagramChannel datagramChannel, datagramChannelIPv4, datagramChannelIPv6;
    private List<Object[]> channels;

    private static final Class<SocketException> SE = SocketException.class;

    @BeforeTest
    public void setUp() throws IOException {
        wildcardZeroAddr = new InetSocketAddress(0);
        loopbackZeroAddr = new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);

        channels = new ArrayList<>();

        datagramChannel = DatagramChannel.open();
        channels.add(new Object[]{datagramChannel});
        if (hasIPv4()) {
            datagramChannelIPv4 = DatagramChannel.open(INET);
            channels.add(new Object[]{datagramChannelIPv4});
        }
        if (hasIPv6()) {
            datagramChannelIPv6 = DatagramChannel.open(INET6);
            channels.add(new Object[]{datagramChannelIPv6});
        }
    }

    @DataProvider(name = "data")
    public Object[][] variants() {
        return channels.toArray(Object[][]::new);
    }

    @Test(dataProvider = "data")
    public void testChannelConnect(DatagramChannel dc) {
        assertThrows(SE, () -> dc.connect(loopbackZeroAddr));
        assertThrows(SE, () -> dc.connect(wildcardZeroAddr));
    }

    @AfterTest
    public void tearDown() throws IOException {
        for(Object[] ch : channels) {
            ((DatagramChannel)ch[0]).close();
        }
    }
}
