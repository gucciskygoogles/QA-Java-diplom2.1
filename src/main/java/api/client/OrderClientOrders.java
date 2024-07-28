package api.client;

import io.qameta.allure.Step;
import parktikum.Order;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClientOrders {

    private static final String AUTH_HEADER = "Authorization";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/orders");
    }

    @Step("Создание заказа c регистрацией")
    public Response createOrderWithAuthorization(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header(AUTH_HEADER, token)
                .body(order)
                .when()
                .post("/orders");
    }

    @Step("Создание заказа без регистрации")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/orders");
    }

    @Step("Создание заказа юез ингредиентов")
    public Response createOrderWithoutIngredients() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[]}")
                .when()
                .post("/orders");
    }

    @Step("Создание заказа с некорректным ингредиентом")
    public Response createOrderWithInvalidIngredient() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[\"invalid_ingredient_id\"]}")
                .when()
                .post("/orders");
    }

    @Step("Получени заказов юзера")
    public Response getUserOrders(String token) {
        return given()
                .header(AUTH_HEADER, token)
                .when()
                .get("/orders");
    }

    @Step("Получениe заказов без регистрации")
    public Response getUserOrdersWithoutAuthorization() {
        return given()
                .when()
                .get("/orders");
    }
}
