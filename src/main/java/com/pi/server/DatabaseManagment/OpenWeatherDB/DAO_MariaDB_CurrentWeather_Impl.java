package com.pi.server.DatabaseManagment.OpenWeatherDB;

import com.pi.server.DatabaseManagment.DAO;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_CurrentWeather_Impl implements DAO<Weather_current_entity> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Weather_current_entity get(long id) {
        return null;
    }

    @Override
    public List<Weather_current_entity> getAll() {
        String queryString = "SELECT w FROM " + Weather_current_entity.TableName +" w ORDER BY w.request_timestamp DESC";
        TypedQuery<Weather_current_entity> query = entityManager.createQuery(queryString, Weather_current_entity.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Weather_current_entity t_save) {
        entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void saveListOfDataAndDeleteFormerData(List<Weather_current_entity> t_saveList) {
        entityManager.persist(t_saveList);
    }

    @Override
    public void update(Weather_current_entity t_alt, Weather_current_entity t_neu) {

    }

    @Override
    public void delete(Weather_current_entity t_del) {

    }
}
