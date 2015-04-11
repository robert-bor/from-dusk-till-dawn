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
    private double maximum;

    @Expose
    @XmlAttribute(name = "min")
    private double minimum;

    @Expose
    @XmlAttribute
    private boolean maxed;

    public int getDay() { return this.day; }
    public double getMaximum() { return this.maximum; }
    public double getMinimum() { return this.minimum; }
    public boolean isMaxed() { return this.maxed; }

}
