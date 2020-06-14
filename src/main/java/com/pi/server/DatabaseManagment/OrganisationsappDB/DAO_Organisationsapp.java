package com.pi.server.DatabaseManagment.OrganisationsappDB;

import java.util.List;

public interface DAO_Organisationsapp<T> {

    T get_withNutzerName(String name);

    List<T> getAll_withStartAndEndTime(long startTime, long endTime);

}
