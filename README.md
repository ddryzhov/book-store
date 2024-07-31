# 📖 BookStore: Your Ultimate Online Bookstore 📖
___
Welcome to BookStore – your go-to spot for all things books. We created this platform to make it easy for book lovers to find, buy, and manage their favorite reads. Whether you read occasionally or are a dedicated bookworm, BookStore simplifies the process and brings your next great read right to you.
___
## 🚀 Technologies and Tools
This project is built using a robust stack of technologies designed to ensure reliability, scalability, and security. Here's a glimpse of what powers BookStore:

- **Spring Boot:** Simplifies the development and deployment process.
- **Spring Security:** Provides comprehensive security for your application.
- **Spring Data JPA:** Manages and accesses data with ease.
- **JWT:** Handles secure authentication and authorization.
- **Lombok:** Reduces boilerplate code with annotations.
- **MapStruct:** Automates the mapping of Java beans.
- **Swagger:** Enhances API documentation and testing.
- **MySQL:** Reliable relational database management system.
- **Liquibase:** Manages database schema changes.
- **Docker:** Streamlines development and deployment in isolated environments.
- **Docker Testcontainers:** Facilitates integration testing with a MySQL container.
___
## 🛠️ Project Endpoints
### **Here's a detailed look at the available endpoints of BookStore:**
## 🔐 Authentication
## Register a New User
### POST
`/api/auth/register` - Registering a new user available without any role.

**Example Request:**
```json
{
    "email": "john.doe@example.com",
    "password": "password123",
    "repeatPassword": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "shippingAddress": "123 Elm Street"
}
```
**Response:**
```json
{
    "id": 1,
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "shippingAddress": "123 Elm Street"
}
```
## User Login
### POST
`/api/auth/login` - Log in as a registered user and get a JWT token.

**Example Request:**
```json
{
    "email": "john.doe@example.com",
    "password": "password123"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjcwMDA3MzAwLCJleHBpIjoxNjcwMDEwOTAwfQ.s5cR4FiqF4FLO2D-QWq8EdJHrg7MPZbD_eS4Ff93C9k"
}
```

## 📚 Books
## Retrieve All Books
### GET
`/api/books` - Returns a list of all stored books accessible for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "isbn": "9780743273565",
        "price": 10.99,
        "description": "A classic novel of the Jazz Age.",
        "coverImage": "https://example.com/newbook.jpg",
        "categoryIds": [
          1, 
          2
        ]
    }
]
```
## Search Books
### GET
`/api/books/search` - Book search by specified parameters, available for User, Admin roles.

**Example Request:**
`/api/books/search?title=The Great Gatsby&author=F. Scott Fitzgerald`

**Response:**
```json
[
    {
        "id": 1,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "isbn": "9780743273565",
        "price": 10.99,
        "description": "A classic novel of the Jazz Age.",
        "coverImage": "https://example.com/newbook.jpg",
        "categoryIds": [
          1,
          2
        ]
    }
]
```

## Add a New Book
### POST
`/api/books` - Creates a new book in the database available to the Admin role.

**Example Request:**

```json
{
    "title": "New Book Title",
    "author": "New Author",
    "isbn": "1111111111111",
    "price": 15.99,
    "description": "A new exciting book.",
    "coverImage": "https://example.com/newbook.jpg",
    "categoryIds": [
      1
    ]
}
```
**Response:**
```json
{
    "id": 1,
    "title": "New Book Title",
    "author": "New author",
    "isbn": "1111111111111",
    "price": 15.99,
    "description": "A new exciting book.",
    "coverImage": "https://example.com/newbook.jpg",
    "categoryIds": [
      1
    ]
}
```

## Update a Book
### PUT
`/api/books/{id}` - Updates the book with the specified ID available to the Admin role.

**Example Request:**
```json
{
    "title": "Updated Title",
    "author": "Updated Author",
    "isbn": "1111111111111",
    "price": 17.99,
    "description": "Updated description.",
    "coverImage": "https://example.com/updatedbook.jpg",
    "categoryIds": [
      1
    ]
}
```
**Response:**
```json
{
    "id": 1,
    "title": "Updated Title",
    "author": "Updated Author",
    "isbn": "1111111111111",
    "price": 17.99,
    "description": "Updated description.",
    "coverImage": "https://example.com/updatedbook.jpg",
    "categoryIds": [
      1
    ]
}
```

## Delete a Book
### DELETE
`/api/books/{id}` - Soft-deletes from database a record with the specified id, accessible for role Admin.

## Get Book by id
### GET
`/api/books/{id}` - Gets the book with the specified identifier available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "title": "Updated Title",
    "author": "Updated Author",
    "isbn": "1111111111111",
    "price": 17.99,
    "description": "Updated description.",
    "coverImage": "https://example.com/updatedbook.jpg",
    "categoryIds": [
      1
    ]
}
```

## 🗂️ Categories
## Add a New Category
### POST
`/api/categories` - Creates a new category available to the Admin role.

**Example Request:**
```json
{
    "name": "Science Fiction",
    "description": "Fiction with scientific concepts."
}
```
**Response:**
```json
{
    "id": 1,
    "name": "Science Fiction",
    "description": "Fiction with scientific concepts."
}
```

## Retrieve Categories
### GET
`/api/categories` - Returns a list of all categories from the database available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "name": "Science Fiction",
        "description": "Fiction with scientific concepts."
    }
]
```

## Get Category by id
### GET
`/api/categories/{id}` - Returns the category that has the specified id value available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "name": "Science Fiction",
    "description": "Fiction with scientific concepts."
}
```

