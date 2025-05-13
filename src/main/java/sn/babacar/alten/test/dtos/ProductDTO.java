package sn.babacar.alten.test.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.babacar.alten.test.entities.Product.InventoryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  private Long id;
  private String code;
  private String name;
  private String description;
  private String image;
  private String category;
  private Double price;
  private Integer quantity;
  private String internalReference;
  private Long shellId;
  private InventoryStatus inventoryStatus;
  private Integer rating;
  private Long createdAt;
  private Long updatedAt;
}