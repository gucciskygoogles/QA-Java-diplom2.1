package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import parktikum.User;

import static io.restassured.RestAssured.given;

public class UserClientLogin {

    @Step("Логин пользователя по ручке")
    public Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/auth/login");
    }


}
