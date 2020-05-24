package com.pi.server.DatabaseManagment.OpenWeatherDB;

import com.pi.server.DatabaseManagment.DAO;
import com.pi.server.Models.OpenWeather.WeatherForecast_hourly_entity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_HourlyWeather_Impl implements DAO<WeatherForecast_hourly_entity> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public WeatherForecast_hourly_entity get(long id) { return null; }

    @Override
    public List<WeatherForecast_hourly_entity> getAll() {
        String queryString = "SELECT w FROM " + WeatherForecast_hourly_entity.TableName +" w ORDER BY w.time ASC";
        TypedQuery<WeatherForecast_hourly_entity> query = entityManager.createQuery(queryString, WeatherForecast_hourly_entity.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(WeatherForecast_hourly_entity t_save) { entityManager.persist(t_save); }

    @Transactional
    @Override
    public void saveListOfDataAndDeleteFormerData(List<WeatherForecast_hourly_entity> t_saveList) {
        entityManager.createQuery("DELETE FROM " + WeatherForecast_hourly_entity.TableName).executeUpdate();
        for(WeatherForecast_hourly_entity item : t_saveList)
            entityManager.persist(item);
    }

    @Override
    public void update(WeatherForecast_hourly_entity t_alt, WeatherForecast_hourly_entity t_neu) {

    }

    @Override
    public void delete(WeatherForecast_hourly_entity t_del) {

    }
}
