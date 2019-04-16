package test.java.testStatus;

import java.io.*;
import java.util.Properties;

public class Prop1 {
    private String fileName;
    static Properties prop = new Properties();
    static OutputStream output = null;
    static InputStream input = null;

    public Prop1(String fileName){
        this.fileName = fileName;
    }

    public void setProperties(String name, String value){
        try {
            input = new FileInputStream(fileName);
            prop.load(input);
            output = new FileOutputStream(fileName);
            prop.setProperty(name, value);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {io.printStackTrace();}
        finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {e.printStackTrace();}
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {e.printStackTrace();}
            }
        }
    }

    public String getProperties(String name){
        String value=null;
        try {
            input = new FileInputStream(fileName);
            prop.load(input);
            value = prop.getProperty(name);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}