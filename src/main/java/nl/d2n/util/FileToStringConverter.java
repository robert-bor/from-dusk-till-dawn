package nl.d2n.util;

import java.io.*;

public class FileToStringConverter {

    public static String SEPARATOR = System.getProperty("line.separator");

    static public String getContent(File aFile) throws Exception {
        StringBuilder contents = new StringBuilder();
        try {
            Reader fileReader = new InputStreamReader(new FileInputStream(aFile), "UTF-8");
            BufferedReader input =  new BufferedReader(fileReader);
            try {
                String line = null; //not declared within while loop
                while (( line = input.readLine()) != null){
                    contents.append(line);
                    contents.append(SEPARATOR);
                }
            }
            finally {
                input.close();
            }
        }
        catch (IOException ex){
            throw ex;
        }

        return contents.toString();
    }
}
