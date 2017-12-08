package com.company.petrov.web.screens;

import com.company.petrov.service.MainWorkService;
import com.haulmont.cuba.gui.components.*;

import javax.inject.Inject;
import java.util.List;

public class Workwindow extends AbstractWindow {
    @Inject
    private Button adminsScanBut;
    @Inject
    private Button scanPortsBut;
    @Inject
    private ResizableTextArea scanPortsOut;
    @Inject
    private Button scriptsBut;
    @Inject
    private TextField urlField;
    @Inject
    private ResizableTextArea adminsOut;
    @Inject
    private ResizableTextArea scriptsOut;
    @Inject
    private MainWorkService mainWorkService;

    public void onScanPortsButClick() throws InterruptedException {
        List<Integer> integers = mainWorkService.scanPorts(urlField.getValue());
        StringBuilder builder = new StringBuilder();
        if(integers.size() == 0)
            builder.append("Открытых портов нет");
        else builder.append("Открытые порты: \n");
        for (Integer integer : integers)
            builder.append(integer).append("\n");
        scanPortsOut.setValue(builder.toString());
    }

    public void onAdminsScanButClick() {
        List<String> activeAdminPanels = mainWorkService.getActiveAdminPanels(urlField.getValue());
        StringBuilder builder = new StringBuilder();
        if(activeAdminPanels.size() == 0)
            builder.append("Админки не найдены");
        else builder.append("Админки: \n");
        for (String name : activeAdminPanels)
            builder.append(name).append("\n");
        adminsOut.setValue(builder.toString());
    }

    public void onScriptsButClick() {
        List<String> activeAdminPanels = mainWorkService.getScripts(urlField.getValue());
        StringBuilder builder = new StringBuilder();
        if(activeAdminPanels.size() == 0)
            builder.append("Скрипты не найдены");
        else builder.append("Остаточные скрипты: \n");
        for (String name : activeAdminPanels)
            builder.append(name).append("\n");
        scriptsOut.setValue(builder.toString());
    }
}