package nl.d2n.model;

import com.sun.xml.internal.txw2.annotation.XmlCDATA;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class News {

    @XmlAttribute(name = "z")
    private int zeds;

    @XmlAttribute(name = "def")
    private int defense;

    public int getZeds() { return this.zeds; }
    public int getDefense() { return this.defense; }
}
