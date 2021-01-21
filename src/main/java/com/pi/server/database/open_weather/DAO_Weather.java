package com.pi.server.database.open_weather;

import java.util.List;

public interface DAO_Weather<T> {

    T getLastItem();

    List<T> getAll_withStartAndEndTime(long startTime, long endTime);

    List<T> getAll();

    void save(T t_save);

    void saveListOfDataAndDeleteFormerData(List<T> t_saveList);

}
