package com.pi.server.DatabaseManagment.OpenWeatherDB;

import com.pi.server.DatabaseManagment.DAO;
import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_DailyWeather_Impl implements DAO<WeatherForecast_daily_entity> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public WeatherForecast_daily_entity get(long id) { return null; }

    @Override
    public List<WeatherForecast_daily_entity> getAll() {
        String queryString = "SELECT w FROM " + WeatherForecast_daily_entity.TableName +" w ORDER BY w.time ASC";
        TypedQuery<WeatherForecast_daily_entity> query = entityManager.createQuery(queryString, WeatherForecast_daily_entity.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(WeatherForecast_daily_entity t_save) { entityManager.persist(t_save); }

    @Transactional
    @Override
    public void saveListOfDataAndDeleteFormerData(List<WeatherForecast_daily_entity> t_saveList) {
        entityManager.createQuery("DELETE FROM " + WeatherForecast_daily_entity.TableName).executeUpdate();
        for(WeatherForecast_daily_entity item : t_saveList)
            entityManager.persist(item);
    }

    @Override
    public void update(WeatherForecast_daily_entity t_alt, WeatherForecast_daily_entity t_neu) {

    }

    @Override
    public void delete(WeatherForecast_daily_entity t_del) {

    }
}
