package com.pi.server.DatabaseManagment;

import java.util.List;

public interface DAO<T> {

    T get(long id);

    List<T> getAll();

    void save(T t_save);

    void saveListOfDataAndDeleteFormerData(List<T> t_saveList);

    void update(T t_alt, T t_neu);

    void delete(T t_del);
}
