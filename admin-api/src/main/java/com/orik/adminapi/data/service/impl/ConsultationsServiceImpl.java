package com.orik.adminapi.data.service.impl;

import com.orik.adminapi.data.DAO.ConsultationRepository;
import com.orik.adminapi.data.entity.Consultation;
import com.orik.adminapi.data.service.interfaces.ConsultationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsultationsServiceImpl implements ConsultationsService {

    private final ConsultationRepository consultationRepository;
    @Override
    public Page<Consultation> getAllConsultationsSorted(Integer page, Integer size, String sortField, String sortOrder) {
        Sort sort = Sort.by(sortField);

        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return consultationRepository.findAll(pageRequest);
    }

    @Override
    public Page<Consultation> findByFieldContainingIgnoreCase(String fieldName, String searchValue, Pageable pageable) {
        return consultationRepository.findByFieldContainingIgnoreCase(fieldName, searchValue, pageable);
    }
}
