package ru.scooter.api.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.scooter.api.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDERS_PATH = "/api/v1/orders";

    private RequestSpecification getBaseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(ApiConfig.BASE_URI);
    }

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        return getBaseSpec()
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getList() {
        return getBaseSpec()
                .when()
                .get(ORDERS_PATH)
                .then();
    }
}
