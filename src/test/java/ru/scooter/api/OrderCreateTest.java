package ru.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.scooter.api.client.OrderClient;
import ru.scooter.api.model.Order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private OrderClient orderClient;
    private final List<String> colors;
    private static final String CUSTOMER_NAME = "Naruto";
    private static final String FAMILY_NAME = "Uzumaki";
    private static final String ADDRESS = "Konoha, 14";
    private static final String PHONE = "+79991112233";
    private static final int CUBE_COUNT = 5;
    private static final String DELIVERY_DATE = "2026-09-01";
    private static final String COMMENT = "Fast delivery";

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Order with colors: {0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                { Collections.singletonList("BLACK") },
                { Collections.singletonList("GREY") },
                { Arrays.asList("BLACK", "GREY") },
                { Collections.emptyList() }
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void createOrderWithDifferentColors() {
        System.out.println("Тестируем цвета: " + colors);
        // Arrange — готовим заказ с разными цветами
        Order order = createTestOrder();

        // Act — создаём заказ
        ValidatableResponse response = orderClient.create(order);

        // Assert — проверяем успешный ответ
        response
                .statusCode(201)
                .body("track", notNullValue());
    }

    private Order createTestOrder() {
        return new Order(
                CUSTOMER_NAME,
                FAMILY_NAME,
                ADDRESS,
                "4",
                PHONE,
                CUBE_COUNT,
                DELIVERY_DATE,
                COMMENT,
                colors
        );
    }
}
