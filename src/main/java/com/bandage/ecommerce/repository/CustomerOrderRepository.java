package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.CustomerOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserOrderByOrderDateDesc(AppUser user);
}
