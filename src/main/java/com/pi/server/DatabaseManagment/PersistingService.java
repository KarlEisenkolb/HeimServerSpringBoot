package com.pi.server.DatabaseManagment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistingService {

    @Autowired
    @Qualifier("DAO_MariaDB_CurrentWeather_Impl")
    private DAO dao;

    public void save (Object obj){
        dao.save(obj);
    }

    public List<Object> getAll(){
        return dao.getAll();
    }
}
