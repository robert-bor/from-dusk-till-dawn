package nl.d2n.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Owner {

    @XmlElement
    private Citizen citizen;

    @XmlElement(name = "myZone")
    private MyZone zone;

    public Citizen getCitizen() { return this.citizen; }
    public MyZone getZone() { return this.zone; }

    public void setCitizen(Citizen citizen) { this.citizen = citizen; }
}
