package com.example.yohni.donorq;

public class Status {
    private String by;
    private String ctx;

    public Status(){

    }

    public Status(String by, String ctx){
        this.by = by;
        this.ctx = ctx;
    }

    public String getBy() {
        return by;
    }

    public String getCtx() {
        return ctx;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setCtx(String ctx) {
        this.ctx = ctx;
    }
}
