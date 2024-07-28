package api.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import parktikum.User;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClientRegister {

    private static final String AUTH_HEADER = "Authorization";

    @Step("Регистрация пользователя по ручке")
    public Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/auth/register");
    }


}
