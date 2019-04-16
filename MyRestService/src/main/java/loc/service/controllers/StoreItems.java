package main.java.loc.service.controllers;


import main.java.loc.service.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StoreItems {
    List<Task> tasks;
    private final String fileName = "src/main/resources/Tasks.dat";

    public List<Task> getAll() {
        tasks = null;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return new ArrayList<Task>();
            } else {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                tasks = (List<Task>)ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean deleteFile() throws IOException{
        File file = new File(fileName);
        return file.delete();
    }

    public void saveItemsList (List<Task> itemList) {
        try {
            File file = new File(fileName);
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(itemList);
            System.out.println("File is written - "+fileName);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}