## Update a Category
### PUT
`/api/categories/{id}` - Updates the category with the specified id available to the Admin role.

**Example Request:**
```json
{
    "name": "Science",
    "description": "good"
}
```
**Response:**
```json
{
    "id": "1",
    "name": "Science",
    "description": "good"
}
```

## Delete a Category
### DELETE
`/api/categories/{id}` - Soft-deletes from database a record with the specified id, accessible for role Admin.

## Get Books by Category id
### GET
`/api/categories/{id}/books` - Returns a list of books that have some category available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "title": "Updated Title",
        "author": "Updated Author",
        "isbn": "1111111111111",
        "price": 17.99,
        "description": "Updated description.",
        "coverImage": "https://example.com/updatedbook.jpg"
    }
]
```

## 🛒 Shopping Cart
## Get User's Cart
### GET
`/api/cart` - Returns the current user's shopping cart available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "userId": 1,
    "cartItemsIds": [1, 2]
}
```

## Add Book to Cart
### POST
`/api/cart` - Add an item to the shopping cart available for User, Admin roles.

**Example Request:**
```json
{
    "bookId": 1,
    "quantity": 2
}
```
**Response:**
```json
{
    "id": 1,
    "userId": 1,
    "cartItems": [
    {
        "id": 1,
        "bookId": 1,
        "bookTitle": "Example Book Title 1",
        "quantity": 2
    }
    ]
}
```

## Update Cart Item
### PUT
`/api/cart/items/{id}` - Update an item in the shopping cart available for User, Admin roles.

**Example Request:**
```json
{
    "quantity": 20
}
```
**Response:**
```json
{
    "bookId": 1,
    "quantity": 20
}
```

## Delete Cart Item
### DELETE
`/api/cart/items/{id}` - Remove an item from the shopping cart available for User, Admin roles.

## 📦 Orders
## Create an Order
### POST
`/api/orders` - Create a new order available for User, Admin roles.

**Example Request:**
```json
{
    "shippingAddress": "Avenue 456"
}
```
**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "order": [
      {
        "id": 1,
        "bookId": 1,
        "quantity": 1,
        "price": 12.00
      },
      {
        "id": 2,
        "bookId": 2,
        "quantity": 2,
        "price": 12.00
      }
    ],
    "orderDate": "2024-03-08 T17:53:05",
    "total": 36.00,
    "status": "PENDING"
  }
]
```

## Get orders
### GET
`/api/orders` - Returns a list of orders available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "userId": 1,
        "order": [
            {
                "id": 1,
                "bookId": 1,
                "quantity": 1,
                "price": 12.00
            },
            {
                "id": 2,
                "bookId": 2,
                "quantity": 2,
                "price": 12.00
            }
        ],
        "orderDate": "2024-03-08 T17:53:05",
        "total": 36.00,
        "status": "PENDING"
    }
]
```

## Update Order
### PUT
`/api/orders/{id}` - Updates the order status, available for Admin role.

**Example Request:**
```json
{
    "status": "SHIPPED"
}
```
**Response:**
```json
{
    "id": 1,
    "userId": 2,
    "order": [
        {
            "id": 1,
            "bookId": 1,
            "quantity": 1,
            "price": 12.00
        },
        {
            "id": 2,
            "bookId": 2,
            "quantity": 2,
            "price": 12.00
        }
    ],
    "orderDate": "2024-03-08 T17:53:05",
    "total": 36.00,
    "status": "SHIPPED"
}
```

## Get Order Items
### GET
`/api/orders/{id}/items` - Retrieves the list of items associated with a specific order available for User, Admin roles.
**Response:**
```json
[
    {
        "id": 1,
        "bookId": 1,
        "quantity": 1,
        "price": 12.00
    },
    {
        "id": 2,
        "bookId": 2,
        "quantity": 2,
        "price": 12.00
    }
]
```

## Get Order Item
### GET
`/api/orders/{id}/items/{id}` - Retrieves details for a specific item within a specific order available for User, Admin roles.
**Response:**
```json
{
    "id": 2,
    "bookId": 2,
    "quantity": 2,
    "price": 12.00
}
```
___
## 🌟 Challenges and Solutions
One of the main tasks was to integrate various components into a cohesive system. Ensuring that each technology interacted seamlessly with the others required careful planning and testing. By using a systematic approach and applying my expertise, I was able to overcome these challenges and create a stable and functional platform.
___
## 💡 Future Improvements
Here are some ideas for enhancing BookStore:

- Enhanced User Experience: Continuously refine the interface and functionality based on user feedback.
- Personalized Recommendations: Implement algorithms to suggest books based on user preferences.
- Mobile Optimization: Ensure full responsiveness across all devices.
- Social Features: Allow users to share their favorite books and reviews on social media.
- Community Features: Add forums or book clubs to engage users more deeply.
___
## 🎥 Video Demonstration
Watch a 2-4 minute demo of BookStore in action.

[![Watch the video](http://img.youtube.com/vi/uW5Xw5ZwBYs/0.jpg)](https://www.youtube.com/watch?v=uW5Xw5ZwBYs&t=5s)
___
## 📦 Postman Collection
You can explore our API using this Postman Collection. It includes all the endpoints described above with example requests and responses.
[Explore BookStore API on Postman](https://www.postman.com/science-geoscientist-94639784/workspace/bookstore/overview)
___
Thank you for exploring BookStore! We hope our platform enhances your reading experience and makes book shopping a breeze. Feel free to contribute or provide feedback to help us improve.