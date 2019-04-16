package main.java.loc.service.controllers;

import org.apache.log4j.Logger;

public class ResponseS {
    static Logger log = Logger.getLogger(ServController.class);
    //private Task t;
    private String message;
    private String status;

    public ResponseS(){}
    public ResponseS(String m){
        this.message = m;
    }
    public ResponseS(String m, String s){
        this.message = m;
        status = s;
    }
    public String getMessage() {return message;}
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return "message:"+message+" | status:"+status;
    }
}
