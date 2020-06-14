package com.pi.server.Models.Organisationsapp;


public class TerminDecrypted_mav {
    private String uhrzeit;
    private String name;
    private String description;
    private long type;
    private long erledigungszeit;

    public TerminDecrypted_mav(){
    }

    public TerminDecrypted_mav(String uhrzeit,
                               String name,
                               String description,
                               long type,
                               long erledigungszeit) {
        this.uhrzeit = uhrzeit;
        this.name = name;
        this.description = description;
        this.type = type;
        this.erledigungszeit = erledigungszeit;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getErledigungszeit() {
        return erledigungszeit;
    }

    public void setErledigungszeit(long erledigungszeit) {
        this.erledigungszeit = erledigungszeit;
    }
}
