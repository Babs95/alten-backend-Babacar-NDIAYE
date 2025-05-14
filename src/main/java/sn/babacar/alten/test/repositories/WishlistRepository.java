package sn.babacar.alten.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.babacar.alten.test.entities.Wishlist;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  Optional<Wishlist> findByUserId(Long userId);
}
