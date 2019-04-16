package main.java.loc.service.controllers;

import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ServController {
    static Logger log = Logger.getLogger(ServController.class);
    private List<Task> tasks = new ArrayList();
    private AtomicLong nextId = new AtomicLong();
    private SimpleDateFormat sdf = new SimpleDateFormat();//"yyyy-MM-dd"
    //private ResponseS r;
    //private StoreItems si;
    private ManageTask mt;

    public ServController(){
        //si = new StoreItems();
        mt = new ManageTask();
        //tasks = si.getAll(); // retrieve from file_source
        //tasks = mt.listTasks();//retrieve from DB restful_service `task` table
    }

    @PostMapping("/todo/items")// ADD new item
    public ResponseEntity<?> creatNewTask(@RequestBody Task t){
        ResponseS r = new ResponseS();
        HttpStatus status = HttpStatus.CREATED;
        try{
            if (t != null && t.getItem() != null && !t.getItem().equalsIgnoreCase("") && !t.getItem().equalsIgnoreCase("null")){
                //t.setId(nextId.incrementAndGet());
                //String test = t.getItem();
                System.out.println("item:" + t.getItem()+"|");
                t.setDateItem(getTime());
                mt.addTask(t);
                if (t.getId() > 0){
                    //si.saveItemsList(tasks);
                    r.setMessage("id:"+t.getId()+", task:"+t.getItem()+", state:" + t.getState() + ", datetime:"+t.getDateItem());
                    log.info("new Task is created with id:"+t.getId());
                    tasks.add(t);
                }
                else{
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                    r.setMessage("Error create Item, datetime:"+t.getDateItem());
                    log.info("error created task with id:"+t.getId());
                }
            }
            else {
                r.setMessage("object is null");
                status = HttpStatus.BAD_REQUEST;
            }
        }
        catch (Exception e){
            log.warn(e.getMessage());
            r.setMessage(e.getMessage()+" | datetime:"+getTime());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            //new ResponseEntity(e.getMessage(), status);
        }
        r.setStatus(String.valueOf(t.getId()));
        return new ResponseEntity(r, status);
    }

    @GetMapping("/hello")//for testing
    public  ResponseEntity<?> getHelloMessage(){
        return new ResponseEntity(new ResponseS("Hello, this is RESTful web service, you can put to and retrieve from here you daily tak", HttpStatus.OK.toString()), HttpStatus.OK);
    }

    @GetMapping("/todo/items")//get ALL tasks
    public ResponseEntity<?> getAllTasks(){
        HttpStatus status = HttpStatus.OK;
        //ResponseS r = new ResponseS();

        try{
            tasks = mt.listTasks();
            if (tasks.size() == 0) {
                log.warn("List is empty");
                status = HttpStatus.NOT_FOUND;
                return new ResponseEntity(new ResponseS("the lists of tasks is empty", status.toString()), status);
            }
        }
        catch(Exception e){
            log.warn("get ALL tasks: " + e.getMessage());
            status = HttpStatus.NOT_FOUND;
            new ResponseEntity(new ResponseS("get ALL tasks: " + e.getMessage(), status.toString()), status);
        }
        return new ResponseEntity(tasks, status);
    }

    @GetMapping("/todo/items/done")//get  tasks done
    public ResponseEntity<?> getTasksDone(){
        HttpStatus status = HttpStatus.OK;
        List<Task> d = new ArrayList();
        ResponseS r = new ResponseS();
        try{
            tasks = mt.listTasks();
            for (Task t: tasks) {if (t.getState() == true) {d.add(t);}}
            if (d.size() == 0) {
                log.warn("List `done` is empty");
                status = HttpStatus.NOT_FOUND;
                r.setMessage("List `done` is empty");
                r.setStatus(status.toString());
                return new ResponseEntity(r, status);
            }
        }
        catch(Exception e){
            status =HttpStatus.NOT_FOUND;
            return new ResponseEntity(new ResponseS(e.getMessage(), status.toString()), status);
        }
        return new ResponseEntity(d, status);
    }

    @RequestMapping(value="/todo/items/{id}", method=GET)
    //*@GetMapping("/todo/items/{id}")//get ALL tasks
    public ResponseEntity<?> getIdItem(@PathVariable("id") int id){
        HttpStatus status = HttpStatus.OK;
        Task task = mt.getTaksById(id);
        if (task == null) {
            log.warn("Item with id:"+id + " was not found");
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity(new ResponseS("Item with id:"+id + " was not found", String.valueOf(id)), status);
        }
        return new ResponseEntity(task, status);
    }

    @PutMapping("/todo/items/{id}")//Change item by id
    public ResponseEntity<?> updateItem(@PathVariable("id") int id, @RequestBody Task new_t){
        HttpStatus status = HttpStatus.OK;
        ResponseS r = new ResponseS();
        try{
            tasks = mt.listTasks();
            if (tasks.size() != 0 && (!new_t.getItem().equalsIgnoreCase("") || new_t.getItem() != null)){
                mt.updateTask(id, new_t.getItem(), new_t.getState(), getTime());
                r.setMessage("item succesfully update, id-"+id+" | date:"+getTime());
                log.info("object task with id:" + id+ " was update succesufuly");
            }
            else {
                r.setMessage("No data | date:"+getTime());
                status = HttpStatus.NOT_FOUND;
            }
        }
        catch(Exception e) {
            status = HttpStatus.NOT_FOUND;
            log.warn(e.getMessage());
            r.setMessage(e.getMessage());
            if (r.getMessage() == null) r.setMessage("Error update item with id:"+id);
        }
        r.setStatus(status.toString());
        return new ResponseEntity(r, status);
    }

    @DeleteMapping("/todo/items")//DELETE file
    public ResponseEntity<?> deleteSource(){
        ResponseS r = new ResponseS();
        HttpStatus status = HttpStatus.OK;
        try {
            mt.deleteAllTask();
        } catch (Exception e) {
            log.warn(e.getMessage());
            status = HttpStatus.NOT_FOUND;
            r.setMessage(e.getMessage());
        }
        log.info("list of tasks was deleted");
        r.setMessage("table was truncated");
        r.setStatus(status.toString());
        tasks = null;
        return new ResponseEntity(r, status);
    }

    @DeleteMapping("/todo/items/{id}")//DELETE item by id
    public ResponseEntity<?> deleteItem(@PathVariable("id") int id){
        HttpStatus status = HttpStatus.OK;
        ResponseS r = new ResponseS();
        try{

            mt.deleteTask(id);
            r.setMessage("item succesfully deleted, id-"+id+" | date:"+getTime());
            r.setStatus(String.valueOf(id));
        }
        catch(Exception e){
            log.warn(e.getMessage());
            status = HttpStatus.NOT_FOUND;
            r.setMessage(e.getMessage());
            r.setStatus(HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity(r, status);
    }

    private String getTime(){//get curent datetime
        return sdf.format(new Date());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void badIdException(){

    }
}