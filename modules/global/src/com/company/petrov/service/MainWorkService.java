package com.company.petrov.service;


import java.util.List;

public interface MainWorkService {
    String NAME = "petrov_MainWorkService";

    List<Integer> scanPorts(String host) throws InterruptedException;

    List<String> getActiveAdminPanels(String host);

    List<String> getScripts(String host);
}