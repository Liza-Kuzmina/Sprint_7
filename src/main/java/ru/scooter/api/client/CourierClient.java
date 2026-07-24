package ru.scooter.api.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.scooter.api.model.Courier;
import ru.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    private RequestSpecification getBaseSpec() {
        return given()
                .baseUri(ApiConfig.BASE_URI)
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json");
    }

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return getBaseSpec()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return getBaseSpec()
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse delete(int courierId) {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }
}
