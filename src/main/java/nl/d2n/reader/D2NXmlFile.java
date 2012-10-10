package nl.d2n.reader;

import java.io.*;

public class D2NXmlFile {

    private String directory;

    public void setDirectory(String directory) { this.directory = directory; }

    public D2NXmlFile() {}
    public D2NXmlFile(String directory) { setDirectory(directory);}

    @SuppressWarnings({"NullableProblems"})
    public void writeFile(final String xmlText, final Integer id) throws IOException, FileNotFoundException {
        writeFile(xmlText, null, id);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public void writeFile(final String xmlText, final String subdir, final Integer id) throws IOException, FileNotFoundException {
        File file = new File(getFilePath(directory, subdir, id));
        file.createNewFile();
        Writer xmlOut = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        xmlOut.write(xmlText);
        xmlOut.close();
    }

    @SuppressWarnings({"NullableProblems"})
    public String readFile(final Integer id) throws IOException, FileNotFoundException {
        return readFile(null, id);
    }
    public String readFile(final String subdir, final Integer id) throws IOException, FileNotFoundException {
        File file = new File(getFilePath(directory, subdir, id));
        BufferedReader xmlIn = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        StringBuilder xmlText = new StringBuilder();
        boolean first = true;
        String lineSeparator = System.getProperty("line.separator");
        for (String line = xmlIn.readLine(); line != null ; line = xmlIn.readLine()) {
            if (!first) {
                xmlText.append(lineSeparator);
            }
            xmlText.append(line);
            first = false;
        }
        xmlIn.close();
        return xmlText.toString();
    }

    @SuppressWarnings({"NullableProblems"})
    public static String getFilePath(String directory, int id) {
        return getFilePath(directory, null, id);
    }
    public static String getFilePath(String directory, String subdir, int id) {
        return directory+(subdir==null?"":subdir+"/")+id+".xml";
    }
}
