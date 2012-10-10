package nl.d2n.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game {

    @XmlAttribute(name = "days")
    private int day;

    @XmlAttribute(name = "quarantine")
    private boolean quarantined;

    @XmlAttribute(name = "datetime")
    private String date;

    @XmlAttribute
    private int id;

    public int getDay() { return this.day; }
    public boolean isQuarantined() { return this.quarantined; }
    public String getDate() { return this.date; }
    public int getId() { return this.id; }

}
