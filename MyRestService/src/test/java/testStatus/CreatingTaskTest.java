package test.java.testStatus;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.loc.service.controllers.ResponseS;
import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class CreatingTaskTest extends BaseTest {

    public CreatingTaskTest(){
        //System.setProperty("MyPath", "test");
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void createItemTest(List<?> list) {
        Logger log = getLog();
        int expected_cod = (Integer)list.get(1);
        Task task = (Task) list.get(3);
        ResponseS ExpectedResponse = (ResponseS)list.get(4);
        //Response resp = createNewTask1(task, log);
        String json = (converTaskJsonXstream(task));
        log.info("CREATE new task = " + json);
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).post("/todo/items");
        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        log.info("RESPONSE: " + str_response + " | response_cod:"+response_cod);
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
        if (response_cod == 500) {
            resp = createNewTask1(task, log);
            str_response = (resp.asString());
            repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
            log.info("CREATE attempt to create new task | response cod:"+response_cod+" == expected cod:" + expected_cod);
        }
        int indexOf = repsonse_serv.getMessage().indexOf(task.getItem());
        int i = 0;
        if (response_cod == expected_cod && indexOf != -1) i =1;
        setAssertTest(i, log);
        log.info("CREATE new task response cod:"+response_cod+" == expected cod:" + expected_cod + "____ end LOG");
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertFalse(indexOf == -1);
        int newId = Integer.parseInt(repsonse_serv.getStatus());
        if (newId < 1) log.info("ERROR in createItemTest(): new Id of Task is invalid");
        Task testTask = getTaskId(newId);
        log.info("createItemTest() - check received new Task");
        log.info("ASSERT new item:'"+testTask.getItem()+"' = = expected item:"+task.getItem());
        log.info("ASSERT new state:"+testTask.getState()+" = = expected state:"+task.getState());
        Assert.assertEquals(testTask.getItem(), task.getItem());
        Assert.assertEquals(testTask.getState(), task.getState());

        /*Gson gson = new Gson();
        String t = gson.toJson(task, Task.class);
        System.out.println("Gson" + t);*/
    }

    public Response createNewTask1(Task task, Logger log){
        Response resp = null;
        int newId = 0;
        log.info("CREATE create new Task{"+task.getItem()+" : "+task.getState()+"}");
        try{
            String json = (converTaskJsonXstream(task));
            RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
            resp = request.body(json).post("/todo/items");
            String str_response = (resp.asString());
            int response_cod = resp.getStatusCode();
            ResponseS response_serv = convertJacksonTask(str_response, ResponseS.class);
            newId = Integer.parseInt(response_serv.getStatus());
            log.info("CREATE new task ID=" + newId + " | response_cod:"+response_cod + " | res_mes:"+response_serv.getMessage() + " | mes_cod:+"+response_serv.getStatus());
            log.info("RESPONSE: "+str_response);
        }
        catch (Exception e){log.error(e.getMessage());}
        return resp;
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void createInvalidStateTest(List<?> list) {
        Logger log = getLog();
        int expected_cod = (Integer)list.get(1);
        Task task = (Task) list.get(3);
        ResponseS expectedResponseS = (ResponseS)list.get(4);
        JSONObject obj = new JSONObject();
        obj.put("items", task.getItem());
        obj.put("state", "fals");
        //System.out.println(obj.toJSONString());
        String json = obj.toJSONString();
        log.info("CREATE createInvalidStateTest() // create new Task with invalide state=flse(false=valide)");
        RequestSpecification request = RestAssured.given().request()
                .header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.body(json)
                .post("/todo/items");


        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);
        int indexOf = repsonse_serv.getMessage().indexOf(expectedResponseS.getMessage());

        //printInfoTest("createInvalidItemTest",status_cod, response_cod, repsonse_serv);
        log.info("ASSERT response_cod:"+response_cod+" = = status_cod:"+expected_cod);
        log.info("ASSERT There is '"+expectedResponseS.getMessage()+"' in answer response");
        int i = 0;
        if (response_cod == expected_cod && indexOf != -1) i =1;
        setAssertTest(i, log);
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertFalse(indexOf == -1);
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void createInvalidItemTest(List<?> list) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("createInvalidItemTest");
        int expected_cod = (Integer)list.get(1);
        Task task = (Task) list.get(3);
        ResponseS expectedResponseS = (ResponseS)list.get(4);
        //System.out.println("expectedResponseS" + list);
        //System.out.println("expectedResponseS" + expectedResponseS);
        JSONObject obj = new JSONObject();
        obj.put("items", task.getItem());
        obj.put("state", String.valueOf(task.getState()));
        //System.out.println(obj.toJSONString());
        String json = obj.toJSONString();
        log.info("CREATE create new Task with invalide parmeter of name=items(item=valide)");
        RequestSpecification request = RestAssured.given().request()
                .header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.body(json)
                .post("/todo/items");

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

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public  void createWithEmptyBodyTest(List<?> list) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("createWithEmptyBodyTest");
        int expected_cod = (Integer)list.get(1);
        ResponseS expectedResponseS = (ResponseS)list.get(4);
        log.info("CREATE createWithEmptyBodyTest() // create new Task with empty body");
        RequestSpecification request = RestAssured.given().request()
                .header("Content-Type", "application/json");
        io.restassured.response.Response resp  = request.body("{}")
                .post("/todo/items");

        String str_response = (resp.asString());
        int response_cod = resp.getStatusCode();
        ResponseS repsonse_serv = convertJacksonTask(str_response, ResponseS.class);

        // printInfoTest("createWithEmptyBodyTest", status_cod, response_cod, repsonse_serv);
        int i = 0;
        if (response_cod == expected_cod && expectedResponseS.getMessage().equalsIgnoreCase(repsonse_serv.getMessage())) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod:"+response_cod+" = = expected_cod:"+expected_cod);
        log.info("ASSERT response_message:"+repsonse_serv.getMessage()+" = = expected_message:"+expectedResponseS.getMessage());
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(repsonse_serv.getMessage(), expectedResponseS.getMessage());
    }

/*
    //@Parameters("Path_xls")
    public void testStatusCode () {
        //System.out.println();
        io.restassured.response.Response res =
                given()
                //.param ()
                //.param ("key", "Xyz")
                .when()
                .get ("/todo/items")
                .then()
                .contentType(ContentType.JSON)
                .extract().response();
        System.out.println(res.getBody().asString());
        System.out.println(res.asString());

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            //T = mapper.readValue(jsonStr, Student.class);
            System.out.println("List<>");
            List<Task> tasks = mapper.readValue(res.getBody().asString(), new TypeReference<List<Task>>(){});
            for (Task t: tasks) {
                System.out.println(t);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error while converting JSON string to Student object.");
            ex.printStackTrace();
        }
        System.out.println("convert");
        List<Task> tasks  = convertTaskJackson(res.getBody().asString());

        for (Task t: tasks) {
            System.out.println(t);
        }
        Assert.assertEquals (res.statusCode (), 200);
    }


    public void testStatusCodeRestAssured () {
        given ()
                //.param ("query", "restaurants in mumbai")
                //.param ("key", "Xyz")
                .when()
                .get ("todo/items/1")
                .then ()
                .assertThat ().statusCode (200);

    }
*/

}
