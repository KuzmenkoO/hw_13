package hw_13;

import hw_13.taskOne.*;

import java.io.IOException;
import java.util.List;

public class MainTest {
    private static final String URL_TASK_1 = "https://jsonplaceholder.typicode.com/users";
    private static final User USER = createDefaultUser();
    private static final String URL_USER = String.format("%s/%d", URL_TASK_1, 5);
    private static final String URL_USER_BY_USERNAME = "https://jsonplaceholder.typicode.com/users?username=";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Завдання № 1.1
        System.out.println("Завдання №1.1");
        User createUser = HttpUtil.createNewObject(URL_TASK_1, USER);
        System.out.println("Створюєм новий обєкт: \n" + createUser);
        System.out.println();

        // Завдання № 1.2
        System.out.println("Завдання №1.2");
        User userUpdate = HttpUtil.getUser(URL_USER);
        System.out.println("Обєкт до зміни: \n" + userUpdate);
        userUpdate.setName("Taras Tarasov");
        userUpdate.setUsername("Tera");
        System.out.println("Обєкт після змін: \n" + HttpUtil.updateObject(URL_USER, userUpdate));
        System.out.println();

        // Завдання № 1.3
        System.out.println("Завдання №1.3");
        int status = HttpUtil.deleteUserStatus(userUpdate, URL_USER);
        System.out.println("Статус видалення: " + status);
        System.out.println();

        // Завдання № 1.4
        System.out.println("Завдання №1.4\nВивід інформації про усіх користувачів:");
        List<User> allUser = HttpUtil.getAllUser(URL_TASK_1);
        for (User user : allUser) {
            System.out.println(user);
        }
        System.out.println();

        // Завдання № 1.5
        System.out.println("Завдання №1.5\nОтримуєм інформацію про користувача під ID=2");
        System.out.println(HttpUtil.getUserById(2, URL_TASK_1));
        System.out.println();

        // Завдання №1.6
        System.out.println("Завдання №1.6\nОтримуєм інформацію про користувача під ніком = Bret");
        System.out.println(HttpUtil.getUserByUsername("Bret", URL_TASK_1));
    }


    private static User createDefaultUser() {
        User user = new User();
        user.setName("Oleksandr");
        user.setUsername("Alex");
        user.setEmail("alex@gmail.com");
        user.setAddress(new Address("Konovalca", "10", "Ternopil", "46000",
                new Geo("43.9509", "-47.0653")));
        user.setCompany(new Company(
                "Alfa-Security",
                "protection against intruders",
                "safety above all"));
        return user;
    }
}
