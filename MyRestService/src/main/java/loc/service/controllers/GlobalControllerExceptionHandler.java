package main.java.loc.service.controllers;


import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketException;

//import main.java.loc.service.controllers.*;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";
    //static public ResponseS response = new ResponseS();
    static Logger log = Logger.getLogger(ServController.class);
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Item was not found")  // 404
    @ExceptionHandler(NullPointerException.class)
    public void handleConflict() {}

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="CONFLICT")  // 404
    @ExceptionHandler(HibernateException.class)
    public String handleHibertnateException() {
        return "Error read/rec Mysql";
    }



    @ResponseStatus(value= HttpStatus.CONFLICT, reason="No connection with DB")  // 404
    @ExceptionHandler(org.hibernate.exception.JDBCConnectionException.class)
    public String handleJDBCConnectionException() {
        return "Error read/rec Mysql";
    }

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="No such oject")  // 404
    @ExceptionHandler(SocketException.class)
    public String handleSocketException() {
        return "Error read/rec Mysql";
    }

    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    public ResponseEntity<ResponseS> jsonParseException(com.fasterxml.jackson.databind.exc.InvalidFormatException e) throws Exception {
        ResponseS responseS = new ResponseS();
        responseS.setStatus(HttpStatus.BAD_REQUEST.toString());
        responseS.setMessage(e.getMessage());
        return new ResponseEntity<>(responseS, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseS> generalException(HttpServletRequest request, Exception e) throws Exception {
        ResponseS responseS = new ResponseS();
        responseS.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        responseS.setMessage("Erorr: " + e.getMessage());
        return new ResponseEntity<>(responseS, HttpStatus.INTERNAL_SERVER_ERROR);
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        /*if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        mav.setStatus(HttpStatus.CONFLICT);*/
       // return e.getMessage();
    }




}
