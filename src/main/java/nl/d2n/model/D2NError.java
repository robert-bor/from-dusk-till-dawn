package nl.d2n.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class D2NError {

    @Enumerated(EnumType.STRING)
    @XmlAttribute(name = "code")
    D2NErrorCode code;

    public D2NErrorCode getCode() { return this.code; }
}
