package main.java.loc.service.controllers;


import main.java.loc.service.model.Task;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ManageTask {
    private static SessionFactory factory;
    static Logger log = Logger.getLogger(ServController.class);

    public  ManageTask(){
        try {
            Configuration configuration = new Configuration();
            factory =configuration.configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /* Method to CREATE an task in the database */
    public Integer addTask(Task t){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer taskID = null;
        System.out.println(t.toString());
        try {
            tx = session.beginTransaction();
            taskID = (Integer) session.save(t);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {session.close();}
        return taskID;
    }

    /* Method to  READ all the tasks */
    public List<Task> listTasks( ) throws Exception{
        Session session = factory.openSession();
        Transaction tx = null;
        List<Task> tasks = new ArrayList<Task>();
        try {
            tx = session.beginTransaction();
            //System.out.println("session isOpen? - "+session.isOpen());
           // System.out.println("session isConnected? - "+session.isConnected());
            tasks = session.createQuery("FROM main.java.loc.service.model.Task").list();
            /*for (Iterator iterator = tasks.iterator(); iterator.hasNext();){
                Task t = (Task) iterator.next();
                System.out.print("id: " + t.getId());
                System.out.print(" | Item: " + t.getItem());
                System.out.print("  State: " + t.getState());
            }*/
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();

            log.warn("My log **************** - " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {session.close();}
        return  tasks;
    }

    /* Method to UPDATE item for an task */
    public void updateTask(Integer taskID, String t, boolean s, String dateItem){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Task task = (Task)session.get(Task.class, taskID);
            task.setItem(t);
            task.setState(s);
            task.setDateItem(dateItem);
            session.update(task);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            log.warn(e.getMessage());
            e.printStackTrace();
        } finally {session.close();}
    }

    /* Method to DELETE ALL items*/
    public void deleteAllTask(){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("truncate table task").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            log.warn(e.getMessage());
            e.printStackTrace();
        } finally {session.close();}
    }


    /* Method to DELETE an task from the records */
    public void deleteTask(Integer taskID){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Task t = (Task)session.get(Task.class, taskID);
            session.delete(t);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            log.warn(e.getMessage());
            e.printStackTrace();
        } finally {session.close();}
    }

    public Task getTaksById(Integer taskID) {
        Session session = factory.openSession();
        Transaction tx = null;
        Task t = null;
        try {
            tx = session.beginTransaction();
            t = (Task)session.get(Task.class, taskID);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            log.warn(e.getMessage());
            e.printStackTrace();
        } finally {session.close();}
        return t;
    }
}
