# Marketly

Marketly is a feature-rich e-commerce platform where users can sign up, browse, and purchase products dynamically. Vendors can also list their products for sale after undergoing a verification process.

## Features

### User Functionality
- **Sign Up/Sign In**:
  - Using email and password.
  - Integration with Google for quick login.
- **User Profiles**:
  - Each user has a dedicated profile to manage account details and track orders.

### Product Search and Filtering
- **Dynamic Search**:
  - Search for products using keywords.
- **Advanced Filtering**:
  - Filter products by price, rating, and categories.

### Vendor Functionality
- Vendors can sign up and submit their products for sale.
- Vendor accounts are activated after an information verification process.

### Checkout and Payment
- Users can add products to their cart and proceed to checkout.
- Payments are processed on delivery.

## Technology Stack

### Frontend
- **React**: For creating a dynamic and responsive user interface.

### Backend
- **Spring Boot**: To handle API development and server-side logic.
- **Hibernate**: For managing database operations with ease.

### Database
- **H2 Database**: Lightweight and efficient in-memory database.

## How to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/marketly.git
   cd marketly
   ```

2. **Backend Setup**:
   - Navigate to the backend directory:
     ```bash
     cd backend
     ```
   - Run the Spring Boot application:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Frontend Setup**:
   - Navigate to the frontend directory:
     ```bash
     cd frontend
     ```
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the React application:
     ```bash
     npm start
     ```

## Future Enhancements
- Adding online payment integration.
- Implementing product recommendations based on user behavior.
- Extending the search functionality to support voice search.



