package sn.babacar.alten.test.entities;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String firstname;

  @Column(unique = true)
  private String email;

  private String password;

  @Column(name = "created_at")
  private Long createdAt;
  @Column(name = "updated_at")
  private Long updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = System.currentTimeMillis();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = System.currentTimeMillis();
  }
}