package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.CreditCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findByUserOrderByIdAsc(AppUser user);

    Optional<CreditCard> findByIdAndUser(Long id, AppUser user);
}
