# Clothing Store E-Commerce API
A Spring Boot REST API backend for an e-commerce clothing store. Users can browse products by category, manage their shopping cart, update their profile, and place orders. Administrators can manage products and categories.

## Features
- User registration and login with JWT authentication
- Browse and search products by category, price range, and subcategory
- Shopping cart management (add, update, remove items)
- User profile management
- Checkout and order creation
- Admin-only product and category management

## Database Diagram
![Database diagram.png](Database%20diagram.png)

## API Endpoints

### Authentication
| Method | URL | Description |
|---|---|---|
| POST | /register | Register a new user |
| POST | /login | Login and receive JWT token |

### Categories
| Method | URL | Description |
|---|---|---|
| GET | /categories | Get all categories |
| GET | /categories/{id} | Get category by id |
| GET | /categories/{id}/products | Get products by category |
| POST | /categories | Create category (Admin only) |
| PUT | /categories/{id} | Update category (Admin only) |
| DELETE | /categories/{id} | Delete category (Admin only) |

### Products
| Method | URL | Description |
|---|---|---|
| GET | /products | Search/filter products |
| GET | /products/{id} | Get product by id |
| POST | /products | Create product (Admin only) |
| PUT | /products/{id} | Update product (Admin only) |
| DELETE | /products/{id} | Delete product (Admin only) |

### Shopping Cart
| Method | URL | Description |
|---|---|---|
| GET | /cart | Get current user's cart |
| POST | /cart/products/{id} | Add product to cart |
| PUT | /cart/products/{id} | Update product quantity |
| DELETE | /cart | Clear cart |

### Profile
| Method | URL | Description |
|---|---|---|
| GET | /profile | Get current user's profile |
| PUT | /profile | Update current user's profile |

### Orders
| Method | URL | Description |
|---|---|---|
| POST | /orders | Checkout and create order |

## Bugs Fixed
- **Bug 1** — Product search was filtering by `isFeatured` on every request, causing most products to never appear in search results
- **Bug 2** — Product update was missing `setStock()`, so stock quantity changes were silently ignored

## Technical Highlights
- Built with Spring Boot and Spring Security using JWT authentication
- Layered architecture: Controller → Service → Repository-> Model
- JPA/Hibernate with MySQL database
- Role based access control (USER and ADMIN roles)
- Shopping cart stored in database per user session
- Order checkout pulls shipping address from user profile automatically

## How to Run

### Prerequisites
- Java 17
- MySQL
- IntelliJ IDEA

### Setup
1. Clone the repository
```bash
   git clone https://github.com/jolienguyen0831/ecommerce-api.git
```
2. Run the database script  `database/create_datebase_clothingstore.sql` in MySQL Workbench

3. Open the project in IntelliJ

4. Run `ECommerceApplication.java`

5. API is available at `http://localhost:8080`

### Test with Insomnia
1. A starter Insomnia collection is included in the project root as `capstone-insomnia-collection.yaml`
2. Import it into Insomnia via **File → Import** to get all endpoints pre-configured

## Interesting Code

```java
// OrderService.java - Checkout cart 
@Transactional
public Order checkout(int userId)
{
    ShoppingCart cart = shoppingCartService.getByUserId(userId);
    
    //handle the case cart is empty
    if (cart.getItems().isEmpty())
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot checkout with an empty cart.");

    Order order = new Order();
    order.setUserId(userId);
    order.setDate(LocalDateTime.now());

    Order savedOrder = orderRepository.save(order);

    for (ShoppingCartItem cartItem : cart.getItems().values())
    {
        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setOrderId(savedOrder.getOrderId());
        lineItem.setProductId(cartItem.getProductId());
        lineItem.setSalesPrice(cartItem.getProduct().getPrice());
        lineItem.setQuantity(cartItem.getQuantity());
        orderLineItemRepository.save(lineItem);
    }

    shoppingCartService.clearCart(userId);
    return savedOrder;
}
```

## Future Improvements
- Add order history endpoint so users can view past orders
- Implement discount/coupon code system
- Add email confirmation on successful checkout
- Implement more front end code. 