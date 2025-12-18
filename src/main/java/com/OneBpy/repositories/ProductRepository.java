package com.OneBpy.repositories;

import com.OneBpy.models.Product;
import com.OneBpy.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
// language=MySQL
    @Query(value = "SELECT p.* FROM product_tb p " +
            "WHERE EXISTS ( " +
            "SELECT 1 FROM stop_tb s1 " +
            "WHERE s1.product_id = p.product_id " +
            "AND s1.stop_time BETWEEN :startTime1 AND :startTime2 " +
            "AND s1.stop_address LIKE CONCAT('%', :startAddress, '%'))  " +
            "AND EXISTS (" +
            "SELECT 1 FROM stop_tb s2 " +
            "WHERE s2.product_id = p.product_id " +
            "AND s2.stop_time > :startTime2 " +
            "AND s2.stop_address LIKE CONCAT('%', :endAddress, '%')) " +
            "ORDER BY p.start_time "
            , nativeQuery = true)
    List<Product> findProductsByTimeAndAddress(
            @Param("startTime1") LocalTime startTime1,
            @Param("startTime2") LocalTime startTime2,
            @Param("startAddress") String startAddress,
            @Param("endAddress") String endAddress
    );

    @Query(value = "SELECT p.* FROM product_tb p WHERE display = true and deleted = false", nativeQuery = true)
    List<Product> findAllActiveProducts();

    @Query(value = "SELECT p.* FROM product_tb p WHERE deleted = false and store_id = :store_id"
            , nativeQuery = true)
    List<Product> findAllStoreProducts(Long store_id);


    @Query(value =  "SELECT DISTINCT p.* " +
            "FROM product_tb p " +
            "INNER JOIN stop_tb s ON p.product_id = s.product_id " +
            "INNER JOIN store_tb st ON p.store_id = st.store_id " +
            "WHERE (s.stop_address LIKE CONCAT('%', :keyword, '%') OR p.start_address LIKE CONCAT('%', :keyword, '%') " +
            "OR p.end_address LIKE CONCAT('%', :keyword, '%') OR st.store_name LIKE CONCAT('%', :keyword, '%') " +
            "OR p.bien_so_xe LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY p.start_time ASC"
            , nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);

    List<Product> findAllProductsByStore_User_UserId(Long userId);

}
