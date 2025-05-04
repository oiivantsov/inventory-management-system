DROP DATABASE IF EXISTS InventoryManager;
CREATE DATABASE InventoryManager;
USE InventoryManager;

CREATE TABLE IF NOT EXISTS User (
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role ENUM('ADMIN', 'USER') DEFAULT 'USER',
  PRIMARY KEY (username)
);

CREATE TABLE Supplier (
    supplier_id   INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    phone_number  VARCHAR(20),
    address       VARCHAR(255)
);

CREATE TABLE Customer (
    customer_id   INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    phone_number  VARCHAR(20),
    address       VARCHAR(255)
);

CREATE TABLE Product (
    product_id    INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(79) NOT NULL,
    category         VARCHAR(50),
    brand            VARCHAR(50),
    description     TEXT,
    quantity        INT NOT NULL DEFAULT 0
);

CREATE TABLE PurchaseOrder (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_id      INT,
    order_date       DATETIME DEFAULT CURRENT_TIMESTAMP,
    status           ENUM('pending', 'in_progress', 'completed') DEFAULT 'pending',
    order_total     DOUBLE DEFAULT 0,
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
);

CREATE TABLE PurchaseOrderItems (
    order_id  INT NOT NULL,
    product_id        INT NOT NULL,
    quantity         INT NOT NULL CHECK (quantity > 0),
    unit_price      double NOT NULL CHECK (unit_price >= 0),
    sale             DOUBLE NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES PurchaseOrder(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
CREATE TABLE SalesOrder (
    order_id INT PRIMARY KEY AUTO_INCREMENT, 
    customer_id   INT,
    order_date    DATETIME DEFAULT CURRENT_TIMESTAMP,
    status       ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    order_total  DOUBLE DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE SalesOrderItems (
    order_id   INT NOT NULL,
    product_id      INT NOT NULL,
    quantity       INT NOT NULL CHECK (quantity > 0),
    unit_price   DOUBLE NOT NULL CHECK (unit_price >= 0),
    sale             DOUBLE NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);