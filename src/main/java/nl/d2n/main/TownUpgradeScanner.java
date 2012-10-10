package nl.d2n.main;

import nl.d2n.model.Info;
import nl.d2n.model.TownUpgrade;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;

import java.io.File;
import java.util.*;

public class TownUpgradeScanner {

    public static final String S3_DIR = "/workspace/d2n/docs/xml-samples";

    public static void main(String args[]) throws Exception {

        File directory = new File(S3_DIR);
        if (!directory.isDirectory()) {
            System.out.println("Not a directory error");
            return;
        }
        String[] names = directory.list();
//        for (String name : names) {
//            System.out.println(name);
//        }

        List<TownUpgrade> upgrades = new ArrayList<TownUpgrade>();

        for (String name : names) {
            System.out.println("Processing "+name+"...");
            readFromFile(upgrades, name);
        }
//        upgrades = readFromFile(upgrades, names[0]);
//        readFromFile(upgrade, names[1]);
//        readFromFile(upgrades, names[2]);

        for (TownUpgrade upgrade : upgrades) {
            System.out.println(upgrade.getName());
        }
    }

    static public List<TownUpgrade> readFromFile(List<TownUpgrade> upgrades, String fileName) throws Exception {
        String fileToRead = S3_DIR + "/" + fileName;
        File file = new File(fileToRead);
        String xml = FileToStringConverter.getContent(file);
        Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
        List<TownUpgrade> foundUpgrades = info.getUpgrades();
        if (foundUpgrades == null) {
            return upgrades;
        }
        for (TownUpgrade upgrade : foundUpgrades) {
            if (!isUpgradeInList(upgrades, upgrade)) {
                upgrades.add(upgrade);
            }
        }
        return upgrades;
    }
    static public boolean isUpgradeInList(List<TownUpgrade> upgrades, TownUpgrade upgrade) {
        for (TownUpgrade currentUpgrade : upgrades) {
            if (upgrade.getName().equals(currentUpgrade.getName())) {
                return true;
            }
        }
        return false;
    }

}
