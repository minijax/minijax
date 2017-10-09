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
package org.minijax.servlet;

import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class MockServletInputStream extends ServletInputStream {
    private final byte[] bytes;
    private volatile int index;

    public MockServletInputStream(final String str) {
        this.bytes = str != null ? str.getBytes() : null;
        this.index = 0;
    }

    @Override
    public boolean isReady() {
        return index < bytes.length;
    }

    @Override
    public boolean isFinished() {
        return index >= bytes.length;
    }

    @Override
    public void setReadListener(final ReadListener readListener) {
    }

    @Override
    public int read() throws IOException {
        return index >= bytes.length ? -1 : bytes[index++];
    }
}
