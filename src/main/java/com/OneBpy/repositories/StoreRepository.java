package com.OneBpy.repositories;

import com.OneBpy.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query(value = "SELECT s.store_name FROM store_tb s", nativeQuery = true)
    List<String> getAllStoreName();

}
