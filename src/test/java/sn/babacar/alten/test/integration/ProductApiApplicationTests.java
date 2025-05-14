package sn.babacar.alten.test.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.babacar.alten.test.util.TestUtil.asJsonString;
import static sn.babacar.alten.test.util.TestUtil.objectMapper;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.repositories.ProductRepository;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.services.mapper.ProductMapper;
import sn.babacar.alten.test.util.Constants;
import sn.babacar.alten.test.util.testcontainer.PostgresTestContainerInitializer;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ProductApiApplicationTests extends PostgresTestContainerInitializer {

  private static final String API_PRODUCTS_BASE_PATH = "/v1/products";

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeAll
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    productRepository.deleteAll();
  }

  @Test
  void createProductSuccess() throws Exception {
    ProductDTO dto = new ProductDTO();
    dto.setCode("prod-001");
    dto.setName("New Product");
    dto.setDescription("Description of the new product");
    dto.setImage("new-product.jpg");
    dto.setCategory("Electronics");
    dto.setPrice(149.99);
    dto.setQuantity(25);
    dto.setInternalReference("INT-NEW-001");
    dto.setShellId(2001L);
    dto.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    dto.setRating(5);

    MvcResult mockResults = mockMvc.perform(
            post(API_PRODUCTS_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.USER_CREATED_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.CREATED))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_POST_SUCCESS))
        .andExpect(jsonPath("$.data.code").value("prod-001"))
        .andExpect(jsonPath("$.data.name").value("New Product"))
        .andDo(print())
        .andReturn();

    String json = mockResults.getResponse().getContentAsString();
    CustomResponse customResponse = objectMapper.readValue(json, CustomResponse.class);
    ProductDTO result = objectMapper.convertValue(customResponse.getData(), ProductDTO.class);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("prod-001", result.getCode());
    assertEquals("New Product", result.getName());
    assertEquals("Description of the new product", result.getDescription());
    assertEquals("Electronics", result.getCategory());
    assertEquals(149.99, result.getPrice());
    assertEquals(25, result.getQuantity());
    assertEquals("INT-NEW-001", result.getInternalReference());
    assertEquals(2001L, result.getShellId());
    assertEquals(Product.InventoryStatus.INSTOCK, result.getInventoryStatus());
    assertEquals(5, result.getRating());
  }

  @Test
  void getProductByIdSuccess() throws Exception {
    // Create a product first
    Product product = new Product();
    product.setCode("prod-002");
    product.setName("Product for Get");
    product.setDescription("This is a test product for get");
    product.setImage("product-002.jpg");
    product.setCategory("Home Appliances");
    product.setPrice(199.99);
    product.setQuantity(50);
    product.setInternalReference("INT-GET-002");
    product.setShellId(2002L);
    product.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product.setRating(4);
    Product savedProduct = productRepository.save(product);

    MvcResult mockResults = mockMvc.perform(
            get(API_PRODUCTS_BASE_PATH + "/{id}", savedProduct.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.SUCCESS_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.OK))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_GET_SUCCESS))
        .andExpect(jsonPath("$.data.code").value("prod-002"))
        .andExpect(jsonPath("$.data.name").value("Product for Get"))
        .andDo(print())
        .andReturn();

    String json = mockResults.getResponse().getContentAsString();
    CustomResponse customResponse = objectMapper.readValue(json, CustomResponse.class);
    ProductDTO result = objectMapper.convertValue(customResponse.getData(), ProductDTO.class);

    assertNotNull(result);
    assertEquals(savedProduct.getId(), result.getId());
    assertEquals("prod-002", result.getCode());
    assertEquals("Product for Get", result.getName());
    assertEquals("This is a test product for get", result.getDescription());
    assertEquals("Home Appliances", result.getCategory());
    assertEquals(199.99, result.getPrice());
    assertEquals(50, result.getQuantity());
  }

  @Test
  void getAllProductsSuccess() throws Exception {
    // Create a few products
    Product product1 = new Product();
    product1.setCode("prod-003");
    product1.setName("Product 3");
    product1.setDescription("Description of product 3");
    product1.setImage("product-003.jpg");
    product1.setCategory("Books");
    product1.setPrice(39.99);
    product1.setQuantity(75);
    product1.setInternalReference("INT-BOOK-003");
    product1.setShellId(2003L);
    product1.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product1.setRating(3);
    productRepository.save(product1);

    Product product2 = new Product();
    product2.setCode("prod-004");
    product2.setName("Product 4");
    product2.setDescription("Description of product 4");
    product2.setImage("product-004.jpg");
    product2.setCategory("Clothing");
    product2.setPrice(49.99);
    product2.setQuantity(120);
    product2.setInternalReference("INT-CLOTH-004");
    product2.setShellId(2004L);
    product2.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product2.setRating(4);
    productRepository.save(product2);

    MvcResult mockResults = mockMvc.perform(
            get(API_PRODUCTS_BASE_PATH))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.SUCCESS_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.OK))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_GET_SUCCESS))
        .andExpect(jsonPath("$.data").isArray())
        .andDo(print())
        .andReturn();

    String json = mockResults.getResponse().getContentAsString();
    CustomResponse customResponse = objectMapper.readValue(json, CustomResponse.class);
    List<ProductDTO> results = objectMapper.convertValue(
        customResponse.getData(),
        new TypeReference<>() {
        }
    );

    assertNotNull(results);
    assertTrue(results.size() >= 2);
    assertTrue(results.stream().anyMatch(p -> p.getCode().equals("prod-003")));
    assertTrue(results.stream().anyMatch(p -> p.getCode().equals("prod-004")));
  }

  @Test
  void updateProductSuccess() throws Exception {
    // Create a product first
    Product product = new Product();
    product.setCode("prod-005");
    product.setName("Original Product Name");
    product.setDescription("Original description");
    product.setImage("product-005.jpg");
    product.setCategory("Sports");
    product.setPrice(59.99);
    product.setQuantity(30);
    product.setInternalReference("INT-SPORT-005");
    product.setShellId(2005L);
    product.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product.setRating(3);
    Product savedProduct = productRepository.save(product);

    ProductDTO updateDto = productMapper.toDto(savedProduct);
    updateDto.setName("Updated Product Name");
    updateDto.setDescription("Updated description");
    updateDto.setPrice(69.99);
    updateDto.setQuantity(45);
    updateDto.setInventoryStatus(Product.InventoryStatus.LOWSTOCK);

    MvcResult mockResults = mockMvc.perform(
            patch(API_PRODUCTS_BASE_PATH + "/{id}", savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.SUCCESS_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.OK))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_PUT_SUCCESS))
        .andExpect(jsonPath("$.data.name").value("Updated Product Name"))
        .andDo(print())
        .andReturn();

    String json = mockResults.getResponse().getContentAsString();
    CustomResponse customResponse = objectMapper.readValue(json, CustomResponse.class);
    ProductDTO result = objectMapper.convertValue(customResponse.getData(), ProductDTO.class);

    assertNotNull(result);
    assertEquals("Updated Product Name", result.getName());
    assertEquals("Updated description", result.getDescription());
    assertEquals(69.99, result.getPrice());
    assertEquals(45, result.getQuantity());
    assertEquals(Product.InventoryStatus.LOWSTOCK, result.getInventoryStatus());
  }

  @Test
  void deleteProductSuccess() throws Exception {
    // Create a product first
    Product product = new Product();
    product.setCode("prod-006");
    product.setName("Product to Delete");
    product.setDescription("This product will be deleted");
    product.setImage("product-006.jpg");
    product.setCategory("Toys");
    product.setPrice(79.99);
    product.setQuantity(15);
    product.setInternalReference("INT-TOY-006");
    product.setShellId(2006L);
    product.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product.setRating(5);
    Product savedProduct = productRepository.save(product);

    mockMvc.perform(
            delete(API_PRODUCTS_BASE_PATH + "/{id}", savedProduct.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.SUCCESS_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.OK))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_DELETE_SUCCESS))
        .andDo(print());

    // Verify it's deleted
    assertFalse(productRepository.existsById(savedProduct.getId()));
  }

  @Test
  void getProductByIdNotFound() throws Exception {
    Long nonExistentId = 9999L;

    mockMvc.perform(
            get(API_PRODUCTS_BASE_PATH + "/{id}", nonExistentId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.NOT_FOUND_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.NOT_FOUND))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_GET_FAILURE_NOT_FOUND))
        .andDo(print());
  }

  @Test
  void updateProductNotFound() throws Exception {
    ProductDTO dto = new ProductDTO();
    dto.setName("Update Non-existent Product");
    dto.setCode("NONEXIST");
    dto.setQuantity(10);

    Long nonExistentId = 9999L;

    mockMvc.perform(
            patch(API_PRODUCTS_BASE_PATH + "/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.NOT_FOUND_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.NOT_FOUND))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_GET_FAILURE_NOT_FOUND))
        .andDo(print());
  }

  @Test
  void deleteProductNotFound() throws Exception {
    Long nonExistentId = 9999L;

    mockMvc.perform(
            delete(API_PRODUCTS_BASE_PATH + "/{id}", nonExistentId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.NOT_FOUND_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.NOT_FOUND))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_GET_FAILURE_NOT_FOUND))
        .andDo(print());
  }

  @Test
  void createProductDuplicateCode() throws Exception {
    // Create a product first
    Product product = new Product();
    product.setCode("duplicate-prod");
    product.setName("First Product");
    product.setDescription("First product with duplicate code");
    product.setImage("duplicate.jpg");
    product.setCategory("Office");
    product.setPrice(29.99);
    product.setQuantity(10);
    product.setInternalReference("INT-DUP-001");
    product.setShellId(2007L);
    product.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    product.setRating(4);
    productRepository.save(product);

    // Try to create another with the same code
    ProductDTO dto = new ProductDTO();
    dto.setCode("duplicate-prod");
    dto.setName("Second Product with Duplicate Code");
    dto.setDescription("Second product with duplicate code");
    dto.setImage("duplicate2.jpg");
    dto.setCategory("Office");
    dto.setPrice(39.99);
    dto.setQuantity(20);
    dto.setInternalReference("INT-DUP-002");
    dto.setShellId(2008L);
    dto.setInventoryStatus(Product.InventoryStatus.INSTOCK);
    dto.setRating(3);

    mockMvc.perform(
            post(API_PRODUCTS_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(Constants.Message.CONFLICT_BODY))
        .andExpect(jsonPath("$.statusCode").value(Constants.Status.CONFLICT))
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_POST_FAILURE_ENTITY_EXIST))
        .andDo(print());
  }


}