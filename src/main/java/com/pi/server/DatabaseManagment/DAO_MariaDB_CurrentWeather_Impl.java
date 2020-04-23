package com.pi.server.DatabaseManagment;

import com.pi.server.Models.Weather_current;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_CurrentWeather_Impl implements DAO<Weather_current> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Weather_current get(long id) {
        return null;
    }

    @Override
    public List<Weather_current> getAll() {
        String queryString = "SELECT w FROM Weather_current w ORDER BY w.request_timestamp DESC";
        TypedQuery<Weather_current> query = entityManager.createQuery(queryString, Weather_current.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Weather_current t_save) {
        entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void saveListOfData(List<Weather_current> t_saveList) {
        entityManager.persist(t_saveList);
    }

    @Override
    public void update(Weather_current t_alt, Weather_current t_neu) {

    }

    @Override
    public void delete(Weather_current t_del) {

    }
}
