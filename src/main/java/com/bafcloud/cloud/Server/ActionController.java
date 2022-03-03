package com.bafcloud.cloud.Server;

import java.io.File;

public class ActionController {

    private final String path;

    public ActionController(String path){
        this.path = path;

    }

    public void reg() {
            }

    public String mkdir(String path) {
        System.out.println("Принята комманда /mkdir");
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("dirSuccess");
            return "dirSuccess";
        } else {
            System.out.println("unSuccess");
            return "unSuccess";
        }
    }

    public String list() {
        System.out.println("Принята комманда /list");
        File file = new File(path);
        File[] files = file.listFiles();
        StringBuilder sb = new StringBuilder();
        assert files != null;
        for (File f : files) {
            sb.append(f.getName()).append("\r\n");
        }
        if (sb.length() < 1) sb.append("Empty");
        return sb.toString();
    }
}
