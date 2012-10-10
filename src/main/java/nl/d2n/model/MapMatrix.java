package nl.d2n.model;

import nl.d2n.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapMatrix {

    public static final boolean NO_CONVERSION_OF_COORDINATES = false;
    public static final boolean CONVERT_COORDINATES          = true;

    private int width;
    private int height;
    private int xPosCity;
    private int yPosCity;

    private Zone[][] matrix;

    public MapMatrix(int width, int height, int xPosCity, int yPosCity, List<Zone> zones, boolean hasUpgradedMap) {
        this.width = width;
        this.height = height;
        this.xPosCity = xPosCity;
        this.yPosCity = yPosCity;
        this.matrix = convertGameMap(zones, NO_CONVERSION_OF_COORDINATES);
        setDefaultValues(this.matrix, hasUpgradedMap);
        padGameMap(this.matrix);
    }

    public Zone[][] getMatrix() {
        return this.matrix;
    }

    public Integer getLeft() {
        return -xPosCity;
    }
    public Integer getRight() {
        return width - xPosCity - 1;
    }
    public Integer getTop() {
        return yPosCity;
    }
    public Integer getBottom() {
        return yPosCity - height + 1;
    }

    private void setDefaultValues(Zone[][] matrix, boolean hasUpgradedMap) {
        for (int xPos = 0; xPos < width; xPos++) {
            for (int yPos = 0; yPos < height; yPos++) {
                Zone zone = matrix[xPos][yPos];
                if (zone == null) {
                    continue;
                }
                if (zone.getDiscoveredNotVisitedToday()) {
                    zone.setDiscoveryStatus(DiscoveryStatus.DISCOVERED_NOT_VISITED_TODAY);
                } else {
                    zone.setDiscoveryStatus(DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY);
                }
                // A standard zone with nothing special has no danger tag and -1 zombies -- it should be NONE and 0
                if (    zone.getDiscoveryStatus() == DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY &&
                        zone.getDanger() == null &&
                        zone.getZombies() == -1) {
                    zone.setDanger(ZoneDanger.NONE);
                    zone.setZombies(0);
                }
                // Upgraded map displays the zombie amount, but not the danger level -- this has to be deduced
                if (    zone.getDiscoveryStatus() == DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY &&
                        hasUpgradedMap &&
                        zone.getZombies() >= 0) {
                    zone.setDanger(ZoneDanger.getClassWithZombies(zone.getZombies()));
                }
                if (zone.getDanger() == null) {
                    zone.setDanger(ZoneDanger.UNKNOWN);
                }

            }
        }
    }
    private void padGameMap(Zone[][] matrix) {
        for (int xPos = 0; xPos < width; xPos++) {
            for (int yPos = 0; yPos < height; yPos++) {
                if (matrix[xPos][yPos] == null) {
                    matrix[xPos][yPos] = createZone(xPos, yPos);
                }
            }
        }
    }
    private Zone[][] convertGameMap(List<Zone> zones, boolean convertCoordinates) {
        Zone[][] gameMatrix = new Zone[this.width][this.height];
        for (Zone zone : zones) {
            if (!convertCoordinates) {
                zone.setX(getRealXPos(zone.getX()));
                zone.setY(getRealYPos(zone.getY()));
            }
            int xPos = getMatrixXPos(zone.getX());
            int yPos = getMatrixYPos(zone.getY());

            gameMatrix[xPos][yPos] = zone;
        }
        return gameMatrix;
    }

    public void mergeZone(Zone zone, int today) {
        List<Zone> savedZone = new ArrayList<Zone>();
        savedZone.add(zone);
        mergeZones(savedZone, today);
    }

    public void mergeZones(List<Zone> zones, int today) {

        Zone[][] zonesToBeMerged = convertGameMap(zones, CONVERT_COORDINATES);

        for (int xPos = 0; xPos < width; xPos++) {
            for (int yPos = 0; yPos < height; yPos++) {
                Zone genericZone = matrix[xPos][yPos];
                Zone specificZone = zonesToBeMerged[xPos][yPos];
                if (specificZone != null) {
                    genericZone.mergeZone(specificZone, today);
                }
                // Abandoned buildings (in ID range 100 or higher) can never be camped for blueprints
                if (genericZone.getBuilding() != null && genericZone.getBuilding().getType() >= 100) {
                    genericZone.setBluePrintRetrieved(true);
                }
            }
        }
    }

    public void updateCitizenCoordinates(List<Citizen> citizens, boolean chaosMode, Citizen activeCitizen) {
        if (chaosMode) {
            // No citizens to place - we know nothing
            return;
        }
        for (Citizen citizen : citizens) {
            if (citizen.getMatrixX() == null || citizen.getMatrixY() == null) {
                continue;
            }
            int realX = getRealXPos(citizen.getMatrixX());
            int realY = getRealYPos(citizen.getMatrixY());
            if (    activeCitizen != null &&
                    activeCitizen.getId().equals(citizen.getId())) {
                realX = getRealXPos(activeCitizen.getMatrixX());
                realY = getRealYPos(activeCitizen.getMatrixY());
                citizen.setOutside(activeCitizen.isOutside());
            }
            citizen.setX(realX);
            citizen.setY(realY);
        }

    }

    protected Zone createZone(int xPos, int yPos) {
        return Zone.createZone(getRealXPos(xPos), getRealYPos(yPos));
    }

    public int getRealXPos(int xPos) {
        return xPos - xPosCity;
    }
    public int getMatrixXPos(int xPos) {
        return xPosCity + xPos;
    }
    public int getRealYPos(int yPos) {
        return yPosCity - yPos;
    }
    public int getMatrixYPos(int yPos) {
        return yPosCity - yPos;
    }

    public String toJson() {
        return GsonUtil.objectToJson(toJsonMap());
    }

    public Map<Integer, Map<Integer, Zone>> toJsonMap() {
        Map<Integer, Map<Integer, Zone>> map = new TreeMap<Integer, Map<Integer, Zone>>();
        for (int xPos = 0; xPos < this.width; xPos++) {
            int realXPos = getRealXPos(xPos);
            Map<Integer, Zone> column = new TreeMap<Integer, Zone>();
            map.put(realXPos, column);

            for (int yPos = 0; yPos < this.height; yPos++) {
                int realYPos = getRealYPos(yPos);
                Zone zone = this.matrix[xPos][yPos];
                column.put(realYPos, zone);
            }
        }
        return map;
    }

}
