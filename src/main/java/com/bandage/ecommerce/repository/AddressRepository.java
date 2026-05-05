package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserOrderByIdAsc(AppUser user);

    Optional<Address> findByIdAndUser(Long id, AppUser user);
}
