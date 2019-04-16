package test.java.testStatus;

import main.java.loc.service.controllers.ResponseS;
import main.java.loc.service.model.Task;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Parameters("FileXls")
public class MyDataProvider {
    static HSSFWorkbook hSSFWorkbook;
    private static final int VERB_COLUMN = 0;
    private static final int METHOD_COLUMN = 1;
    private static final int STATUS_COD_COLUMN = 2;
    private static final int POINT_COLUMN = 3;
    private static final int ID_COLUMN = 4;
    private static final int ITEM_COLUMN = 5;
    private static final int STATE_COLUMN = 6;
    private static final int DATAITEM_COLUMN = 7;
    private static final int MESSAGE_COLUMN = 9;
    private static final int SATUS_COLUMN = 10;
    private static Map<String, List<?>> map;

    private static final String fileXls = "src/test/resources/file_1.xls";

    static List<String[]>listItems;

    public MyDataProvider(){
        map = getDataFormXlsFile();
        Iterator<Map.Entry<String, List<?>>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, List<?>> pair = it.next();
            System.out.println("_____________________________________________");
            System.out.println("map_key:" + pair.getKey());
            List<?> list = pair.getValue();
            for (Object o: list) {System.out.println(o);}
        }
    }

    @DataProvider(name ="dataXls")
    public static Object[][] createData1(Method m){
        return getDataFromXls(m.getName());
        /*map = getDataFormXlsFile();
        Map<String, List<?>> mapCreate = new HashMap<>();
        Iterator<Map.Entry<String, List<?>>> it = map.entrySet().iterator();
        while (it.hasNext()){
            //System.out.println(m.getName());
            Map.Entry<String, List<?>> pair = it.next();
            if (m.getName().equalsIgnoreCase("createItemTest")) {
                if (pair.getKey().equalsIgnoreCase("create")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("createInvalidItemTest")) {
                if (pair.getKey().equalsIgnoreCase("invalidCreateItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("createInvalidStateTest")) {
                if (pair.getKey().equalsIgnoreCase("invalideCreateStatus")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("createWithEmptyBodyTest")) {
                if (pair.getKey().equalsIgnoreCase("createWithEmptyBody")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("getEmptyListItemsTest")) {
                if (pair.getKey().equalsIgnoreCase("getEmptyList")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("deleteItemTest")) {
                if (pair.getKey().equalsIgnoreCase("deleteItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("deleteInvalideItemTest")) {
                if (pair.getKey().equalsIgnoreCase("deleteInvalideItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (m.getName().equalsIgnoreCase("getItemsTest") || m.getName().equalsIgnoreCase("getItemByIdTest") ||
                    m.getName().equalsIgnoreCase("getItemByInvalidIdTest") || m.getName().equalsIgnoreCase("updateItemTest") ||
                    m.getName().equalsIgnoreCase("updateInvalidDataTest")) {
                if (pair.getKey().equalsIgnoreCase("create1") || pair.getKey().equalsIgnoreCase("create2") ||
                        pair.getKey().equalsIgnoreCase("create3")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if ( m.getName().equalsIgnoreCase("getItemByInvalidIdTest") &&
                        pair.getKey().equalsIgnoreCase("getItemByInvalidatedId")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if ( m.getName().equalsIgnoreCase("updateItemTest") &&
                        pair.getKey().equalsIgnoreCase("updateItem")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if ( m.getName().equalsIgnoreCase("updateInvalidDataTest") &&
                        pair.getKey().equalsIgnoreCase("updateInvalidDate")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }

            }
        }*/
        /*if (m.getName().equalsIgnoreCase("getItemsTest") || m.getName().equalsIgnoreCase("getItemByIdTest") ||
                m.getName().equalsIgnoreCase("getItemByInvalidIdTest") || m.getName().equalsIgnoreCase("updateItemTest") ||
                m.getName().equalsIgnoreCase("updateInvalidDataTest")){
            return new Object[][] {new Object[] { mapCreate}};
        }*/
        //return new Object[][] {new Object[] { mapCreate}};
    }

    public static Object[][] getDataFromXlsForAll(){
        return getDataFromXls("all");
    }

    public static Object[][] getDataFromXls(String method){
        map = getDataFormXlsFile();
        Map<String, List<?>> mapCreate = new HashMap<>();
        Iterator<Map.Entry<String, List<?>>> it = map.entrySet().iterator();
        while (it.hasNext()){
            //System.out.println(m.getName());
            Map.Entry<String, List<?>> pair = it.next();
            if (method.equalsIgnoreCase("createItemTest")) {
                if (pair.getKey().equalsIgnoreCase("create")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("createInvalidItemTest")) {
                if (pair.getKey().equalsIgnoreCase("invalidCreateItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("createInvalidStateTest")) {
                if (pair.getKey().equalsIgnoreCase("invalideCreateStatus")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("createWithEmptyBodyTest")) {
                if (pair.getKey().equalsIgnoreCase("createWithEmptyBody")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("updateInvalideIdItemTest")) {
                if (pair.getKey().equalsIgnoreCase("updateInvalidId")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("getEmptyListItemsTest")) {
                if (pair.getKey().equalsIgnoreCase("getEmptyList")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("deleteItemTest")) {
                if (pair.getKey().equalsIgnoreCase("deleteItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("deleteInvalideItemTest")) {
                if (pair.getKey().equalsIgnoreCase("deleteInvalideItem")){
                    return new Object[][] {new Object[] { pair.getValue()}};
                }
            }
            else if (method.equalsIgnoreCase("all") || method.equalsIgnoreCase("getItemsTest") || method.equalsIgnoreCase("getItemByIdTest") ||
                    method.equalsIgnoreCase("getItemByInvalidIdTest") || method.equalsIgnoreCase("updateItemTest") ||
                    method.equalsIgnoreCase("updateInvalidDataTest")) {
                if (pair.getKey().equalsIgnoreCase("create1") || pair.getKey().equalsIgnoreCase("create2") ||
                        pair.getKey().equalsIgnoreCase("create3")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if ( method.equalsIgnoreCase("getItemByInvalidIdTest") &&
                        pair.getKey().equalsIgnoreCase("getItemByInvalidatedId")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if (method.equalsIgnoreCase("updateItemTest") &&
                        pair.getKey().equalsIgnoreCase("updateItem")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if (method.equalsIgnoreCase("updateInvalidDataTest") &&
                        pair.getKey().equalsIgnoreCase("updateInvalidDate")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if (method.equalsIgnoreCase("getItemsTest") &&
                        pair.getKey().equalsIgnoreCase("getItems")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
                if (method.equalsIgnoreCase("getItemByIdTest") &&
                        pair.getKey().equalsIgnoreCase("getItemById")){
                    mapCreate.put(pair.getKey(), pair.getValue());
                }
            }
        }
        return new Object[][] {new Object[] { mapCreate}};
    }



    public static Map<String, List<?>> getDataFormXlsFile(){
        File file = new File(fileXls);
        map = new HashMap<>();
        if (file.exists() && file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                List<Object> listDataTest;
                hSSFWorkbook = new HSSFWorkbook(fis);
                Sheet sheet = hSSFWorkbook.getSheetAt(0);
                Iterator<Row> rows = sheet.iterator();
                if (rows.hasNext()) rows.next();
                while (rows.hasNext()) {
                    HSSFRow row = (HSSFRow )rows.next();
                    String s = null;
                    //System.out.println(row.getRowNum());
                    HSSFCell cellVerb = row.getCell(VERB_COLUMN);
                    HSSFCell cellStatusCod = row.getCell(STATUS_COD_COLUMN);
                    HSSFCell cellId = row.getCell(ID_COLUMN);
                    HSSFCell cellItem = row.getCell(ITEM_COLUMN);
                    HSSFCell cellState = row.getCell(STATE_COLUMN);
                    HSSFCell cellPoint = row.getCell(POINT_COLUMN);
                    HSSFCell cellDateItem = row.getCell(DATAITEM_COLUMN);
                    HSSFCell cellMessage = row.getCell(MESSAGE_COLUMN);
                    HSSFCell cellStatus = row.getCell(SATUS_COLUMN);
                    HSSFCell cellMethod = row.getCell(METHOD_COLUMN);
                    //int nr_row = row.getRowNum();
                    Iterator<Cell> cells = row.iterator();
                    while (cells.hasNext()) {Cell cell = cells.next();}
                    if (cellVerb != null) {
                        listDataTest = new ArrayList<>();
                        String verb = cellVerb.getStringCellValue();
                        if (!verb.equalsIgnoreCase("") && verb != null) {
                            String method = "get", point = "/todo/items", dateItem = "", message = "", status = "", item="";
                            if (cellMethod != null) method = cellMethod.getStringCellValue();
                            // System.out.println("cellStatusCod - " + String.valueOf(cellStatusCod.getNumericCellValue()));
                            int statusCod = 200;
                            //System.out.println("cellStatusCod:" + cellStatusCod.getNumericCellValue());
                            if (cellStatusCod != null) statusCod = (int)(cellStatusCod.getNumericCellValue());
                            //int statusCod = 0;
                            //System.out.println("getNumericCellValue:" + cellId.getNumericCellValue());
                            if (cellPoint != null) point = cellPoint.getStringCellValue();
                            int id = 0;
                            if (cellId != null) id = (int)(cellId.getNumericCellValue());
                            if (cellItem != null) item = cellItem.getStringCellValue();
                            boolean state = false;
                            if (cellState != null) state = Boolean.valueOf(cellState.getStringCellValue());
                            // System.out.println("cellDateItem:" + cellDateItem.getStringCellValue());
                            if(cellDateItem != null) dateItem = cellDateItem.getStringCellValue();
                            if (cellMessage != null) message = cellMessage.getStringCellValue();
                            if (cellStatus != null) status = cellStatus.getStringCellValue();
                            if (!verb.equalsIgnoreCase("")){
                                if (method.equalsIgnoreCase("")) method = "get";
                                if (statusCod == 0) statusCod = 200;
                                Task task = null;
                                if (!item.equalsIgnoreCase("")){
                                    task = new Task(item, state);
                                }
                                ResponseS repon = new ResponseS(message, status);
                                listDataTest.add(method);
                                listDataTest.add(statusCod);
                                listDataTest.add(point);
                                listDataTest.add(task);
                                listDataTest.add(repon);
                                map.put(verb, listDataTest);
                            }
                        }

                    }
                }
                fis.close();
            } catch (FileNotFoundException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            return map;
        }
        System.out.println("No file");
        return null;
    }
}
