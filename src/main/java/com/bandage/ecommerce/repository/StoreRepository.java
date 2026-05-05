package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
