package com.orik.adminapi.data.DAO;

import com.orik.adminapi.data.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {}