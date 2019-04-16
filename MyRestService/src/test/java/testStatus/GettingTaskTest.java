package test.java.testStatus;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import main.java.loc.service.controllers.ResponseS;
import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GettingTaskTest extends BaseTest {
    public GettingTaskTest(){}

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class,  groups={"b"}, dependsOnGroups = { "a" })
    public  void getEmptyListItemsTest(List<?> list) {
        Logger log = getLog();
        int expected_cod = (Integer)list.get(1);
        ResponseS expectedResponseS = (ResponseS)list.get(4);
       //System.out.println("status_cod:"+status_cod);
       //System.out.println("expectedResponseS:" + expectedResponseS);
        log.info("DELETE list of task");
        if (!clearItemsList()){if (!clearItemsList()) return;}//clean Items list
        log.info("TEST expected message 'the lists of tasks is empy'");
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.get("/todo/items");

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);

         printInfoTest(expected_cod, response_cod, repsonse_serv);
        int i = 0;
        if (response_cod == expected_cod && expectedResponseS.getMessage().equalsIgnoreCase(repsonse_serv.getMessage())) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod:"+response_cod+" = = expected_cod:"+expected_cod);
        log.info("ASSERT expectedResponse:"+repsonse_serv.getMessage()+" = = expectedResponse:"+expectedResponseS.getMessage());
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(repsonse_serv.getMessage(), expectedResponseS.getMessage());
    }

    public boolean clearItemsList(){
        RequestSpecification request = RestAssured.given().request()
                .header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.delete("/todo/items");
        String str_response = (resp.asString());
        ResponseS response_serv = convertJacksonTask(str_response, ResponseS.class);
        int response_cod = resp.getStatusCode();
       // System.out.println("serv mes DELETE:" + response_serv.getMessage());
        if (response_cod == 200 && response_serv.getMessage().equalsIgnoreCase("table was truncated")) return true;
        else return false;
    }
