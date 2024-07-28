
import api.client.UserClientLogin;
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

public class UserLoginTest {

    private UserClientLogin userClientLogin;
    private UserClientRegister userClientRegister;
    private UserClientUser userClientUser;
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
    @DisplayName("Логин юзера")
    @Description("Логирование нового пользователя")
    public void loginUserTest() {
        User user = DataCreator.generateRandomUser();
        Response response = userClientRegister.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .body("success", equalTo(true));

        userClientLogin.loginUser(user)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Логин юзера")
    @Description("Логирование пользователя с неверным паролем")
    public void loginUserWithWrongPasswordTest() {
        User user = DataCreator.generateRandomUser();
        Response response = userClientRegister.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .body("success", equalTo(true));

        user.setPassword(User.generateRandomPassword());

        userClientLogin.loginUser(user)
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин юзера")
    @Description("Логирование пользователя с неверным паролем")
    public void loginUserWithWrongNameTest() {
        User user = DataCreator.generateRandomUser();
        Response response = userClientRegister.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .body("success", equalTo(true));

        user.setEmail(User.generateRandomPassword());

        userClientLogin.loginUser(user)
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }


}
