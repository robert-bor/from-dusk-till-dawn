package nl.d2n.model.builder;

import nl.d2n.model.*;

import java.util.List;

public class ZoneBuilder {

    private Zone zone = new Zone();

    public ZoneBuilder setId(Long id) {
        zone.setId(id);
        return this;
    }
    public ZoneBuilder setX(int x) {
        zone.setX(x);
        return this;
    }
    public ZoneBuilder setY(int y) {
        zone.setY(y);
        return this;
    }
    public ZoneBuilder setVisited(boolean visited) {
        zone.setVisited(true);
        return this;
    }
    public ZoneBuilder setZombies(int zombies) {
        zone.setZombies(zombies);
        return this;
    }
    public ZoneBuilder setScoutSense(int scoutSense) {
        zone.setScoutSense(scoutSense);
        return this;
    }
    public ZoneBuilder setZoneDepleted(boolean zoneDepleted) {
        zone.setZoneDepleted(zoneDepleted);
        return this;
    }
    public ZoneBuilder setBuildingDepleted(boolean buildingDepleted) {
        zone.setBuildingDepleted(buildingDepleted);
        return this;
    }
    public ZoneBuilder setBluePrintRetrieved(boolean bluePrintRetrieved) {
        zone.setBluePrintRetrieved(bluePrintRetrieved);
        return this;
    }
    public ZoneBuilder setScoutPeek(String scoutPeek) {
        zone.setScoutPeek(scoutPeek);
        return this;
    }
    public ZoneBuilder setCampingTopology(CampingTopology campingTopology) {
        zone.setCampingTopology(campingTopology);
        return this;
    }
    public ZoneBuilder addItem(Item item) {
        zone.addItem(item);
        return this;
    }
    public ZoneBuilder setCity(City city) {
        zone.setCity(city);
        return this;
    }
    public ZoneBuilder setItems(List<Item> items) {
        zone.setItems(items);
        return this;
    }
    public ZoneBuilder setDanger(ZoneDanger danger) {
        zone.setDanger(danger);
        return this;
    }
    public ZoneBuilder setDiscoveryStatus(DiscoveryStatus discoveryStatus) {
        zone.setDiscoveryStatus(discoveryStatus);
        return this;
    }
    public Zone toZone() {
        return zone;
    }
}
