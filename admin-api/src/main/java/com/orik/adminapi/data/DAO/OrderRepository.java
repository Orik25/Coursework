package com.orik.adminapi.data.DAO;

import com.orik.adminapi.data.entity.Consultation;
import com.orik.adminapi.data.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * " +
            "FROM orders " +
            "WHERE " +
            "CASE " +
            "WHEN :fieldName = 'phone_number' THEN LOWER(orders.phone_number) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'email' THEN LOWER(orders.email) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'server_type' THEN LOWER(orders.server_type) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'status' THEN LOWER(orders.status) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'message' THEN LOWER(orders.message) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'user' THEN (LOWER(orders.last_name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR LOWER(orders.first_name) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "OR " +
            "(LOWER(orders.last_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', -1), '%')) AND LOWER(orders.first_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', 1), '%'))) " +
            "OR " +
            "(LOWER(orders.first_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', -1), '%')) AND LOWER(orders.last_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', 1), '%'))) " +
            "END",
            countQuery = "SELECT COUNT(*) FROM orders " +
                    "WHERE " +
                    "CASE " +
                    "WHEN :fieldName = 'phone_number' THEN LOWER(orders.phone_number) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'email' THEN LOWER(orders.email) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'server_type' THEN LOWER(orders.server_type) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'status' THEN LOWER(orders.status) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'message' THEN LOWER(orders.message) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'user' THEN (LOWER(orders.last_name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR LOWER(orders.first_name) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
                    "OR " +
                    "(LOWER(orders.last_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', -1), '%')) AND LOWER(orders.first_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', 1), '%'))) " +
                    "OR " +
                    "(LOWER(orders.first_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', -1), '%')) AND LOWER(orders.last_name) LIKE LOWER(CONCAT('%', SPLIT_PART(:searchValue, ' ', 1), '%'))) " +
                    "END",
            nativeQuery = true)
    Page<Order> findByFieldContainingIgnoreCase(@Param("fieldName") String fieldName, @Param("searchValue") String searchValue, Pageable pageable);
}