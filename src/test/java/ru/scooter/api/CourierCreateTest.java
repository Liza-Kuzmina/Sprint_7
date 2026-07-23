package ru.scooter.api;

import io.restassured.response.ValidatableResponse;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.api.client.CourierClient;
import ru.scooter.api.model.Courier;
import ru.scooter.api.model.CourierCredentials;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;


public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;
    private String uniqueLogin;
    private static final Random RANDOM = new Random();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        uniqueLogin = generateUniqueLogin();
    }

    @After
    public void tearDown() {
        cleanupCreatedCourier();
    }

    private String generateUniqueLogin() {
        return "TestCourier_" + RANDOM.nextInt(100000);
    }

    private void cleanupCreatedCourier() {
        if (courierId != 0) {
            try {
                courierClient.delete(courierId);
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void courierCanBeCreated() {
        // Arrange
        Courier courier = new Courier(uniqueLogin, "password123", "TestName");

        // Act
        ValidatableResponse response = courierClient.create(courier);

        // Assert
        response
                .statusCode(201)
                .body("ok", is(true));

        // Сохранение ID для очистки
        courierId = courierClient.login(new CourierCredentials(uniqueLogin, "password123"))
                .extract().path("id");
    }

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
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierClient.login(new CourierCredentials(uniqueLogin, "password123"))
                .extract().path("id");
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        // Arrange — курьер без логина
        Courier invalidCourier = new Courier(null, "password123", "TestName");

        // Act
        ValidatableResponse response = courierClient.create(invalidCourier);

        // Assert — ожидаем ошибку валидации
        response
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {
        // Arrange — курьер без пароля
        Courier invalidCourier = new Courier(uniqueLogin, null, "TestName");

        // Act
        ValidatableResponse response = courierClient.create(invalidCourier);

        // Assert — ожидаем ошибку валидации
        response
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
