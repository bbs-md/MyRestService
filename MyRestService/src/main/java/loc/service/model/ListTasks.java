package main.java.loc.service.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("ListTasks")
public class ListTasks {
    @XStreamImplicit(itemFieldName="Task")
    public List<Task> tasks = new ArrayList();

    public void addTask(Task task){
        tasks.add(task);
    }
    public Task getTask(int i){
        return tasks.get(i);
    }

    public List<Task> getTasks() {return tasks;}
    public void setTasks(List<Task> tasks) {this.tasks = tasks;}
}
