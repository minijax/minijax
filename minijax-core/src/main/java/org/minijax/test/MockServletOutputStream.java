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
package org.minijax.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class MockServletOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream outputStream;

    public MockServletOutputStream() {
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {
    }

    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
    }


    public String getValue() throws IOException {
        outputStream.flush();
        return outputStream.toString();
    }
}
