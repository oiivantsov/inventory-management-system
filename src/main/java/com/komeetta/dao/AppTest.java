package com.komeetta.dao;
import com.komeetta.model.*;
import com.komeetta.service.PurchaseService;
import com.komeetta.service.SalesService;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class AppTest {
    public static void main(String[] args) {
        // Load environment variables
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        // Initialize DAOs and services
        CustomerDAO customerDAO = new CustomerDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        ProductDAO productDAO = new ProductDAO();
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
        SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
        PurchaseService purchaseService = new PurchaseService();
        SalesService salesService = new SalesService();

        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplier.setEmail("supplier@mail.com");
        supplier.setPhoneNumber("987654321");
        supplier.setAddress("456 Elm St");
        supplierDAO.addSupplier(supplier);

        // Create products
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(0); // Initial stock is 0
        productDAO.addProduct(laptop);

        Product phone = new Product();
        phone.setName("Phone");
        phone.setBrand("Apple");
        phone.setCategory("Electronics");
        phone.setQuantity(0); // Initial stock is 0
        productDAO.addProduct(phone);

        // Create a purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        // Create purchase order items
        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder, laptop, 10, 500.00, 0);
        PurchaseOrderItem item2 = new PurchaseOrderItem(purchaseOrder, phone, 20, 1000.00, 0);

        // Process purchase order
        purchaseService.processPurchaseOrder(purchaseOrder, List.of(item1, item2));

        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@mail.com");
        customer.setPhoneNumber("123456789");
        customer.setAddress("123 Main St");
        customerDAO.addCustomer(customer);

        // Select products for sale
        laptop = productDAO.getProductById(1);
        System.out.println("Stock level for product " + laptop.getName() + ": " + laptop.getQuantity());
        phone = productDAO.getProductById(2);
        System.out.println("Stock level for product " + phone.getName() + ": " + phone.getQuantity());

        // Create a sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrderItem saleItem1 = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);
        SalesOrderItem saleItem2 = new SalesOrderItem(salesOrder, phone, 1, 1200.00, 0);

        // Process sales order
        salesService.processSalesOrder(salesOrder, List.of(saleItem1, saleItem2));

        // show all suppliers
        System.out.println("Suppliers:");
        supplierDAO.getSuppliers().forEach(System.out::println);

        // show suppliers' names
        System.out.println("Suppliers' names:");
        supplierDAO.getSuppliers().forEach(supplier1 -> System.out.println(supplier1.getName()));

        // show all customers (objects)
        System.out.println("Customers:");
        customerDAO.getCustomers().forEach(System.out::println);

        // show customers' names
        System.out.println("Customers' names:");
        customerDAO.getCustomers().forEach(customer1 -> System.out.println(customer1.getName()));

        // show all products
        System.out.println("Products:");
        productDAO.getProducts().forEach(System.out::println);

        // show all products' names
        System.out.println("Products' names:");
        productDAO.getProducts().forEach(product -> System.out.println(product.getName()));

        // show all purchase orders
        System.out.println("Purchase Orders:");
        purchaseOrderDAO.getPurchaseOrders().forEach(System.out::println);

        // show all sales orders
        System.out.println("Sales Orders:");
        salesOrderDAO.getSalesOrders().forEach(System.out::println);

        // show items in purchase order
        System.out.println("Items in Purchase Order with id 1:");
        purchaseOrderDAO.getPurchaseOrder(1).getItems().forEach(System.out::println);

        // show items in sales order
        System.out.println("Items in Sales Order with id 1:");
        salesOrderDAO.getSalesOrder(1).getItems().forEach(System.out::println);

        // sum of all purchase orders
        System.out.println("Sum of all purchase orders: " + purchaseOrderDAO.getTotalPurchaseOrders());

        // sum of all sales orders
        System.out.println("Sum of all sales orders: " + salesOrderDAO.getTotalSaleOrders());

    }
}
