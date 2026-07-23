package ru.scooter.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.scooter.api.model.Courier;
import ru.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    private RequestSpecification getBaseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(BASE_URI);
    }

    public ValidatableResponse create(Courier courier) {
        return getBaseSpec()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    public ValidatableResponse login(CourierCredentials credentials) {
        return getBaseSpec()
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(int courierId) {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }
}
