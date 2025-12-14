package com.OneBpy.repositories;

import com.OneBpy.dtos.NoticeDTO;
import com.OneBpy.models.Notice;
import com.OneBpy.models.User;
import com.OneBpy.services.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

//    @Query(value = "SELECT n.notice_id as noticeId, n.created_at as createdAt, n.title as title " +
//            ", n.content as content, n.expired as expired " +
//            ", p.product_name as productName, p.product_id as productId, p.bien_so_xe as bienSoXe " +
//            ", p.start_time as startTime " +
//            "FROM notice_tb n " +
//            "JOIN product_tb p ON n.product_id = p.product_id " +
//            "JOIN store_tb s ON p.store_id = s.store_id " +
//            "WHERE s.store_id = :store_id " +
//            "ORDER BY n.created_at DESC"
//            , nativeQuery = true)
    @Query(value = "new com.OneBpy.dtos.NoticeDTO(n.noticeId, p.productId, p.productName, p.price, p.startTime, p.bienSoXe, p.title, p.content, p.)" +
            "FROM Notice n " +
            "JOIN Product p ON n.product_id = p.product_id " +
            "JOIN Store s ON p.store_id = s.store_id " +
            "WHERE s.store_id = :store_id " +
            "ORDER BY n.created_at DESC"
            , nativeQuery = true)
    List<NoticeDTO> getAllStoreNotice(@Param("store_id") Long store_id);

    @Query(value = "SELECT n.* FROM notice_tb n ORDER BY n.last_update DESC", nativeQuery = true)
    List<Notice> getAllActiveNotice();
}
