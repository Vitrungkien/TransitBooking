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

    @Query("select new com.OneBpy.dtos.NoticeDTO(n.noticeId, n.createdAt, n.title, n.content, " +
            "n.expired, p.productName, p.productId, p.bienSoXe, p.startTime) " +
            "FROM Notice n " +
            "JOIN Product p ON n.product.productId = p.productId " +
            "JOIN Store s on p.store.storeId = s.storeId " +
            "WHERE s.storeId = :store_id " +
            "ORDER BY n.createdAt DESC ")
    List<NoticeDTO> getAllStoreNotice(@Param("store_id") Long store_id);

    @Query(value = "SELECT n.* FROM notice_tb n ORDER BY n.last_update DESC", nativeQuery = true)
    List<Notice> getAllActiveNotice();
}
