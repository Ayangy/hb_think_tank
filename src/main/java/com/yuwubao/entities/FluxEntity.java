package com.yuwubao.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flux")
public class FluxEntity {

    @Id
    @GeneratedValue
    private int id;
    private int fluxnum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFluxnum() {
        return fluxnum;
    }

    public void setFluxnum(int fluxnum) {
        this.fluxnum = fluxnum;
    }
}
