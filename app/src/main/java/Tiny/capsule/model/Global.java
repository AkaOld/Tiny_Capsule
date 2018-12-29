package Tiny.capsule.model;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class Global {
    public static String baseProject = "http://172.20.10.3:8080/TinyCapsuleServer";
    public static String baseUrl = baseProject + "/android/";
    public static String currentUsername = null;
    public static volatile int downloadCount = 0;
    public static ArrayList<String> icons;
    public static boolean downloaded = false;
    public static boolean added = false;
}
