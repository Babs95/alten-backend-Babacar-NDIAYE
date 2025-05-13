package sn.babacar.alten.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.babacar.alten.test.entities.Product;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByCode(String code);
  boolean existsByCode(String code);
}