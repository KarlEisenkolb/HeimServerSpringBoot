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
    @JoinColumn(name = "firebaseID")
    private Nutzer_entity nutzer_entity;

    @Column
    private String token;

    @Column(length = 510)
    private String token_crypt_firebase; // Das Token wie es aktuell in Firebase verschlüsselt liegt (für den Löschvorgang)

    @Column
    private int error_count; // zählt wie oft ein Token benutzt wurde aber das Gerät nicht erreicht wurde... nach einer bestimmten Anzahl wird das Token gelöscht

    public Token_FirebaseMessagingOrganisationsApp_entity(){}

    public Token_FirebaseMessagingOrganisationsApp_entity(String token, String token_CryptFirebase, Nutzer_entity nutzer_entity){
        this.nutzer_entity = nutzer_entity;
        this.token = token;
        this.token_crypt_firebase = token_CryptFirebase;
        error_count = 0;
    }

    public int getId() {
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

    public int getError_count() {
        return error_count;
    }

    public void setError_count(int error_count) {
        this.error_count = error_count;
    }

    public String getToken_CryptFirebase() {
        return token_crypt_firebase;
    }

    public void setToken_CryptFirebase(String token_CryptFirebase) {
        this.token_crypt_firebase = token_CryptFirebase;
    }
}
