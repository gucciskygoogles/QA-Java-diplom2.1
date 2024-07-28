import api.client.UserClientRegister;
import api.client.UserClientUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parktikum.DataCreator;
import parktikum.Finals;
import parktikum.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {

    private UserClientRegister userClientRegister;
    private UserClientUser userClientUser;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Finals.BASE_URI;
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
    @DisplayName("Создание пользователя ")
    @Description("Создание пользователя со случайными значениями")
    public void createUserTest() {
        User user = DataCreator.generateRandomUser();
        Response response = userClientRegister.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Создание двух одинаковых пользователей")
    public void createUserTwiceTest() {
        User user = DataCreator.generateRandomUser();
        Response response = userClientRegister.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .body("success", equalTo(true));

        userClientRegister.createUser(user)
                .then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Создание пользователя без имени")
    public void createUserWithNoNameTest() {
        User user = DataCreator.generateRandomUser();
        user.setName(null);

        userClientRegister.createUser(user)
                .then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


}
