package test.java.testStatus;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateReport {
    private static String fileName = null;
    public GenerateReport(){}
    public GenerateReport(String fileName){this.fileName = fileName;}
    public void setfileName(String fileN){
        fileName = fileN;
    }
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");


    public static String[] extractText(){
        String raport = getFromHtml();
        String[] array = raport.split("\n|\r");
        String[] array1 = array[23].split("\t");
        System.out.println("array - " + array.length+"\narray1-"+array1.length);
        return  array;
    }

    public static String getFromHtml(){
        if (fileName == null) return null;
        String text = null;
        BodyContentHandler handler = new BodyContentHandler();

        Metadata metadata = new Metadata();
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(new File(
                    fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ParseContext pcontext = new ParseContext();

        //Html parser
        HtmlParser htmlparser = new HtmlParser();

        try {
            htmlparser.parse(inputstream, handler, metadata,pcontext);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        //System.out.println("Contents of the document:" + handler.toString());
        //System.out.println("Metadata of the document:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {System.out.println(name + ":   " + metadata.get(name));}
        return handler.toString();
    }

    public void writeRaport(){
        String raport = getFromHtml();
        String[] array = raport.split("\n|\r");
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Raport");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        int rowNum = 1;

       /* XSSFRow row = sheet.createRow(0);
        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("New Cell");
        cell.setCellStyle(style);*/
        boolean b = false;
        for (String s: array) {
            Row row = sheet.createRow(rowNum++);
            int i=0;
            String[] array1 = s.split("\t");
            for (String s2: array1) {
                if (s2 == null || s2.equalsIgnoreCase("")) s2 = " ";

                if (s2.equalsIgnoreCase("Exception")) b = true;
                row.createCell(i).setCellValue(s2);
                if (i == 1 && b) {
                    i++;
                    row.createCell(i).setCellValue("");
                }



                //Cell cell = headerRow.createCell(i);
               // cell.setCellValue(s2);
                //cell.setCellStyle(headerCellStyle);
                i++;
            }
        }

/*
        for (int x = 0; x < 3; x++) {
            sheet.autoSizeColumn(x);
        }*/
        //String current_date = simpleDateFormat.format(simpleDateFormat.parse(dat));
        String current_date = simpleDateFormat.format(new Date());
        System.out.println("current_date : "+current_date);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("src/test/resources/paport_" + current_date + ".xlsx");
            System.out.println("RAPOSRT in src/test/resources/paport_" + current_date + ".xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Closing the workbook
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
