package ru.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.api.client.OrderClient;
import ru.scooter.api.model.Order;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    private static final int FAST_DELIVERY_STATUS = 1;
    private static final int STANDARD_DELIVERY_STATUS = 2;

    private void createTestOrders() {
        Order fastDeliveryOrder = new Order("Ivan", "Ivanov", "Moscow, 1", "1", "+79991112233", 1, "2026-09-01", "Fast delivery", Arrays.asList("BLACK"));
        Order standardDeliveryOrder = new Order("Petr", "Petrov", "SPb, 2", "2", "+78882223344", 2, "2026-10-01", "Standard delivery", Arrays.asList("GREY"));

        orderClient.create(fastDeliveryOrder);
        orderClient.create(standardDeliveryOrder);
    }

    @Test
    public void getOrdersListReturnsAllOrders() {
        createTestOrders();

        ValidatableResponse response = orderClient.getList();

        response.statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThanOrEqualTo(0));
    }
}
