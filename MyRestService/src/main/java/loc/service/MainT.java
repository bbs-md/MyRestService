package main.java.loc.service;


import test.java.testStatus.Prop1;
import test.java.testStatus.GenerateReport;

public class MainT {
    private static Prop1 prop;
    public static void main(String[] args) {
        //MyDataProvider dp = new MyDataProvider();
        GenerateReport rt = new GenerateReport("target/surefire-reports-surefire/Suite1/Second.html");
        String[] array = rt.extractText();
        /*for (String s: array) {
            System.out.println("--" + s);
            for (String s1: s.split("\t")) {
                System.out.println("** " + s1);
            }
        }*/
        /*System.out.println("1-ret second ret".split("\\s+").length);
        System.out.println("2-ret second".split("\n").length);
        System.out.println("3-ret \tsecond");
        System.out.println("3-ret \tsecond".split("\t").length);
        System.out.println("4-ret second".split("\r").length);
        System.out.println(("5-ret second" +
                "ret").split("\n|\r").length);*/

        rt.writeRaport();
    }
}
