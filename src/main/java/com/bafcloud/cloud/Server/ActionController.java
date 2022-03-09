package com.bafcloud.cloud.Server;

import com.bafcloud.cloud.Server.Services.AuthorizationService;

import java.io.*;

public class ActionController {

    private final String root;
    private String rootClient;
    private long spaceClient;
    private String currentDir;
    private String fileName;

    private final AuthorizationService authorizationService;
    private FileOutputStream fos;

    public ActionController(AuthorizationService authorizationService, String root) {
        this.authorizationService = authorizationService;
        this.root = root;
    }

    public String authorization(String[] parts) {
        spaceClient = authorizationService.checkUserVerification(parts[1], parts[2]);
        if (spaceClient > -1) {
            rootClient = authorizationService.getRootClient(parts[1], parts[2]);
            if (rootClient.equals("notExist")) {
                File folder = new File(root + File.separator + parts[1]);
                folder.mkdir();
                rootClient = parts[1];
            }
            return "Success";
        }
        return "unSuccess";
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
                file = new File(rootClient + File.separator + currentDir + File.separator + "(copy)" + fileName);
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            return "ready";
        } catch (IOException e) {
            e.printStackTrace();
            return "unSuccess";
        }
    }

    public String checkCapacity(String size) {
        if (spaceClient > Long.parseLong(size)) {
            return "waitingGet";
        }
        return "exceeded";
    }

    public String uploadFile(byte[] bytes) {
        try {
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "unSuccess";
        }
        return "Success";
    }
}
