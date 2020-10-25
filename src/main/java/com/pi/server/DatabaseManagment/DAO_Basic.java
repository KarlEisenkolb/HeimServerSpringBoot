package com.pi.server.DatabaseManagment;

import java.util.List;

public interface DAO_Basic<T> {

    T get(long id);

    T get(String id);

    T getLastItem();

    List<T> getAll();

    List<T> getAll_withStartAndEndTime(long startTime, long endTime);

    void save(T t_save);

    void update(T t_alt, T t_neu);

    void delete(T t_del);


}
