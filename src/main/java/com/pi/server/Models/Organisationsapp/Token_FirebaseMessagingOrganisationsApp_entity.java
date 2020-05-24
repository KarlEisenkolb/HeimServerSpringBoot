package com.pi.server.Models.Organisationsapp;

import javax.persistence.*;

@Entity(name = Token_FirebaseMessagingOrganisationsApp_entity.TableName)
@Table(name = Token_FirebaseMessagingOrganisationsApp_entity.TableName)
public class Token_FirebaseMessagingOrganisationsApp_entity {

    public final static String TableName = "organisationsapp_token";

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn
    private Nutzer_entity nutzer_entity;

    @Column
    private String token;

    public Token_FirebaseMessagingOrganisationsApp_entity(String token, Nutzer_entity nutzer_entity){
        this.token = token;
        this.nutzer_entity = nutzer_entity;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nutzer_entity getNutzer_entity() {
        return nutzer_entity;
    }

    public void setNutzer_entity(Nutzer_entity nutzer_entity) {
        this.nutzer_entity = nutzer_entity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
