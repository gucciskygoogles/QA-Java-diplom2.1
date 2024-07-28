
import api.client.*;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.After;
import parktikum.DataCreator;
import parktikum.Finals;
import parktikum.Order;
import parktikum.User;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static parktikum.DataCreator.getValidIngredientIds;

public class OrderCreateAndGetTest {

    private OrderClientIngredients orderClientIngredients;
    private OrderClientOrders orderClientOrders;
    private UserClientLogin userClientLogin;
    private UserClientRegister userClientRegister;
    private UserClientUser userClientUser;
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = Finals.BASE_URI;

        orderClientOrders = new OrderClientOrders();
        orderClientIngredients = new OrderClientIngredients();
        userClientLogin = new UserClientLogin();
        userClientRegister = new UserClientRegister();
        userClientUser = new UserClientUser();
    }

    @After
    public void tearDown() {
        if (token != null) {
            Response response = userClientUser.deleteUser(token);
            System.out.println("Attempting to delete user with token: " + token);
            response.then()
                    .statusCode(202)
                    .and()
                    .body("success", equalTo(true));
        } else {
            System.out.println("Token is null, skipping user deletion.");
        }
    }

    @Test
    public void createOrderWithAuthorizationTest() {
        User user = DataCreator.generateRandomUser();
        userClientRegister.createUser(user);

        Response loginResponse = userClientLogin.loginUser(user);
        token = loginResponse.path("accessToken");
        Order order = new Order(getValidIngredientIds(orderClientIngredients));
        Response response = orderClientOrders.createOrderWithAuthorization(order, token);

        response.then().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Description("Тест Создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        Order order = new Order(getValidIngredientIds(orderClientIngredients));
        Response response = orderClientOrders.createOrderWithoutAuthorization(order);

        response.then().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Description("Тест Создание заказа с ингредиентами")
    public void createOrderWithIngredientsTest() {
        Order order = new Order(getValidIngredientIds(orderClientIngredients));
        Response response = orderClientOrders.createOrder(order);

        response.then().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Description("Тест Создание заказа с игредиентами")
    public void createOrderWithoutIngredientsTest() {
        Response response = orderClientOrders.createOrderWithoutIngredients();

        response.then().statusCode(400)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Тест Создание заказа с некорректными ингредиентами")
    public void createOrderWithInvalidIngredientTest() {
        Response response = orderClientOrders.createOrderWithInvalidIngredient();

        response.then().statusCode(500);
    }

    @Test
    @Description("Тест Получение заказов юзера")
    public void getUserOrdersWithAuthorizationTest() {
        User user = DataCreator.generateRandomUser();
        userClientRegister.createUser(user);

        Response loginResponse = userClientLogin.loginUser(user);
        token = loginResponse.path("accessToken");
        Response response = orderClientOrders.getUserOrders(token);

        response.then().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @Description("Тест Получение заказов юзера без регистрации")
    public void getUserOrdersWithoutAuthorizationTest() {
        Response response = orderClientOrders.getUserOrdersWithoutAuthorization();

        response.then().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }


}