package org.jan.repositorio;

import java.util.List;

public interface Repository<T>{
    List<T> listar();
    T forId(Long id);
    void save(T t);//esta el insert y update
    void delete(Long id);
}
