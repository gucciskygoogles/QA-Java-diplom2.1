package parktikum;

import api.client.OrderClientIngredients;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static parktikum.User.*;

public class DataCreator {

    @Step("Создание рандомного пользователя")
    public static User generateRandomUser() {
        return new User(generateRandomEmail(), generateRandomPassword(), generateRandomName());
    }

    public static List<String> getValidIngredientIds(OrderClientIngredients orderClient) {
        Response response = orderClient.getIngredients();
        response.then().statusCode(200).and().body("success", equalTo(true));

        List<Map<String, String>> ingredients = response.jsonPath().getList("data");
        return ingredients.stream().map(ingredient -> ingredient.get("_id")).collect(Collectors.toList());
    }
}
