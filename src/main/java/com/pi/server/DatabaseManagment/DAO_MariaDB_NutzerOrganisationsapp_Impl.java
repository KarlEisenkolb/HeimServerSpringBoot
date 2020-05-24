package com.pi.server.DatabaseManagment;

import com.pi.server.Models.Organisationsapp.Nutzer_entity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_NutzerOrganisationsapp_Impl implements DAO<Nutzer_entity> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Nutzer_entity get(long id) {
        return null;
    }

    @Override
    public List<Nutzer_entity> getAll() {
        String queryString = "SELECT w FROM " + Nutzer_entity.TableName + " w ORDER BY w.name";
        TypedQuery<Nutzer_entity> query = entityManager.createQuery(queryString, Nutzer_entity.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Nutzer_entity t_save) {
        entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void saveListOfDataAndDeleteFormerData(List<Nutzer_entity> t_saveList) {
        entityManager.persist(t_saveList);
    }

    @Override
    public void update(Nutzer_entity t_alt, Nutzer_entity t_neu) {

    }

    @Override
    public void delete(Nutzer_entity t_del) {

    }
}