## Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-repo-name.git
    cd your-repo-name
    ```

2. **Build the project**:
    If you're using Maven:
    ```bash
    ./mvnw clean install
    ```

3. **Run the application**:
    After building the project, you can run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

## API Endpoints

### Product Endpoints

- `POST /api/products` - **Create a new product**
  - Request Body: Product object with name, description, price, stock quantity, category, sku, date created and date updated.
  - Response: Product object.

- `GET /api/products/{id}` - **Retrieve a product by ID**
  - Path Parameter: `id` (Product ID).
  - Response: Product object.

- `PUT /api/products/{id}/price` - **Update the product price**
  - Path Parameter: `id` (Product ID).
  - Request Body: `{"price": double}` (new price).
  - Response: Updated Product object.

- `DELETE /api/products/{id}` - **Delete a product by ID**
  - Path Parameter: `id` (Product ID).
  - Response: HTTP Status 204 (No Content).

- `GET /api/products` - **Retrieve all products**
  - Response: List of Product objects.

- `GET /api/products/category/{category}` - **Filter products by category**
  - Path Parameter: `category` (Product category).
  - Response: List of Product objects.

- `GET /api/products/price` - **Filter products by price range**
  - Query Parameters: `minPrice`, `maxPrice`.
  - Response: List of Product objects.

- `GET /api/products/sort/name` - **Sort products by name**
  - Response: List of Product objects sorted by name.

- `GET /api/products/sort/stock` - **Sort products by stock quantity**
  - Response: List of Product objects sorted by stock.

- `PUT /api/products/{id}/stock` - **Update product stock quantity**
  - Path Parameter: `id` (Product ID).
  - Request Body: `{"stock": int}` (new stock quantity).
  - Response: Updated Product object.

### Security & Roles

The API uses role-based access control with the following roles:

- **Admin**: Can perform all operations.
- **Manager**: Can add, update, and view products, but cannot delete them.
- **User**: Can view products but cannot modify them.

### Error Handling & Custom Exceptions

The API has custom error handling to deal with specific scenarios. The following exceptions are thrown based on different situations:

- **ProductNotFoundException**: Thrown when a product is not found by its ID or when no products match the filter criteria.
- **NoProductsFoundException**: Thrown when no products exist in the inventory or when no products match the given search filters.
- **InvalidRequestException**: Thrown when an invalid request is made (e.g., missing price or stock information).
- **UserAccessDeniedException**: Thrown when a user attempts to perform an action they don't have permission to, such as accessing a restricted resource.

The `UserAccessDeniedHandler` class is responsible for logging access violations and returning appropriate responses.

#### Product Object

```json
{
  "id": 1,
  "name": "Product A",
  "description": "Description of Product A",
  "price": 100.0,
  "stock": 50,
  "category": "Electronics",
  "sku": "PROD-A123",
  "createdAt": "2024-12-10T12:00:00",
  "updatedAt": "2024-12-10T12:00:00"
}
