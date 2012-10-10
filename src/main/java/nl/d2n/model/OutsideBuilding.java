package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "building")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutsideBuilding {

    @Expose
    @XmlAttribute
    private String name;

    @Expose
    @XmlAttribute
    private int type;

    @Expose
    @XmlAttribute
    private int dig;

    @Expose
    @XmlValue
    private String flavor;

    @Expose
    @XmlTransient
    private String url;

    public OutsideBuilding() {}
    public OutsideBuilding(int type, String name, String flavor, String url) {
        this.type = type;
        this.name = name;
        this.flavor = flavor;
        this.url = url;
    }

    public static List<OutsideBuilding> extractOutsideBuildingsFromMap(List<Zone> zones) {
        List<OutsideBuilding> outsideBuildings = new ArrayList<OutsideBuilding>();
        for (Zone zone : zones) {
            if (zone.getBuilding() != null && zone.getBuilding().getType() != -1) {
                outsideBuildings.add(zone.getBuilding());
            }
        }
        return outsideBuildings;
    }

    public void setUrl(String url) { this.url = url; }

    public String getName() { return this.name; }
    public int getType() { return this.type; }
    public int getDig() { return this.dig; }
    public String getFlavor() { return this.flavor; }
    public String getUrl() { return this.url; }
}
