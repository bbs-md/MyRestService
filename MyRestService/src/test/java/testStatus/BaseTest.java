package test.java.testStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.loc.service.controllers.ResponseS;
import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class BaseTest {
    static Logger log = Logger.getLogger(BaseTest.class);
    private SimpleDateFormat sdf = new SimpleDateFormat();//"yyyy-MM-dd"
    private static int nr=1;

    public BaseTest(){
        //BaseTest::get
        //PropertyConfigurator.configure("src/test/resources/log4j.properties");//cconfiguration log4j
       // PropertyConfigurator.configure("src/test/resources/log4j2.xml");//cconfiguration log4j
    }

    public Logger getLog(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String methodName = stackTrace[2].getMethodName();
        return Logger.getLogger(methodName);
    }

    public int createNewTask(Task task, Logger log){
        Response resp = null;
        int newId = 0;
        log.info("CREATE create new Task{"+task.getItem()+" : "+task.getState()+"}");
        try{
            String json = (converTaskJsonXstream(task));
            RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
            resp = request.body(json).post("/todo/items");
            String str_response = (resp.asString());
            int response_cod = resp.getStatusCode();
            ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
            newId = Integer.parseInt(repsonse_serv.getStatus());
            log.info("CREATE new task ID=" + newId + " | response_cod:"+response_cod + " | res_mes:"+repsonse_serv.getMessage() + " | mes_cod:+"+repsonse_serv.getStatus());
        }
        catch (Exception e){log.error(e.getMessage());}
        return newId;
    }

    protected void setLogBefore(String metod, Logger log){
        log.info(nr + " - STARTING TESTING method " + metod + "() | time:" + getTime() + " | " + showInfoThread());
    }

    protected void setLogAfter(String metod) {
        Logger log = Logger.getLogger(metod);
        log.info("E_N_D TESTING method " + metod + "() | time:" + getTime() + " | " + showInfoThread());
    }

    protected String getTime(){//get curent datetime
        return sdf.format(new Date());
    }

    protected void setAssertTest(int i, Logger log){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String methhod = stackTrace[2].getMethodName();
        if (i == 1) log.info(" ** method " + methhod + "() __ PASSED");
        else log.info(" ** method " + methhod + "() __ FAILED");
    }

    public void getSrevice(Logger log){
        log.info("WAKE UP server");
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.get("/todo/items");
        String resp_mes = null;
        ResponseS repsonse_serv = null;
        log.info(" ***************  response StatusCode:" + resp.getStatusCode());
        int ms = 100;
        if (resp.getStatusCode() != 200){
            resp_mes = resp.asString();
            repsonse_serv = convertJacksonTask(resp_mes, ResponseS.class);
            //log.info("WAKE response:"+resp_mes);
        }


        if (resp.getStatusCode() != 200 && resp.getStatusCode() != 404){

            int i = 0;
            while (i < 40){
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("INFO get request current response code:"+resp.getStatusCode());
                if (resp.getStatusCode() == 500 || resp.getStatusCode() == 400){
                    log.info("WAKE UP AGAIN nr:" + i);
                    request = RestAssured.given().request().header("Content-Type", "application/json");
                    resp  = request.get("/todo/items");
                    log.info(" ***************  response StatusCode:" + resp.getStatusCode() + " nr:"+i);
                    if (resp.getStatusCode() != 200) log.info("response:" + resp.asString());
                    resp_mes = resp.asString();
                    repsonse_serv = convertJacksonTask(resp_mes, ResponseS.class);
                }
                if (resp.getStatusCode() == 200 || resp.getStatusCode() == 404){
                    log.info(" OK  ************  response StatusCode:" + resp.getStatusCode()+" nr:"+i);
                    if (resp.getStatusCode() == 404)  log.info(" ERROR 404  ************  response :" + resp.asString()+" nr:"+i);
                    break;
                }
                i++;
                if (ms > 20) ms += 100;
                else if (ms > 30) ms += 100;

            }
            /*try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(" ***************  response StatusCode:" + resp.getStatusCode());
            if (resp.getStatusCode() == 404 || resp.getStatusCode() == 500){
                log.info("WAKE UP AGAIN");
                request = RestAssured.given().request().header("Content-Type", "application/json");
                resp  = request.get("/todo/items");
                log.info(" ***************  response StatusCode:" + resp.getStatusCode());
            }
            try {
                Thread.sleep(1800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resp.getStatusCode() == 404 || resp.getStatusCode() == 500){
                log.info("WAKE UP SECOND TIME ");
                request = RestAssured.given().request().header("Content-Type", "application/json");
                resp  = request.get("/todo/items");
                log.info(" ***************  response StatusCode:" + resp.getStatusCode());
            }
            try {
                Thread.sleep(2200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resp.getStatusCode() == 404 || resp.getStatusCode() == 500){
                log.info("WAKE UP LAST _____________________ TIME ");
                request = RestAssured.given().request().header("Content-Type", "application/json");
                resp  = request.get("/todo/items");
                log.info(" ***************  response StatusCode:" + resp.getStatusCode());
            }*/
        }

    }

    public static List<Task> convertTaskJackson(String jsonStr){

        List<Task> tasks = new ArrayList();
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            tasks =  mapper.readValue(jsonStr, new TypeReference<List<Task>>(){});
        }
        catch(Exception ex){
            System.out.println("Error while converting JSON string to List<Task> object.");
            ex.printStackTrace();
        }
        return tasks;
    }

    @BeforeClass
    public void setBaseUri () {
        //RestAssured.baseURI = "http://192.168.16.100:8080";
       // getAllTasks(null, log);
        //Logger log.info("__________________________NEW___CLASS_______________________________________");
    }

    @AfterClass
    public void setAfterClass () {
        //log.info("__________________________END___CLASS_______________________________________");
    }

    @BeforeSuite
    public void beforeSuit(){
        RestAssured.baseURI = "http://192.168.16.100:8080";
        getSrevice(log);
    }

    @AfterSuite
    public void afterSuite(){

        GenerateReport rt = new GenerateReport("target/surefire-reports-surefire/Suite1/Second.html");
        rt.writeRaport();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        RestAssured.baseURI = "http://192.168.16.100:8080";
        Logger log = Logger.getLogger(method.getName());
        //getSrevice(log);
       // System.out.println("CLASS:"+this.getClass());
        setLogBefore(method.getName(), log);
        long id = Thread.currentThread().getId();
        System.out.println("Before test-method. Thread id is: " + id);
        nr++;
    }

    @AfterMethod
    public void afterMethod(Method method) {
        setLogAfter(method.getName());
    }

    public String showInfoThread() {
        long id = Thread.currentThread().getId();
        return ("Thread id is: " + id);
    }

    public static <T> T convertJacksonTask(String jsonStr, Class c){
        T task = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            task =  (T)mapper.readValue(jsonStr, c);
        }
        catch(Exception ex){
            System.out.println("Error while converting JSON string to Task object.");
            ex.printStackTrace();
        }
        return task;
    }

    public static String convertTaskJackson(Task task){
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/test/resources/task.json"), Task.class);
            json = mapper.writeValueAsString(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String converTaskJsonXstream(Task task){
        Class<?>[] classes = new Class[] {Task.class};
        //XStream xstream  = new XStream();
        // XStream.setupDefaultSecurity(xstream);
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer writer) {
                return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
            }
        });
         xstream.setMode(XStream.NO_REFERENCES);
       // XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        xstream.processAnnotations(Task.class);
        return (xstream.toXML(task));
    }

    public void printInfoTest(int status_cod, int response_cod, ResponseS responseS){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String method = stackTrace[2].getMethodName();
        System.out.println("_________@Test: " + method + " status_cod:"+status_cod+ "() | response code:" + response_cod);
        if (responseS != null) System.out.println("response message:" + responseS.getMessage());
        if (responseS != null) System.out.println("response status:" + responseS.getStatus());
        long id = Thread.currentThread().getId();
        System.out.println("_________Thread id is: " + id);
    }

    public boolean createThreeItem(Map<String, List<?>> mapCreate, Logger log){
        if (mapCreate.size() > 0){
            Iterator<Map.Entry<String, List<?>>> it = mapCreate.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, List<?>> pair = it.next();
                if (pair.getKey().equalsIgnoreCase("create1")){//|| pair.getKey().equalsIgnoreCase("create2"
                    if (!createNewItem(pair.getValue(), log)) {
                        if (!createNewItem(pair.getValue(), log)) return false;
                    }
                }
            }
            log.info("three Items are created");
            return true;
        }
        else return false;
    }

    public void showMap(Map<String, List<?>> map){
        Iterator<Map.Entry<String, List<?>>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, List<?>> pair = it.next();
            System.out.println("_____________________________________________");
            System.out.println("map_key:" + pair.getKey());
            List<?> list = pair.getValue();
            for (Object o: list) {
                System.out.println(o);
            }


            //System.out.println("map_value:" + list.get(2));
        }
    }



    public boolean createNewItem(List<?> list, Logger log){

        int status_cod = (Integer)list.get(1);
        Task task = (Task) list.get(3);
        ResponseS responseS = (ResponseS)list.get(4);

        String json = (converTaskJsonXstream(task));
        log.info("CREATE new task:" + task);
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).post("/todo/items");

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        log.info("new task response_cod:" + response_cod);
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
        int indexOf = repsonse_serv.getMessage().indexOf(task.getItem());
        //printInfoTest("create NEW Item", status_cod, response_cod, repsonse_serv);
        if (response_cod == status_cod && indexOf != -1) return true;
        else return false;
    }

    public List<Task> getAllTasks(Map<String, List<?>> mapCreate, Logger log){
        List<Task> list = new ArrayList();
        try{
            RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
            io.restassured.response.Response resp  = request.get("/todo/items");
            String str_response = (resp.asString());
            int response_cod = resp.getStatusCode();
            if (response_cod != 404){
                //System.out.println("response_cod:"+response_cod);
                list =  convertTaskJackson(str_response);
            }
            else {
                if (mapCreate == null) {

                    Object[][] mapCreate1 = MyDataProvider.getDataFromXlsForAll();
                    mapCreate = (HashMap)mapCreate1[0][0];
                    System.out.println("*****************************************************************");
                    showMap(mapCreate);
                }
                if (mapCreate != null) {
                    System.out.println("#################################################################");
                    createThreeItem(mapCreate, log);
                    return getAllTasks(null, log);
                }
                else return list;
            }
        }
        catch (Exception e) {
           // log.error(e.getLocalizedMessage());
        }
        return list;
    }

    public Task getTaskId(int id){
        Task task = new Task();
        try{
            RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
            io.restassured.response.Response resp  = request.get("/todo/items/" + id);
            String str_response = (resp.asString());
            //System.out.println("str_response ****************"+str_response);
            int response_cod = resp.getStatusCode();
            if (response_cod == 200){
                //System.out.println("response_cod:"+response_cod);
                task =  convertJacksonTask(str_response, Task.class);
                //System.out.println(task);
            }
        }
        catch (Exception e) {
            //log.error(e.getLocalizedMessage());
        }
        return task;
    }


}
