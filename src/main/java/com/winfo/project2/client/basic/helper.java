package com.winfo.project2.client.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.project2.client.basic.data.Settings;

import java.io.*;

/**
 * Created by Kevin on 12.06.17.
 */
public class helper {

    public static Settings getSettings(){
        Settings s = new Settings();
        s.setIpaddress("");
        s.setPort("");
        File file = new File("settings.ser");
        if (!file.exists()){
            return s;
        }
        try {
            FileInputStream fis = new FileInputStream("settings.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            s = (Settings)ois.readObject();
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean setSettings(Settings settings){

        File file = new File("settings.ser");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream("settings.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(settings);

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return jsonInString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
