package com.pi.server.DatabaseManagment.SensorsDB;

import com.pi.server.DatabaseManagment.DAO_Basic;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Sensor_BME680_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DAO_MariaDB_BME680_Impl implements DAO_Basic<Sensor_BME680_entity>  {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_BME680_Impl.class);

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Sensor_BME680_entity get(long id) {
        return null;
    }

    @Override
    public Sensor_BME680_entity get(String id) {
        return null;
    }

    @Override
    public Sensor_BME680_entity getLastItem() {
        String queryString = "SELECT w FROM " + Sensor_BME680_entity.TableName +" w ORDER BY w.id DESC";
        TypedQuery<Sensor_BME680_entity> query = entityManager.createQuery(queryString, Sensor_BME680_entity.class);
        return query.setMaxResults(1).getSingleResult();
    }

    @Override
    public List<Sensor_BME680_entity> getAll() {
        String queryString = "SELECT w FROM " + Sensor_BME680_entity.TableName +" w ORDER BY w.id ASC";
        TypedQuery<Sensor_BME680_entity> query = entityManager.createQuery(queryString, Sensor_BME680_entity.class);
        return query.getResultList();
    }

    @Override
    public void save(Sensor_BME680_entity t_save) {

    }

    @Override
    public void update(Sensor_BME680_entity t_alt, Sensor_BME680_entity t_neu) {

    }

    @Override
    public void delete(Sensor_BME680_entity t_del) {

    }

    @Override
    public List<Sensor_BME680_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        String timePeriod = "(w.id <= " + endTime + " AND w.id >= " + startTime + ")";
        String queryString = "SELECT w FROM " + Sensor_BME680_entity.TableName + " w WHERE " + timePeriod  + " ORDER BY w.id ASC";
        TypedQuery<Sensor_BME680_entity> query = entityManager.createQuery(queryString, Sensor_BME680_entity.class);
        return query.getResultList();
    }
}
