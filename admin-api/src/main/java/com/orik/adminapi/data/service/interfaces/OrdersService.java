package com.orik.adminapi.data.service.interfaces;

import com.orik.adminapi.DTO.OrderUpdateDTO;
import com.orik.adminapi.data.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersService {
    Page<Order> getAllOrdersSorted(Integer page, Integer size, String sortField, String sortOrder);
    Page<Order> findByFieldContainingIgnoreCase(String fieldName, String searchValue, Pageable pageable);
    void update(OrderUpdateDTO orderUpdate);
    void deleteById(Long id);

}
