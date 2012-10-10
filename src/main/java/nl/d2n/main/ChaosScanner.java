package nl.d2n.main;

import nl.d2n.model.City;
import nl.d2n.model.Info;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;

import java.io.File;
import java.util.*;

public class ChaosScanner {

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

        List<String> cities = new ArrayList<String>();
        for (String name : names) {
            if (name.endsWith(".xml")) {
                System.out.println("Processing "+name+"...");
                readFromFile(cities, name);
            }
        }

        for (String city : cities) {
            System.out.println(city);
        }
    }
    static public void readFromFile(List<String> cities, String fileName) throws Exception {
        String fileToRead = S3_DIR + "/" + fileName;
        File file = new File(fileToRead);
        String xml = FileToStringConverter.getContent(file);
        Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
        City city = info.getCity();
        if (city.isChaosMode()) {
            cities.add(info.getGameHeader().getGame().getId()+": "+city.getName());
        }
    }

}
