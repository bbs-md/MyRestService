package test.java.testStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.loc.service.controllers.ResponseS;
import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DeleteItemTest extends BaseTest  {

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public void deleteItemTest(List<?> list){
        Logger log = getLog();
        //Logger log = Logger.getLogger("deleteItemTest");
        //List<Task> allList = new ArrayList();
        int expected_cod = (Integer)list.get(1);
        Task task = (Task)list.get(3);
        log.info("Task // "+task);
        int id = createNewTask(task, log);
        if (id == 0) id = createNewTask(task, log);
        //allList = getAllTasks(null, log);
        //int id = allList.get(allList.size()-1).getId();
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.delete("/todo/items/"+id);
        String str_response = (resp.asString());
        ResponseS responseS = convertJacksonTask(str_response, ResponseS.class);
        int response_cod = resp.getStatusCode();
        int i = 0;
        if (response_cod == expected_cod && Integer.parseInt(responseS.getStatus()) == id) i =1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod="+response_cod+" = = expected_cod="+expected_cod);
        log.info("ASSERT response_messag_cod="+Integer.parseInt(responseS.getStatus())+" = = expected_message_cod="+id);
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(Integer.parseInt(responseS.getStatus()), id);

    }

    @Test(dataProvider = "dataXls", dataProviderClass = MyDataProvider.class, retryAnalyzer = MyRetry.class, groups = {"a"})
    public void deleteInvalideItemTest(List<?> list){
        Logger log = getLog();
        //Logger log = Logger.getLogger("deleteInvalideItemTest");
        //List<Task> allList = new ArrayList();
        int expected_cod = (Integer)list.get(1);
        Task task = (Task)list.get(3);
        int id = createNewTask(task, log);
        if (id == 0) id = createNewTask(task, log);
        log.info("Task // "+task);
        ResponseS expectResponse = (ResponseS)list.get(4);
        //allList = getAllTasks(null, log);
        //int id = allList.get(allList.size()-1).getId();

        log.info("INFO last id=" + id + " // new id=" + (id + 100));
        id += 100;
        RequestSpecification request = RestAssured.given().request().header("Content-Type", "application/json");
        Response resp  = request.delete("/todo/items/"+id);
        String str_response = (resp.asString());
        ResponseS responseS = convertJacksonTask(str_response, ResponseS.class);
        int response_cod = resp.getStatusCode();
        int i = 0;
        if (response_cod == expected_cod && responseS.getMessage().equalsIgnoreCase(expectResponse.getMessage())) i = 1;
        setAssertTest(i, log);
        log.info("ASSERT response_cod=" + response_cod+" = = status_cod=" + expected_cod);
        log.info("ASSERT response_message=" + responseS.getMessage()+" = = ecpected_message=" + expectResponse.getMessage());
        Assert.assertEquals(response_cod, expected_cod);
        Assert.assertEquals(responseS.getMessage(), expectResponse.getMessage());

    }
/*
    @Test()
    public void getAllTest() {
        System.out.println("1111111111111111111111111111");
        List<Task> list1 = new ArrayList();
        list1 = getAllTasks(null);
        System.out.println("test");
        for (Task task: list1) {
            System.out.println(task);
        }
    }*/

}
