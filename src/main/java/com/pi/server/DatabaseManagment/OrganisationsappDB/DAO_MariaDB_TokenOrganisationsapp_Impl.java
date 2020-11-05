package com.pi.server.DatabaseManagment.OrganisationsappDB;

import com.pi.server.Models.Organisationsapp.OrganisationsApp_Nutzer_entity;
import com.pi.server.Models.Organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_TokenOrganisationsapp_Impl implements DAO_Organisationsapp<Token_FirebaseMessagingOrganisationsApp_entity> {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_NutzerOrganisationsapp_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Token_FirebaseMessagingOrganisationsApp_entity get(String id) {
        return null;
    }

    @Override
    public Token_FirebaseMessagingOrganisationsApp_entity get_withNutzerName(String name) {
        return null;
    }

    @Override
    public List<Token_FirebaseMessagingOrganisationsApp_entity> getAll() {
        String queryString = "SELECT a FROM " + OrganisationsApp_Nutzer_entity.TableName + " a";
        TypedQuery<OrganisationsApp_Nutzer_entity> query = entityManager.createQuery(queryString, OrganisationsApp_Nutzer_entity.class);
        log.info("Nutzer aus Datenbank: {}", queryString);
        //return query.getResultList();
        return null;
    }

    @Override
    public List<Token_FirebaseMessagingOrganisationsApp_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        return null;
    }

    @Transactional
    @Override
    public void save(Token_FirebaseMessagingOrganisationsApp_entity t_save) {
        entityManager.persist(t_save);
    }

    @Override
    public void update(Token_FirebaseMessagingOrganisationsApp_entity t_old, Token_FirebaseMessagingOrganisationsApp_entity t_new) {

    }

    @Transactional
    @Override
    public void delete(Token_FirebaseMessagingOrganisationsApp_entity t_del) {
        //entityManager.remove(entityManager.contains(t_del) ? t_del : entityManager.merge(t_del));
        //entityManager.remove(entityManager.merge(t_del));
        int deleted = entityManager.createQuery("DELETE FROM " + Token_FirebaseMessagingOrganisationsApp_entity.TableName + " a WHERE a.id = " + t_del.getId()).executeUpdate();
        //int deleted = entityManager.createQuery("DELETE FROM " + Token_FirebaseMessagingOrganisationsApp_entity.TableName).executeUpdate();
    }
}