package com.orik.adminapi.data.DAO;

import com.orik.adminapi.data.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,Long>{}