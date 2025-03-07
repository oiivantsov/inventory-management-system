package com.komeetta.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemIdTest {

    @Test
    void testConstructorAndGetters() {
        // Create an instance
        OrderItemId orderItemId = new OrderItemId(1, 100);

        // Verify values
        assertEquals(1, orderItemId.getOrderId());
        assertEquals(100, orderItemId.getProductId());
    }

    @Test
    void testSetters() {
        OrderItemId orderItemId = new OrderItemId();

        // Set values
        orderItemId.setOrderId(5);
        orderItemId.setProductId(200);

        // Verify values
        assertEquals(5, orderItemId.getOrderId());
        assertEquals(200, orderItemId.getProductId());
    }

    @Test
    void testEquals_SameValues_ShouldBeEqual() {
        OrderItemId id1 = new OrderItemId(10, 300);
        OrderItemId id2 = new OrderItemId(10, 300);

        assertEquals(id1, id2);
    }

    @Test
    void testEquals_DifferentValues_ShouldNotBeEqual() {
        OrderItemId id1 = new OrderItemId(10, 300);
        OrderItemId id2 = new OrderItemId(20, 400);

        assertNotEquals(id1, id2);
    }

    @Test
    void testHashCode_SameValues_ShouldBeEqual() {
        OrderItemId id1 = new OrderItemId(7, 150);
        OrderItemId id2 = new OrderItemId(7, 150);

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues_ShouldBeDifferent() {
        OrderItemId id1 = new OrderItemId(1, 50);
        OrderItemId id2 = new OrderItemId(2, 100);

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testEquals_NullAndDifferentClass() {
        OrderItemId id = new OrderItemId(1, 10);

        assertNotEquals(null, id);
        assertNotEquals("Not an OrderItemId", id);
    }
}
