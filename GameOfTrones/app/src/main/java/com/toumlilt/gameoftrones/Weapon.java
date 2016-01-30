package com.toumlilt.gameoftrones;

import java.io.Serializable;

public class Weapon implements Serializable{
    private String name;
    private Integer pv;
    private Integer scope;


    public Weapon(String name, Integer pv, Integer scope) {
        this.name = name;
        this.pv = pv;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }
}
