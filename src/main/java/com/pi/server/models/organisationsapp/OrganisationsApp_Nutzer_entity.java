package com.pi.server.models.organisationsapp;

import com.pi.server.security_handling.Crypt;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.pi.server.security_handling.Crypt.CRYPT_USE_DEFAULT_KEY;

@Entity(name = OrganisationsApp_Nutzer_entity.TableName)
@Table(name = OrganisationsApp_Nutzer_entity.TableName)
public class OrganisationsApp_Nutzer_entity {

    public final static String TableName = "organisationsapp_nutzer";

    @Id
    @Column
    private String firebaseID;

    @Column
    private String name;

    @Column
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="nutzer_entity")
    private List<Token_FirebaseMessagingOrganisationsApp_entity> tokens = new ArrayList<>();

    public OrganisationsApp_Nutzer_entity(){}

    public OrganisationsApp_Nutzer_entity(String firebaseID, String name, List<String> tokenListCrypted){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.firebaseID = firebaseID;
        this.name = name;
        for (String tokenCrypted : tokenListCrypted){
            tokens.add(new Token_FirebaseMessagingOrganisationsApp_entity(crypt.decryptString(tokenCrypted), tokenCrypted, this));
        }
    }

    public List<Token_FirebaseMessagingOrganisationsApp_entity> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token_FirebaseMessagingOrganisationsApp_entity> tokens) {
        this.tokens = tokens;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
