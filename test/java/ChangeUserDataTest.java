import api.client.UserClientLogin;
import api.client.UserClientRegister;
import api.client.UserClientUser;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parktikum.DataCreator;
import parktikum.Finals;
import parktikum.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserDataTest {

    private UserClientUser userClientUser;
    private UserClientRegister userClientRegister;
    private UserClientLogin userClientLogin;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Finals.BASE_URI;
        userClientLogin = new UserClientLogin();
        userClientRegister = new UserClientRegister();
        userClientUser = new UserClientUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Response response = userClientUser.deleteUser(accessToken);
            System.out.println("Attempting to delete user with token: " + accessToken);
            response.then()
                    .statusCode(202)
                    .and()
                    .body("success", equalTo(true));
        } else {
            System.out.println("Token is null, skipping user deletion.");
        }
    }

    @Test
    @Description("Обновление данных юзера")
    public void changeUserData() {
        User user = DataCreator.generateRandomUser();
        userClientRegister.createUser(user)
                .then()
                .body("success", equalTo(true));

        Response loginResponse = userClientLogin.loginUser(user)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()))
                .extract()
                .response();

        accessToken = loginResponse.jsonPath().getString("accessToken");

        String newEmail = User.generateRandomEmail();
        String newName = User.generateRandomName();

        userClientUser.updateUserInfo(accessToken, newEmail, newName)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(newEmail))
                .and()
                .body("user.name", equalTo(newName));
    }

    @Test
    @Description("Проверка смены данных без авторизации")
    public void changeUserDataWithoutAuthorizationTest() {
        User user = DataCreator.generateRandomUser();
        userClientRegister.createUser(user)
                .then()
                .body("success", equalTo(true));

        Response loginResponse = userClientLogin.loginUser(user)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()))
                .extract().response();

        accessToken = loginResponse.jsonPath().getString("accessToken");

        String newEmail = User.generateRandomEmail();
        String newName = User.generateRandomName();

        userClientUser.updateUserInfoWithoutAuthorization(newEmail, newName)
                .then()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
