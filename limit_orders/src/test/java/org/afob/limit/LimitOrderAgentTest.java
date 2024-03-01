package org.afob.limit;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.afob.execution.ExecutionClient;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LimitOrderAgent.class, ExecutionClient.class}) // List final classes to mock
public class LimitOrderAgentTest {

    private LimitOrderAgent limitOrderAgent;
    private ExecutionClient executionClient;

    @Before
    public void setUp() {
        executionClient = mock(ExecutionClient.class);
        limitOrderAgent = new LimitOrderAgent(executionClient);
    }

    @Test
    public void testBuyOrderExecution() throws ExecutionClient.ExecutionException {
        // Arrange
        String productId = "ABC";
        int amount = 100;
        BigDecimal price = BigDecimal.valueOf(45);
        BigDecimal limit = BigDecimal.valueOf(50);

        // Act
        limitOrderAgent.addOrder(true, productId, amount, limit);
        limitOrderAgent.priceTick(productId, price);

        // Assert
        verify(executionClient).buy(productId, amount);
    }

    @Test
    public void testSellOrderExecution() throws ExecutionClient.ExecutionException {
        // Arrange
        String productId = "XYZ";
        int amount = 200;
        BigDecimal price = BigDecimal.valueOf(70);
        BigDecimal limit = BigDecimal.valueOf(60);

        // Act
        limitOrderAgent.addOrder(false, productId, amount, limit);
        limitOrderAgent.priceTick(productId, price);

        // Assert
        verify(executionClient).sell(productId, amount);
    }
}
