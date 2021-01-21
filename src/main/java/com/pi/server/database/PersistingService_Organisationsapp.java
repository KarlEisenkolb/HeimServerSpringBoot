package com.pi.server.database;

import com.pi.server.database.organisationsapp.DAO_Organisationsapp;
import com.pi.server.models.organisationsapp.FirebaseCrypt_Termin_entity;
import com.pi.server.models.organisationsapp.OrganisationsApp_Nutzer_entity;
import com.pi.server.models.organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistingService_Organisationsapp {

    private final Logger log = LoggerFactory.getLogger(PersistingService_Organisationsapp.class);

    @Autowired
    @Qualifier("DAO_MariaDB_NutzerOrganisationsapp_Impl")
    private DAO_Organisationsapp dao_nutzerOrganisationsapp;
    public final static int NutzerOrganisationsapp = 0;
    public final static int NutzerOrganisationsapp_withNutzerNameAsId = 1;

    @Autowired
    @Qualifier("DAO_MariaDB_TokenOrganisationsapp_Impl")
    private DAO_Organisationsapp dao_tokenOrganisationsapp;
    public final static int NutzerOrganisationsapp_Token = 2;

    @Autowired
    @Qualifier("DAO_MariaDB_TerminOrganisationsapp_Impl")
    private DAO_Organisationsapp dao_terminOrganisationsapp;
    public final static int NutzerOrganisationsapp_Termin = 3;

    public void save (Object obj){
        if(obj instanceof OrganisationsApp_Nutzer_entity)
            dao_nutzerOrganisationsapp.save(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.save(obj);
        else if(obj instanceof FirebaseCrypt_Termin_entity)
            dao_terminOrganisationsapp.save(obj);
        else
            log.warn(" No known Object to save found!");
    }

    public void update (Object old_obj, Object new_obj){
        if (old_obj instanceof FirebaseCrypt_Termin_entity)
            dao_terminOrganisationsapp.update(old_obj, new_obj);
        else
            log.warn(" No known Object to save found!");
    }

    public Object get(String id, int requestedType){
        if (requestedType == NutzerOrganisationsapp)
            return dao_nutzerOrganisationsapp.get(id);
        else if (requestedType == NutzerOrganisationsapp_withNutzerNameAsId)
            return dao_nutzerOrganisationsapp.get_withNutzerName(id);
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAll_withStartAndEndTime(int requestedType, long startTime, long endTime){
        if (requestedType == NutzerOrganisationsapp_Termin)
            return dao_terminOrganisationsapp.getAll_withStartAndEndTime(startTime, endTime);
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAll(int requestedType){
        if (requestedType == NutzerOrganisationsapp)
            return dao_nutzerOrganisationsapp.getAll();
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public void delete(Object obj) {
        if(obj instanceof OrganisationsApp_Nutzer_entity)
            dao_nutzerOrganisationsapp.delete(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.delete(obj);
        else if(obj instanceof FirebaseCrypt_Termin_entity)
            dao_terminOrganisationsapp.delete(obj);
        else {
            log.warn(" No known Object to get from Database requested");
        }
    }
}
