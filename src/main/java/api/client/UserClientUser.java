package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClientUser {

    private static final String AUTH_HEADER = "Authorization";

    @Step("Обновление данных пользователя")
    public Response updateUserInfo(String token, String email, String name) {
        Map<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);

        return given()
                .header(AUTH_HEADER, token)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch("/auth/user")
                .then()
                .extract()
                .response();
    }

    @Step("Попытка обновления данных пользователя без авторизации")
    public Response updateUserInfoWithoutAuthorization(String email, String name) {
        Map<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);

        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch("/auth/user")
                .then()
                .extract()
                .response();
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return RestAssured
                .given()
                .header("Content-Type", "json")
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user");
    }
}
