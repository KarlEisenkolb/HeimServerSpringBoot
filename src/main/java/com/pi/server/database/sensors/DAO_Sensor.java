package com.pi.server.database.sensors;

import java.util.List;

public interface DAO_Sensor<T> {

    T getLastItem_withTableName(String table_name);

    List<T> getAll_withTableName(String table_name);

    List<T> getAll_withStartAndEndTime_withTableName(long startTime, long endTime, String table_name);

}
