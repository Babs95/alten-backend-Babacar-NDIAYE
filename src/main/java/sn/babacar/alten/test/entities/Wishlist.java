package sn.babacar.alten.test.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wishlists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  @JoinTable(
      name = "wishlist_products",
      joinColumns = @JoinColumn(name = "wishlist_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  private List<Product> products = new ArrayList<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
