package com.orik.adminapi.data.service.interfaces;

import com.orik.adminapi.data.entity.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsultationsService {
    Page<Consultation> getAllConsultationsSorted(Integer page, Integer size, String sortField, String sortOrder);
    Page<Consultation> findByFieldContainingIgnoreCase(String fieldName, String searchValue, Pageable pageable);
}
