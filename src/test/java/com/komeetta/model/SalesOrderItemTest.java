package com.komeetta.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalesOrderItemTest {

    @Test
    void testConstructorAndGetters() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(100);

        SalesOrderItem salesOrderItem = new SalesOrderItem(salesOrder, product, 5, 50.0, 10.0);

        assertEquals(salesOrder, salesOrderItem.getSalesOrder());
        assertEquals(product, salesOrderItem.getProduct());
        assertEquals(5, salesOrderItem.getQuantity());
        assertEquals(50.0, salesOrderItem.getUnitPrice());
        assertEquals(10.0, salesOrderItem.getSale());
        assertEquals(new OrderItemId(1, 100), salesOrderItem.getId());
    }

    @Test
    void testSetters() {
        SalesOrderItem salesOrderItem = new SalesOrderItem();

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(2);

        Product product = new Product();
        product.setProductId(200);

        salesOrderItem.setSalesOrder(salesOrder);
        salesOrderItem.setProduct(product);
        salesOrderItem.setQuantity(10);
        salesOrderItem.setUnitPrice(100.0);
        salesOrderItem.setSale(5.0);
        salesOrderItem.setId(new OrderItemId(2, 200));

        assertEquals(salesOrder, salesOrderItem.getSalesOrder());
        assertEquals(product, salesOrderItem.getProduct());
        assertEquals(10, salesOrderItem.getQuantity());
        assertEquals(100.0, salesOrderItem.getUnitPrice());
        assertEquals(5.0, salesOrderItem.getSale());
        assertEquals(new OrderItemId(2, 200), salesOrderItem.getId());
    }

    @Test
    void testEquals_SameValues_ShouldBeEqual() {
        SalesOrder salesOrder1 = new SalesOrder();
        salesOrder1.setOrderId(3);

        Product product1 = new Product();
        product1.setProductId(300);

        SalesOrder salesOrder2 = new SalesOrder();
        salesOrder2.setOrderId(3);

        Product product2 = new Product();
        product2.setProductId(300);

        SalesOrderItem item1 = new SalesOrderItem(salesOrder1, product1, 5, 50.0, 10.0);
        SalesOrderItem item2 = new SalesOrderItem(salesOrder2, product2, 5, 50.0, 10.0);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEquals_DifferentValues_ShouldNotBeEqual() {
        SalesOrder salesOrder1 = new SalesOrder();
        salesOrder1.setOrderId(4);

        Product product1 = new Product();
        product1.setProductId(400);

        SalesOrder salesOrder2 = new SalesOrder();
        salesOrder2.setOrderId(5);

        Product product2 = new Product();
        product2.setProductId(500);

        SalesOrderItem item1 = new SalesOrderItem(salesOrder1, product1, 5, 50.0, 10.0);
        SalesOrderItem item2 = new SalesOrderItem(salesOrder2, product2, 5, 50.0, 10.0);

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEquals_NullAndDifferentClass() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(6);

        Product product = new Product();
        product.setProductId(600);

        SalesOrderItem item = new SalesOrderItem(salesOrder, product, 5, 50.0, 10.0);

        assertNotEquals(null, item);
        assertNotEquals("Not a SalesOrderItem", item);
    }

    @Test
    void testConstructor_ShouldThrowExceptionIfSalesOrderNotPersisted() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(0); // Not persisted

        Product product = new Product();
        product.setProductId(1);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                new SalesOrderItem(salesOrder, product, 5, 50.0, 10.0)
        );

        assertEquals("SalesOrder must be persisted before creating SalesOrderItem", exception.getMessage());
    }

    @Test
    void testConstructor_ShouldThrowExceptionIfProductNotPersisted() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(0); // Not persisted

        Exception exception = assertThrows(IllegalStateException.class, () ->
                new SalesOrderItem(salesOrder, product, 5, 50.0, 10.0)
        );

        assertEquals("Product must be persisted before creating SalesOrderItem", exception.getMessage());
    }
}
