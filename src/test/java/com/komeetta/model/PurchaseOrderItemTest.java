package com.komeetta.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderItemTest {

    @Test
    void testConstructorAndGetters() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, product, 10, 20.5, 5.0);

        assertEquals(purchaseOrder, item.getPurchaseOrder());
        assertEquals(product, item.getProduct());
        assertEquals(10, item.getQuantity());
        assertEquals(20.5, item.getUnitPrice());
        assertEquals(5.0, item.getSale());
    }

    @Test
    void testSetters() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(2);

        Product product = new Product();
        product.setProductId(200);

        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, product, 5, 15.0, 2.0);

        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setOrderId(3);
        item.setPurchaseOrder(newOrder);
        assertEquals(newOrder, item.getPurchaseOrder());

        Product newProduct = new Product();
        newProduct.setProductId(300);
        item.setProduct(newProduct);
        assertEquals(newProduct, item.getProduct());

        item.setQuantity(8);
        assertEquals(8, item.getQuantity());

        item.setUnitPrice(25.0);
        assertEquals(25.0, item.getUnitPrice());

        item.setSale(7.5);
        assertEquals(7.5, item.getSale());
    }

    @Test
    void testEquals_SameValues_ShouldBeEqual() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder, product, 5, 10.0, 0);
        PurchaseOrderItem item2 = new PurchaseOrderItem(purchaseOrder, product, 5, 10.0, 0);

        assertEquals(item1, item2);
    }

    @Test
    void testEquals_DifferentValues_ShouldNotBeEqual() {
        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setOrderId(1);

        PurchaseOrder purchaseOrder2 = new PurchaseOrder();
        purchaseOrder2.setOrderId(2);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder1, product, 5, 10.0, 0);
        PurchaseOrderItem item2 = new PurchaseOrderItem(purchaseOrder2, product, 5, 10.0, 0);

        assertNotEquals(item1, item2);
    }

    @Test
    void testHashCode_SameValues_ShouldBeEqual() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder, product, 5, 10.0, 0);
        PurchaseOrderItem item2 = new PurchaseOrderItem(purchaseOrder, product, 5, 10.0, 0);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues_ShouldBeDifferent() {
        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setOrderId(1);

        PurchaseOrder purchaseOrder2 = new PurchaseOrder();
        purchaseOrder2.setOrderId(2);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder1, product, 5, 10.0, 0);
        PurchaseOrderItem item2 = new PurchaseOrderItem(purchaseOrder2, product, 5, 10.0, 0);

        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEquals_NullAndDifferentClass() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(1);

        Product product = new Product();
        product.setProductId(100);

        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, product, 5, 10.0, 0);

        assertNotEquals(null, item);
        assertNotEquals("Not a PurchaseOrderItem", item);
    }

    @Test
    void testConstructor_ShouldThrowException_WhenOrderOrProductNotPersisted() {
        PurchaseOrder nonPersistedOrder = new PurchaseOrder();
        Product nonPersistedProduct = new Product();

        Exception exception1 = assertThrows(IllegalStateException.class, () ->
                new PurchaseOrderItem(nonPersistedOrder, nonPersistedProduct, 5, 10.0, 0)
        );
        assertTrue(exception1.getMessage().contains("PurchaseOrder must be persisted"));

        Exception exception2 = assertThrows(IllegalStateException.class, () -> {
            nonPersistedOrder.setOrderId(1);
            new PurchaseOrderItem(nonPersistedOrder, nonPersistedProduct, 5, 10.0, 0);
        });
        assertTrue(exception2.getMessage().contains("Product must be persisted"));
    }
}
