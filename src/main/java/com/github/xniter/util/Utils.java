package com.github.xniter.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Utils {
    static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public static void writeObjectToFile(Object o, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(GSON.toJson(o));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(Class c, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            reader.close();
            return GSON.fromJson(sb.toString(), c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object readObjectFromFile(Class c, File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            reader.close();
            return GSON.fromJson(sb.toString(), c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
