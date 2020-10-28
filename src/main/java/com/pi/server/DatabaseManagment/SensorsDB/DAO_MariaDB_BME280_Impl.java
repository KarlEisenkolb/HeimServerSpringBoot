package com.pi.server.DatabaseManagment.SensorsDB;
import com.pi.server.DatabaseManagment.DAO_Basic;
import com.pi.server.Models.SensorModels.Sensor_BME280_entity;
import com.pi.server.Models.SensorModels.Sensor_BME680_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DAO_MariaDB_BME280_Impl implements DAO_Basic<Sensor_BME280_entity> {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_BME280_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Sensor_BME280_entity get(long id) {
        return null;
    }

    @Override
    public Sensor_BME280_entity get(String id) {
        return null;
    }

    @Override
    public Sensor_BME280_entity getLastItem() {
        String queryString = "SELECT w FROM " + Sensor_BME280_entity.TableName +" w ORDER BY w.id DESC";
        TypedQuery<Sensor_BME280_entity> query = entityManager.createQuery(queryString, Sensor_BME280_entity.class);
        return query.setMaxResults(1).getSingleResult();
    }

    @Override
    public List<Sensor_BME280_entity> getAll() {
        return null;
    }

    @Override
    public List<Sensor_BME280_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        return null;
    }

    @Override
    public void save(Sensor_BME280_entity t_save) {

    }

    @Override
    public void update(Sensor_BME280_entity t_alt, Sensor_BME280_entity t_neu) {

    }

    @Override
    public void delete(Sensor_BME280_entity t_del) {

    }
}
