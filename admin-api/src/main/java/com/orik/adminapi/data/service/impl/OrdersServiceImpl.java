package com.orik.adminapi.data.service.impl;

import com.orik.adminapi.DTO.OrderUpdateDTO;
import com.orik.adminapi.data.DAO.OrderRepository;
import com.orik.adminapi.data.entity.Consultation;
import com.orik.adminapi.data.entity.Order;
import com.orik.adminapi.data.service.interfaces.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;
    @Override
    public Page<Order> getAllOrdersSorted(Integer page, Integer size, String sortField, String sortOrder) {
        String[] splitFields = sortField.split("-");
        Sort sort = null;

        for (String field : splitFields) {
            Sort currentSort = Sort.by(
                    sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    field
            );

            if (sort == null) {
                sort = currentSort;
            } else {
                sort = sort.and(currentSort);
            }
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageRequest);
    }

    @Override
    public Page<Order> findByFieldContainingIgnoreCase(String fieldName, String searchValue, Pageable pageable) {
        return orderRepository.findByFieldContainingIgnoreCase(fieldName, searchValue, pageable);
    }

    @Override
    public void update(OrderUpdateDTO orderUpdate) {
        Order order = orderRepository.findById(orderUpdate.getId()).get();
        order.setStatus(orderUpdate.getStatusType().name());
        order.setServerType(orderUpdate.getServerType().name());
        orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
