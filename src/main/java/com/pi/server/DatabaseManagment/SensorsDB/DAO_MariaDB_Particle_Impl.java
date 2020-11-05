package com.pi.server.DatabaseManagment.SensorsDB;

import com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DAO_MariaDB_Particle_Impl implements DAO_Sensor<Sensor_Particle_entity> {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_Particle_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Sensor_Particle_entity getLastItem_withTableName(String table_name) {
        String queryString = "SELECT w FROM " + table_name +" w ORDER BY w.id DESC";
        TypedQuery<Sensor_Particle_entity> query = entityManager.createQuery(queryString, Sensor_Particle_entity.class);
        return query.setMaxResults(1).getSingleResult();
    }

    @Override
    public List<Sensor_Particle_entity> getAll_withTableName(String table_name) {
        return null;
    }

    @Override
    public List<Sensor_Particle_entity> getAll_withStartAndEndTime_withTableName(long startTime, long endTime, String table_name) {
        String timePeriod = "(w.id <= " + endTime + " AND w.id >= " + startTime + ")";
        String queryString = "SELECT w FROM " + table_name + " w WHERE " + timePeriod  + " ORDER BY w.id ASC";
        TypedQuery<Sensor_Particle_entity> query = entityManager.createQuery(queryString, Sensor_Particle_entity.class);
        return query.getResultList();
    }

}
