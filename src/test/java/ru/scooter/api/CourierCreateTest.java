package ru.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.api.client.CourierClient;
import ru.scooter.api.model.Courier;
import ru.scooter.api.model.CourierCredentials;

import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import io.qameta.allure.Step;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;
    private String uniqueLogin;
    private String savedLogin;
    private String savedPassword;
    private static final Random RANDOM = new Random();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        uniqueLogin = generateUniqueLogin();
    }

    @After
    @Step("Сохранение ID курьера после создания")
    public void saveCourierIdAfterCreation() {
        if (savedLogin != null && savedPassword != null) {
            courierId = courierClient.login(new CourierCredentials(savedLogin, savedPassword))
                    .extract().path("id");
        }
        cleanupCreatedCourier();
    }

    private String generateUniqueLogin() {
        return "TestCourier_" + RANDOM.nextInt(100000);
    }

    @Step("Очистка: удаление созданного курьера")
    private void cleanupCreatedCourier() {
        if (courierId != 0) {
            try {
                courierClient.delete(courierId);
            } catch (Exception e) {
                System.err.println("Не удалось удалить курьера с ID: " + courierId + ", ошибка: " + e.getMessage());

            }
        }
    }

    @Feature("Работа с курьерами")
    @Story("Создать курьера с уникальными данными")
    @Test
    public void courierCanBeCreated() {
        // Arrange
        Courier courier = new Courier(uniqueLogin, "password123", "TestName");

        // Act
        ValidatableResponse response = courierClient.create(courier);

        // Assert
        response
                .statusCode(SC_CREATED)
                .body("ok", is(true));

        savedLogin = uniqueLogin;
        savedPassword = "password123";
    }

    @Feature("Валидация данных курьера")
    @Story("Попытка создать двух курьеров с одинаковым логином")
    @Test
    public void cannotCreateTwoIdenticalCouriers() {
        // Arrange
        Courier courier = new Courier(uniqueLogin, "password123", "TestName");

        // Act — создание первого курьера
        courierClient.create(courier);

        // Act — создание такого же
        ValidatableResponse dupResponse = courierClient.create(courier);

        // Assert — ожидание ошибки конфликта
        dupResponse
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        savedLogin = uniqueLogin;
        savedPassword = "password123";
    }

    @Feature("Валидация данных курьера")
    @Story("Попытка создать курьера без логина")
    @Test
    public void cannotCreateCourierWithoutLogin() {
        // Arrange — курьер без логина
        Courier invalidCourier = new Courier(null, "password123", "TestName");

        // Act
        ValidatableResponse response = courierClient.create(invalidCourier);

        // Assert — ожидаем ошибку валидации
        response
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Feature("Валидация данных курьера")
    @Story("Попытка создать курьера без пароля")
    @Test
    public void cannotCreateCourierWithoutPassword() {
        // Arrange — курьер без пароля
        Courier invalidCourier = new Courier(uniqueLogin, null, "TestName");

        // Act
        ValidatableResponse response = courierClient.create(invalidCourier);

        // Assert — ожидаем ошибку валидации
        response
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
