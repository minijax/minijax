
package org.minijax.json;

import jakarta.ws.rs.WebApplicationException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class MinijaxJsonExceptionWrapper {

    @XmlElement(name = "code")
    private final int code;

    @XmlElement(name = "message")
    private final String message;

    private MinijaxJsonExceptionWrapper(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public MinijaxJsonExceptionWrapper(final WebApplicationException ex) {
        this(ex.getResponse().getStatus(), ex.getMessage());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
