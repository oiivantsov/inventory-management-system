package com.komeetta.model;

/**
 * OrderStatus enum represents the status of a sales order. It has the following values:
 * - PENDING: the order is pending
 * - IN_PROGRESS: the order is in progress
 * - COMPLETED: the order is completed
 */

public enum OrderStatus {
    PENDING{
        @Override
        public String toString() {
            return "pending";
        }
    },
    IN_PROGRESS{
        @Override
        public String toString() {
            return "in_progress";
        }
    },
    COMPLETED{
        @Override
        public String toString() {
            return "completed";
        }
    },
    completed{
        @Override
        public String toString() {return "completed";}
    }
}
