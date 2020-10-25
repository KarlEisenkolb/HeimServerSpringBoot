package com.pi.server.DatabaseManagment.OrganisationsappDB;

import com.pi.server.DatabaseManagment.DAO_Basic;
import com.pi.server.Models.Organisationsapp.OrganisationsApp_Nutzer_entity;
import com.pi.server.Models.Organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_NutzerOrganisationsapp_Impl implements DAO_Basic<OrganisationsApp_Nutzer_entity>, DAO_Organisationsapp<OrganisationsApp_Nutzer_entity>{

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_NutzerOrganisationsapp_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public OrganisationsApp_Nutzer_entity get(String id) {
        /*TypedQuery<Nutzer_entity> q1 = entityManager.createQuery("SELECT a FROM " + Nutzer_entity.TableName + " a WHERE a.firebaseID = :itemId", Nutzer_entity.class);
        q1.setParameter("itemId", id);
        Nutzer_entity nutzer_entity = q1.getSingleResult();

        List<String> tokenStringList = new ArrayList<>();
        TypedQuery<Token_FirebaseMessagingOrganisationsApp_entity> q2 = entityManager.createQuery("SELECT b FROM " + Token_FirebaseMessagingOrganisationsApp_entity.TableName + " b WHERE b.nutzer_entity.firebaseID = :itemId", Token_FirebaseMessagingOrganisationsApp_entity.class);
        q2.setParameter("itemId", id);
        List<Token_FirebaseMessagingOrganisationsApp_entity> tokenEntityList = q2.getResultList();
        for (Token_FirebaseMessagingOrganisationsApp_entity tokenEntity : tokenEntityList)
            tokenStringList.add(tokenEntity.getToken());
        nutzer_entity.setTokenStringlist(tokenStringList);*/
        return entityManager.find(OrganisationsApp_Nutzer_entity.class, id);
    }

    @Override
    public OrganisationsApp_Nutzer_entity getLastItem() {
        return null;
    }

    @Override
    public OrganisationsApp_Nutzer_entity get_withNutzerName(String name){
        TypedQuery<OrganisationsApp_Nutzer_entity> q = entityManager.createQuery("SELECT a FROM " + OrganisationsApp_Nutzer_entity.TableName + " a WHERE a.name = :nameID", OrganisationsApp_Nutzer_entity.class);
        q.setParameter("nameID", name);
        return q.getSingleResult();
    }

    @Override
    public List<OrganisationsApp_Nutzer_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        return null;
    }

    @Override
    public OrganisationsApp_Nutzer_entity get(long id) {
        return null;
    }

    @Override
    public List<OrganisationsApp_Nutzer_entity> getAll() {
        String queryString = "SELECT a FROM " + OrganisationsApp_Nutzer_entity.TableName + " a";
        TypedQuery<OrganisationsApp_Nutzer_entity> query = entityManager.createQuery(queryString, OrganisationsApp_Nutzer_entity.class);
        log.info("Nutzer aus Datenbank: {}", queryString);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(OrganisationsApp_Nutzer_entity t_save) {
        entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void update(OrganisationsApp_Nutzer_entity t_alt, OrganisationsApp_Nutzer_entity t_neu) {

    }

    @Transactional
    @Override
    public void delete(OrganisationsApp_Nutzer_entity t_del) {
        Query q2 = entityManager.createQuery("DELETE FROM " + Token_FirebaseMessagingOrganisationsApp_entity.TableName + " b WHERE b.nutzer_entity.firebaseID = :id");
        int deleted2 = q2.setParameter("id", t_del.getFirebaseID()).executeUpdate();

        Query q1 = entityManager.createQuery("DELETE FROM " + OrganisationsApp_Nutzer_entity.TableName + " a WHERE a.firebaseID = :id");
        int deleted1 = q1.setParameter("id", t_del.getFirebaseID()).executeUpdate();
    }
}