package main.java.loc.service.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;

@XStreamAlias("Task")
public class Task implements Serializable {

    @XStreamOmitField()
    private int id;

    @XStreamAlias("item")
    private String item;

    @XStreamAlias("state")
    private boolean state;

    @XStreamOmitField
    private String dateItem;

    public Task(){}
    public Task(int id, String i){this(id, i, false);}
    public Task(int id, String i, boolean s){
        this.id = id;
        this.item = i;
        this.state = s;
    }
    public Task(String item, boolean state){
        this.item = item;
        this.state = state;
    }

    public String getItem() {return item;}
    public boolean getState() {return state;}
    public int getId(){return id;}
    public void setState(boolean state) {this.state = state;}
    public void setId(int id){
        this.id = id;
    }
    public void setItem(String i){this.item   = i;}
    public String getDateItem() {return dateItem;}
    public void setDateItem(String dateItem) {this.dateItem = dateItem;}

    @Override
    public String toString() {
        return "id:" + this.getId() + " | item:"+ this.getItem() + " | state:" + this.getState() + " | datestamp:"+dateItem;
    }

    public String sortView(){
        return "item:"+ this.getItem() + " | state:" + this.getState();
    }
}
