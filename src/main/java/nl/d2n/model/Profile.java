package nl.d2n.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Profile {

    @XmlElementWrapper(name = "rewards")
    @XmlElement(name = "r")
    private List<Distinction> distinctions;

    @XmlElementWrapper(name = "maps")
    @XmlElement(name = "m")
    private List<PlayedMap> playedMaps;

    public List<PlayedMap> getPlayedMaps() { return this.playedMaps; }
    public List<Distinction> getDistinctions() { return this.distinctions; }
    public Integer getSoulPoints() {
        Integer soulPoints = 0;
        for (PlayedMap playedMap : getPlayedMaps()) {
            soulPoints += playedMap.getScore();
        }
        return soulPoints;
    }
}
