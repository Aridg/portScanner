package com.company.petrov.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum AdminPanels implements EnumClass<Integer> {

    VESTA(10,":8083"),
    ISPMANAGER(20, ":1500/ispmgr"),
    DIRECTADMIN(30, ":2222"),
    CPANEL(40, ":2083"),
    PLESK(50, ":8443"),
    BRAINY(60, ":8000"),
    PHPMYADMIN(70, "/phpmyadmin"),
    WPADMIN(80, "/wp-admin")
    ;

    private Integer id;
    private String url;

    AdminPanels(Integer value, String url) {
        this.id = value;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl(){
        return url;
    }

    @Nullable
    public static AdminPanels fromId(Integer id) {
        for (AdminPanels at : AdminPanels.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}