/*
    public boolean createNewItem(List<?> list){
        int status_cod = (Integer)list.get(1);
        Task task = (Task) list.get(3);
        ResponseS responseS = (ResponseS)list.get(4);

        String json = (converTaskJsonXstream(task));
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).post("/todo/item");

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
        int indexOf = repsonse_serv.getMessage().indexOf(task.getItem());
        //printInfoTest("create NEW Item", status_cod, response_cod, repsonse_serv);
        if (response_cod == status_cod && indexOf != -1) return true;
        else return false;
    }

    public boolean createThreeItem(Map<String, List<?>> mapCreate){
        if (mapCreate.size() > 0){
            Iterator<Map.Entry<String, List<?>>> it = mapCreate.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, List<?>> pair = it.next();
                if (pair.getKey().equalsIgnoreCase("create1") || pair.getKey().equalsIgnoreCase("create2")
                 || pair.getKey().equalsIgnoreCase("create3")){
                    if (!createNewItem(pair.getValue())) {
                        if (!createNewItem(pair.getValue())) return false;
                    }
                }
            }
            System.out.println("three Items are created");
            return true;
        }
        else return false;
    }*/

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void getItemsTest(Map<String, List<?>> mapCreate) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("getItemsTest");
        createThreeItem(mapCreate, log);
        int expected_cod = 0;
        if (mapCreate.containsKey("getItems")) expected_cod = (Integer) mapCreate.get("getItems").get(1);
        log.info("TEST getItemsTest() // expected lisk of tasks");
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.get("/todo/items");

        String str_response = (resp.asString());
        // System.out.println(str_response);
        int response_cod = resp.getStatusCode();
        // ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
        List<Task> list =  convertTaskJackson(str_response);
        log.info("GET list, size:"+list.size());
        //log.info();
        //if (response_cod == 404) {if (!createThreeItem(mapCreate)) return;}
        ResponseS responseS = new ResponseS("were created "+  String.valueOf(list.size()) + " Items", "");
        printInfoTest(expected_cod, response_cod, responseS);
        int i = 0;
        if (response_cod == expected_cod && list.size() != 0) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod:"+response_cod+" = = expected_cod:"+expected_cod);
        log.info("ASSERT expected recieved list of tasks, size not to equal 0:");
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertFalse(list.size() == 0);
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void getItemByIdTest(Map<String, List<?>> mapCreate) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("getItemByIdTest");
        //List<Task> all_list = getAllTasks(null);
       // if (all_list == null || all_list.size() == 0){if (!createThreeItem(mapCreate)) return;}
       // all_list = getAllTasks(null);
        int expected_cod = 0;
        Task task = null;
        int id = 0;
        if (mapCreate.containsKey("getItemById")) {
            expected_cod = (Integer) mapCreate.get("getItemById").get(1);
            task = (Task) mapCreate.get("getItemById").get(3);

            id = createNewTask(task, log);
            //String json = (converTaskJsonXstream(task));
            //RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
            //Response resp  = request.body(json).post("/todo/items");


            //String str_response = (resp.asString());
            //int response_cod = resp.getStatusCode();
            //ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
            //log.info("RESULT creating new task:" + repsonse_serv.getMessage() + " | cod:" + repsonse_serv.getStatus());
           // log.info("STATUS COD:"+resp.getStatusCode());
           // id = Integer.parseInt(repsonse_serv.getStatus());


            /*
            Response resp = createNewTask(task, log);
            String str_response = (resp.asString());
            ResponseS repsonse_serv_create = convertJacksonTask(str_response, ResponseS.class);
            id = Integer.parseInt(repsonse_serv_create.getStatus());*/
        }


        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.get("/todo/items/" + id);//get Item with id=2

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();

        //int id = Integer.parseInt(repsonse_serv.getStatus());
        Task task1 = convertJacksonTask(str_response, Task.class);
        int i = 0;
        if (response_cod == expected_cod && task1.getId() == id) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod:"+response_cod+" = = expected_cod:"+expected_cod);
        if (response_cod == 404){
            ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
            log.info("ERROR response_cod:" + response_cod + "|  mess:"+repsonse_serv.getMessage());
        }
        log.info("ASSERT get item:" + task1.getItem() + " = = expected ietm:" + task.getItem());
        log.info("ASSERT get status:" + task1.getState() + " = = expected ietm:" + task.getState());
        log.info("ASSERT get id:" + task1.getId() + " = = expected id:" + id);
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(task1.getItem(), task.getItem());
        Assert.assertEquals(task1.getState(), task.getState());
        Assert.assertEquals(task1.getId(), id);
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, groups = {"a"})
    public  void getItemByInvalidIdTest(Map<String, List<?>> mapCreate) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("getItemByInvalidIdTest");
        List<Task> all_list = getAllTasks(null, log);
        if (all_list == null || all_list.size() == 0){if (!createThreeItem(mapCreate, log)) return;}

        all_list = getAllTasks(null, log);
        log.info("GET get list of tasks, size:"+all_list.size());
        int id = all_list.get(all_list.size()-1).getId();
        log.info("SET id="+(id + 100)+", last id is " + id);
        id += 100;
        //log.info("GET inexisting id");
        int expected_cod = 0;
        ResponseS responseS = null;
        if (mapCreate.containsKey("getItemByInvalidatedId")) {
            List<?> list = mapCreate.get("getItemByInvalidatedId");
            expected_cod = (Integer) list.get(1);
            responseS = (ResponseS) list.get(4);
        }
        if (expected_cod == 0) log.info("ERROR getItemByInvalidIdTest() // expected_cod=0");
            //for (Object o: list) {System.out.println(o);}
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.get("/todo/items/"+id);//get Item with id=2

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        ResponseS serv_response = convertJacksonTask(str_response, ResponseS.class);
        printInfoTest(expected_cod, response_cod, serv_response);
        int i = 0;
        if (response_cod == expected_cod && id == Integer.parseInt(serv_response.getStatus())) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod:"+response_cod+" = = expected_cod:"+expected_cod);
        log.info("ASSERT get message_status:" + Integer.parseInt(serv_response.getStatus()) + " = = expected message_status:" + id);
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(Integer.parseInt(serv_response.getStatus()) ,id);
    }
}
