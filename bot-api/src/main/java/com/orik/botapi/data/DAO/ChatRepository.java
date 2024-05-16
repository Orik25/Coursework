package com.orik.botapi.data.DAO;

import com.orik.botapi.data.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,Long>{}