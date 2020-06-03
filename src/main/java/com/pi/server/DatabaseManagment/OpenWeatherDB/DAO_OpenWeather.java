package com.pi.server.DatabaseManagment.OpenWeatherDB;

import java.util.List;

public interface DAO_OpenWeather<T> {

    void saveListOfDataAndDeleteFormerData(List<T> t_saveList);

}
