package nl.d2n.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Cadaver {

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "dtype")
    private int deathType;

    @XmlAttribute
    private int id;

    @XmlAttribute
    private int day;

    @XmlElement(name = "msg")
    private String message;

    public String getName() { return this.name; }
    public int getDeathType() { return this.deathType; }
    public int getId() { return this.id; }
    public int getDay() { return this.day; }
    public String getMessage() { return this.message; }
}
