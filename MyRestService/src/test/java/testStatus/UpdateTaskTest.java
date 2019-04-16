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
import java.util.Map;

public class UpdateTaskTest extends BaseTest {
    public UpdateTaskTest(){}

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public void updateItemTest(Map<String, List<?>> mapCreate) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("updateItemTest");
        List<Task> list;

        //showMap(mapCreate);
        log.info("CREATE 3 tasks");
        list = getAllTasks(mapCreate, log);
        if (list.size() != 0) {log.info("GET size of list task = " + list.size());}
        Task task = list.get(list.size() - 1);
        task.setItem("New object for update");
        int id = createNewTask(task, log);
        if (id == 0) log.info("CREATED id="+id);
        task.setItem(task.getItem() + " UPDATE OKKK");
        task.setId(id);
        String json = (converTaskJsonXstream(task));
        //System.out.println(json);
        log.info("UPDATE task id=" + id);
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).put("/todo/items/"+id);
        int response_cod = resp.getStatusCode();
        List<?> list1;
        int status_cod = 0;
        if (mapCreate.containsKey("updateItem")) {
            list1 = mapCreate.get("updateItem");
            status_cod = (Integer)list1.get(1);
        }
        log.info("ASSERT response_cod:" + response_cod + " = = status_cod:" + status_cod);
        Assert.assertEquals(response_cod, status_cod);
        Task new_task = getTaskId(task.getId());
        int i = 0;
        if (new_task.getId() ==  task.getId() && new_task.getItem().equalsIgnoreCase( task.getItem())) i =1;
        setAssertTest(i, log);
        log.info("ASSERT update id=" + new_task.getId() + " == expected id="+task.getId());
        log.info("ASSERT update item=" + new_task.getItem() + " == expected item="+task.getItem());
        Assert.assertEquals(new_task.getId(), task.getId());
        Assert.assertEquals(new_task.getItem(), task.getItem());


    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public void updateInvalideIdItemTest(List<?> list) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("updateInvalideIdItemTest");
        int expected_cod = (Integer)list.get(1);
        ResponseS expectedResponseS = (ResponseS)list.get(4);
        List<Task> all_list = getAllTasks(null, log);
        int id = 0;
        if (all_list.size() != 0) {
            id = all_list.get(all_list.size()-1).getId();
        }
        id += 100;
        log.info("UPDATE id=" + id);
        //System.out.println("_______________________"+id);
        JSONObject obj = new JSONObject();
        obj.put("item", "task data");
        obj.put("state", "false");
        //System.out.println(obj.toJSONString());
        String json = obj.toJSONString();
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).put("/todo/items/"+id);
        int response_cod = resp.getStatusCode();
        String str_response = (resp.asString());
        ResponseS responseS = convertJacksonTask(str_response, ResponseS.class);
        int i = 0;
        if (response_cod ==  expected_cod && responseS.getMessage().equalsIgnoreCase(expectedResponseS.getMessage()+id)) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod="+response_cod + "  = = expected_cod="+expected_cod);
        log.info("ASSERT response_message="+responseS.getMessage() + "  = = expected_message="+expectedResponseS.getMessage()+id);
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(responseS.getMessage(), expectedResponseS.getMessage()+id);
    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public void updateInvalidDataTest(Map<String, List<?>> mapCreate) {
        Logger log = getLog();
        //Logger log = Logger.getLogger("updateInvalidDataTest");
        List<Task> list;

       // list = getAllTasks(mapCreate, log);
       // int id = list.get(list.size()-1).getId();
        int id = 0;
        List<?> list1;
        int expected_cod = 0;
        ResponseS expectResponse = null;
        if (mapCreate.containsKey("updateInvalidDate")) {
            list1 = mapCreate.get("updateInvalidDate");
            expected_cod = (Integer)list1.get(1);
            Task task = (Task)list1.get(3);
            expectResponse = (ResponseS) list1.get(4);
            id = createNewTask(task, log);
            //String str_response = (resp.asString());
           // ResponseS responseS = convertJacksonTask(str_response, ResponseS.class);
           // id = Integer.parseInt(responseS.getStatus());
            //log.info("CREATE new task id=" + id);
        }
        JSONObject obj = new JSONObject();
        obj.put("item", "task data");
        obj.put("state", "falsse");
        //System.out.println(obj.toJSONString());
        String json = obj.toJSONString();
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.body(json).put("/todo/items/"+id);
        int response_cod = resp.getStatusCode();
        String str_response = (resp.asString());
        ResponseS responseS = convertJacksonTask(str_response, ResponseS.class);
        int i = 0;
        if (response_cod ==  expected_cod) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod="+response_cod+" = = expected_cod="+expected_cod);
        Assert.assertEquals(response_cod, expected_cod);
       // Assert.assertEquals(responseS.getMessage(), expectResponse.getMessage());

    }
}
