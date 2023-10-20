DROP DATABASE bookstore;
CREATE DATABASE bookstore;
USE bookstore;

CREATE TABLE users (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    address VARCHAR(255),
    phone VARCHAR(20)
);

CREATE TABLE books (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50),
    author VARCHAR(50),
    publisher VARCHAR(50),
    category VARCHAR(50),
    isbn VARCHAR(13),
    price FLOAT,
    quantity INT,
    version BIGINT
);

CREATE TABLE orders (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    user INTEGER,
    total FLOAT,
    FOREIGN KEY (user) REFERENCES users (id)
);

CREATE TABLE carts (
   id INTEGER AUTO_INCREMENT PRIMARY KEY,
   user INTEGER,
   FOREIGN KEY (user) REFERENCES users (id)
);

CREATE TABLE cart_details (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    book INTEGER,
    price FLOAT,
    quantity INTEGER,
    cart INTEGER,
    subtotal FLOAT,
    version BIGINT,
    FOREIGN KEY (book) REFERENCES books (id),
    FOREIGN KEY (cart) REFERENCES carts (id)
);

CREATE TABLE order_details (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    book INTEGER,
    price FLOAT,
    quantity INTEGER,
    purchase INTEGER,
    subtotal FLOAT,
    FOREIGN KEY (book) REFERENCES books (id),
    FOREIGN KEY (purchase) REFERENCES orders (id)
);