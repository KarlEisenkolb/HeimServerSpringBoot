package com.pi.server.DatabaseManagment.OrganisationsappDB;

import java.util.List;

public interface DAO_Organisationsapp<T> {

    T get(String id);

    T get_withNutzerName(String name);

    List<T> getAll();

    List<T> getAll_withStartAndEndTime(long startTime, long endTime);

    void save(T t_save);

    void update(T t_old, T t_new);

    void delete(T t_del);

}
