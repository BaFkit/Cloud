package com.bafcloud.cloud.Server;

import java.io.*;

public class ActionController {

    private final String rootClient;
    private String currentDir;
    private String fileName;
    private FileOutputStream fos;

    public ActionController(String rootClient){
        this.rootClient = rootClient;

    }

    public void reg() {
            }

    public String mkdir(String[] parts) {
        System.out.println("Принята комманда /mkdir");
        File folder = new File(rootClient + File.separator + parts[1] + File.separator + parts[2]);
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Success");
            return "Success";
        } else {
            System.out.println("unSuccess");
            return "unSuccess";
        }
    }

    public String list(String currentPath) {
        System.out.println("Принята комманда /list");
        File file = new File(rootClient + File.separator + currentPath);
        File[] files = file.listFiles();
        StringBuilder sb = new StringBuilder();
        assert files != null;
        for (File f : files) {
            sb.append(f.getName()).append("\r\n");
        }
        if (sb.length() < 1) sb.append("Empty");
        return sb.toString();
    }

    public String upload(String[] parts) {
        currentDir = parts[1];
        fileName = parts[2];
        try {
            File file = new File(rootClient + File.separator + currentDir + File.separator + fileName);
            if (file.exists()) {
                file = new File(rootClient + File.separator + currentDir + File.separator + "(copy)" +fileName);
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            return "ready";
        } catch (IOException e) {
            e.printStackTrace();
            return "unSuccess";
        }
    }

    public String uploadFile(byte[] bytes) {
        try {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "unSuccess";
        }
        return "Success";
    }


//    public byte[] getBytes() {
//        byte[] bytes = new byte[512];
//        try {
//            bytes = Files.readAllBytes(download.toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bytes;
//    }

}
