
package org.minijax.json;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
