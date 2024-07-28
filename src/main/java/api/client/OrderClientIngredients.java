package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClientIngredients {

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .when()
                .get("ingredients");
    }
}
