package com.orik.adminapi.data.DAO;

import com.orik.adminapi.data.entity.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {
    @Query(value = "SELECT * " +
            "FROM consultations " +
            "WHERE " +
            "CASE " +
            "WHEN :fieldName = 'phone_number' THEN LOWER(consultations.phone_number) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "WHEN :fieldName = 'message' THEN LOWER(consultations.message) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "END",
            countQuery = "SELECT COUNT(*) FROM consultations " +
                    "WHERE " +
                    "CASE " +
                    "WHEN :fieldName = 'phone_number' THEN LOWER(consultations.phone_number) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "WHEN :fieldName = 'message' THEN LOWER(consultations.message) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
                    "END",
            nativeQuery = true)
    Page<Consultation> findByFieldContainingIgnoreCase(@Param("fieldName") String fieldName, @Param("searchValue") String searchValue, Pageable pageable);
}