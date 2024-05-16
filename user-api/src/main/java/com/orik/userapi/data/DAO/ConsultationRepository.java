package com.orik.userapi.data.DAO;

import com.orik.userapi.data.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {}