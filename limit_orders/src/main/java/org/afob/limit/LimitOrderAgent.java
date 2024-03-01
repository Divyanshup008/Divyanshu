package org.afob.limit;

import org.afob.execution.ExecutionClient;
import org.afob.execution.ExecutionClient.ExecutionException;
import org.afob.prices.PriceListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LimitOrderAgent implements PriceListener {

    private final ExecutionClient executionClient;
    private final Map<String, Order> orders = new HashMap<>();

    public LimitOrderAgent(final ExecutionClient ec) {
        this.executionClient = ec;
    }

    @Override
    public void priceTick(String productId, BigDecimal price) {
        // Check if there are any pending orders for this product
        if (orders.containsKey(productId)) {
            Order order = orders.get(productId);
            BigDecimal limit = order.getLimit();
            if (order.isBuy() && price.compareTo(limit) <= 0) {
                try {
                    executionClient.buy(productId, order.getAmount());
                    System.out.println("Bought " + order.getAmount() + " shares of " + productId + " at $" + price);
                } catch (ExecutionException e) {
                    System.err.println("Failed to execute buy order: " + e.getMessage());
                }
                orders.remove(productId);
            } else if (!order.isBuy() && price.compareTo(limit) >= 0) {
                try {
                    executionClient.sell(productId, order.getAmount());
                    System.out.println("Sold " + order.getAmount() + " shares of " + productId + " at $" + price);
                } catch (ExecutionException e) {
                    System.err.println("Failed to execute sell order: " + e.getMessage());
                }
                orders.remove(productId);
            }
        }
    }

    public void addOrder(boolean isBuy, String productId, int amount, BigDecimal limit) {
        orders.put(productId, new Order(isBuy, amount, limit));
    }

    private static class Order {
        private final boolean isBuy;
        private final int amount;
        private final BigDecimal limit;

        public Order(boolean isBuy, int amount, BigDecimal limit) {
            this.isBuy = isBuy;
            this.amount = amount;
            this.limit = limit;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public int getAmount() {
            return amount;
        }

        public BigDecimal getLimit() {
            return limit;
        }
    }
}
