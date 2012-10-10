package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "e")
@XmlAccessorType(XmlAccessType.FIELD)
public class Estimation {

    @Expose
    @XmlAttribute
    private int day;

    @Expose
    @XmlAttribute(name = "max")
    private int maximum;

    @Expose
    @XmlAttribute(name = "min")
    private int minimum;

    @Expose
    @XmlAttribute
    private boolean maxed;

    public int getDay() { return this.day; }
    public int getMaximum() { return this.maximum; }
    public int getMinimum() { return this.minimum; }
    public boolean isMaxed() { return this.maxed; }

}
