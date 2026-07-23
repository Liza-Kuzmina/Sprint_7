package ru.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.api.client.CourierClient;
import ru.scooter.api.model.Courier;
import ru.scooter.api.model.CourierCredentials;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private int courierId;
    private String login;
    private static final String PASSWORD = "password123";
    private static final Random RANDOM = new Random();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        login = generateUniqueLogin();
        createTestCourier();
    }

    @After
    public void tearDown() {
        cleanupCreatedCourier();
    }

    private String generateUniqueLogin() {
        return "TestLogin_" + RANDOM.nextInt(100000);
    }

    private void createTestCourier() {
        Courier courier = new Courier(login, PASSWORD, "TestName");
        ValidatableResponse response = courierClient.create(courier);

        response.statusCode(201).body("ok", equalTo(true));
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
    public void courierCanLogin() {
        // Arrange
        CourierCredentials credentials = new CourierCredentials(login, PASSWORD);

        // Act
        ValidatableResponse response = courierClient.login(credentials);

        // Assert
        response
                .statusCode(200)
                .body("id", notNullValue());

        // Сохраняем ID для очистки
        courierId = response.extract().path("id");
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {
        // Arrange — неверные учётные данные
        CourierCredentials invalidCredentials = new CourierCredentials(login, "wrong_password");

        // Act — попытка логина с неверным паролем
        ValidatableResponse response = courierClient.login(invalidCredentials);

        // Assert — ожидаем ошибку
        response
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginNonExistentCourierReturnsError() {
        // Arrange — несуществующий логин
        String nonExistentLogin = "ghost_user_scooter_" + RANDOM.nextInt(10000);
        CourierCredentials invalidCredentials = new CourierCredentials(nonExistentLogin, PASSWORD);

        // Act — попытка логина с несуществующим логином
        ValidatableResponse response = courierClient.login(invalidCredentials);

        // Assert — ожидаем ошибку
        response
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginWithoutLoginFieldReturnsError() {
        // Arrange — пустые данные для логина
        CourierCredentials invalidCredentials = new CourierCredentials("", PASSWORD);

        // Act — попытка входа без логина
        ValidatableResponse response = courierClient.login(invalidCredentials);

        // Assert — ожидаем ошибку валидации
        response
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
