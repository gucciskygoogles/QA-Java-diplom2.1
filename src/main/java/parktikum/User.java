package parktikum;

import io.qameta.allure.Step;

import java.util.Random;
import java.util.UUID;

public class User {

    private String email;
    private String name;
    private String password;

    private static final String[] NAMES = {
            "Vadim", "Alexey", "Olga", "Maria", "Sergey", "Ivan", "Anna", "Elena"
    };

    public User(String email, String password, String name) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Step("Генерация рандомного пароля")
    public static String generateRandomPassword() {
        return "password_" + UUID.randomUUID().toString();
    }

    @Step("Генерация рандомного имени")
    public static String generateRandomName() {
        Random random = new Random();
        return NAMES[random.nextInt(NAMES.length)];
    }

    @Step("Генерация рандомного email")
    public static String generateRandomEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }

}
