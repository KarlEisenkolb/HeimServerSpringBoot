package com.pi.server.database.open_weather;

import com.pi.server.models.open_weather.WeatherForecast_daily_entity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_DailyWeather_Impl implements DAO_Weather<WeatherForecast_daily_entity>{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public WeatherForecast_daily_entity getLastItem() {
        return null;
    }

    @Override
    public List<WeatherForecast_daily_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        return null;
    }

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
}
