package com.company.petrov.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum Scripts implements EnumClass<Integer> {

    INDEX(10,"/index.php"),
    LOGIN(20,"/login.php"),
    LOG(30, "/log.php"),
    SERVER(40,"/server.php"),
    ACRCHIVE(50,"/archive.php"),
    PICTURES(60,"/pictures.php"),
    MAIN(70,"/main.php"),
    ADMIN(80,"admin.php"),
    INVOICE(90,"/invoice.php"),
    EXAMPLE(100,"/example.php"),
    PRINT(110,"/print.php")
    ;

    private Integer id;
    private String url;

    Scripts(Integer value, String url) {
        this.id = value;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }
    public String getUrl(){return url;}

    @Nullable
    public static Scripts fromId(Integer id) {
        for (Scripts at : Scripts.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}