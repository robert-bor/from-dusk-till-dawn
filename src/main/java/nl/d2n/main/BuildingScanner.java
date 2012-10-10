package nl.d2n.main;

import nl.d2n.model.Info;
import nl.d2n.model.InsideBuilding;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;

import java.io.File;
import java.util.*;

public class BuildingScanner {

    public static final String S3_DIR = "docs/sample-xml/s3";

    public static void main(String args[]) throws Exception {

        File directory = new File(S3_DIR);
        if (!directory.isDirectory()) {
            System.out.println("Not a directory error");
            return;
        }
        String[] names = directory.list();
        for (String name : names) {
            System.out.println(name);
        }

        Map<Integer, Set<InsideBuilding>> buildings = new TreeMap<Integer, Set<InsideBuilding>>();

        for (String name : names) {
            System.out.println("Processing "+name+"...");
            readFromFile(buildings, name);
        }
//        readFromFile(buildings, names[0]);
//        readFromFile(buildings, names[1]);
//        readFromFile(buildings, names[2]);

        Set<InsideBuilding> rootBuildings = buildings.get(0);
        for (InsideBuilding building : rootBuildings) {
            printBuilding(0, building, buildings);
        }
    }
    static public void printBuilding(int layer, InsideBuilding building, Map<Integer, Set<InsideBuilding>> buildings) {
        printText(layer, building.getName());
        printText(layer, building.getFlavor());
        System.out.println();
        Set<InsideBuilding> childBuildings = buildings.get(building.getBuildingId());
        if (childBuildings == null) {
            return;
        }
        for (InsideBuilding childBuilding : childBuildings) {
            printBuilding(layer+1, childBuilding, buildings);
        }
    }
    static public void printText(int layer, String text) {
        for (int i = 0; i < layer * 2; i++) {
            System.out.print("_");
        }
        System.out.println(text);
    }

    static public void readFromFile(Map<Integer, Set<InsideBuilding>> orderedBuildings, String fileName) throws Exception {
        String fileToRead = S3_DIR + "/" + fileName;
        File file = new File(fileToRead);
        String xml = FileToStringConverter.getContent(file);
        Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
        List<InsideBuilding> buildings = info.getCity().getBuildings();
        if (buildings == null) {
            return;
        }
        for (InsideBuilding building : buildings) {
            Set<InsideBuilding> parentList = orderedBuildings.get(building.getParent());
            if (parentList == null) {
                parentList = new TreeSet<InsideBuilding>();
                orderedBuildings.put(building.getParent(), parentList);
            }
            parentList.add(building);
        }
    }

}
