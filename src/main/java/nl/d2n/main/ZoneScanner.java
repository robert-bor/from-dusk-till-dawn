package nl.d2n.main;

import nl.d2n.model.Info;
import nl.d2n.model.OutsideBuilding;
import nl.d2n.model.Zone;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;

import java.io.File;
import java.util.*;

public class ZoneScanner {

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

        Map<String, OutsideBuilding> zones = new TreeMap<String, OutsideBuilding>();

        for (String name : names) {
            System.out.println("Processing "+name+"...");
            readFromFile(zones, name);
        }
//        readFromFile(zones, names[0]);
//        readFromFile(zones, names[1]);
//        readFromFile(zones, names[2]);

        for (String name : zones.keySet()) {
            OutsideBuilding building = zones.get(name);
            System.out.println(building.getName());
            System.out.println(building.getFlavor());
            System.out.println();
        }
    }

    static public void readFromFile(Map<String, OutsideBuilding> orderedZones, String fileName) throws Exception {
        String fileToRead = S3_DIR + "/" + fileName;
        File file = new File(fileToRead);
        String xml = FileToStringConverter.getContent(file);
        Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
        List<Zone> zones= info.getMap().getZones();
        if (zones == null) {
            return;
        }
        for (Zone zone : zones) {
            if (zone.getBuilding() != null) {
                OutsideBuilding building = zone.getBuilding();
                orderedZones.put(building.getName(), building);
            }
        }
    }
}
