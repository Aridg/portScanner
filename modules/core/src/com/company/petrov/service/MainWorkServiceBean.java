package com.company.petrov.service;

import com.company.petrov.entity.AdminPanels;
import com.company.petrov.entity.Scripts;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service(MainWorkService.NAME)
public class MainWorkServiceBean implements MainWorkService {
    @Override
    public List<Integer> scanPorts(String host) throws InterruptedException {
        PortScanner scanner = new PortScanner(host)
                .scanPorts();
        List<Integer> openPorts = scanner.getAllOpenPorts();
        return openPorts;
    }

    @Override
    public List<String> getActiveAdminPanels(String host) {
        List<String> admins = new ArrayList<String>();
        for (AdminPanels adminPanel : AdminPanels.values())
            admins.add(adminPanel.getUrl());
        List<String> urlsAdm = testConnect(host, admins);
        if(urlsAdm.size() > 0) System.out.println("Админки:");
        for (String s : urlsAdm)
            System.out.println(s);
        return urlsAdm;
    }

    @Override
    public List<String> getScripts(String host) {
        List<String> scripts = new ArrayList<String>();
        for (Scripts script : Scripts.values())
            scripts.add(script.getUrl());
        List<String> urlsScripts = testConnect(host, scripts);
        return urlsScripts;
    }

    private List<String> testConnect(String host, List<String> urlList) {
        List<String> existed = new ArrayList<String>();

        for(String url : urlList){
            try {
                URL url_request = new URL(String.format("http://%s%s", host, url));

                HttpURLConnection connection = (HttpURLConnection) url_request.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode < 400)
                    existed.add(url);
                else continue;
            }catch (IOException e){

            }
        }

        return existed;
    }
}