package com.pi.server.DatabaseManagment.SensorsDB;

import java.util.List;

public interface DAO_Sensors<T> {

    List<T> getAll_withStartAndEndTime(long startTime, long endTime);

}
