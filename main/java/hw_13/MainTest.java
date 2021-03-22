package hw_13;

import hw_13.taskOne.Address;
import hw_13.taskOne.Company;
import hw_13.taskOne.Geo;
import hw_13.taskOne.User;
import hw_13.taskThree.TaskUser;
import hw_13.taskTwo.PostUser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MainTest {
    private static final String URL_TASK_1 = "https://jsonplaceholder.typicode.com/users";
    private static final User USER = createDefaultUser();
    private static final String URL_USER = String.format("%s/%d", URL_TASK_1, 5);
    private static final String URL_USER_BY_USERNAME = "https://jsonplaceholder.typicode.com/users?username=";
    private static final String URL_POST_PART_1 = "https://jsonplaceholder.typicode.com/users/";
    private static final String URL_POST_PART_2 = "/posts";
    private static final String URL_COMMENTS_PART_1 = "https://jsonplaceholder.typicode.com/posts/";
    private static final String URL_COMMENTS_PART_2 = "/comments";
    private static final String URL_PART_1 = "https://jsonplaceholder.typicode.com/users/";
    private static final String URL_PART_2 = "/todos";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Завдання № 1.1
        System.out.println("Завдання № 1.1");
        User createUser = HttpUtil.createNewObject(URL_TASK_1, USER);
        System.out.println("Створюєм новий обєкт: \n" + createUser);
        System.out.println();

        // Завдання № 1.2
        System.out.println("Завдання № 1.2");
        User userUpdate = HttpUtil.getUser(URL_USER);
        System.out.println("Обєкт до зміни: \n" + userUpdate);
        userUpdate.setName("Taras Tarasov");
        userUpdate.setUsername("Tera");
        System.out.println("Обєкт після змін: \n" + HttpUtil.updateObject(URL_USER, userUpdate));
        System.out.println();

        // Завдання № 1.3
        System.out.println("Завдання № 1.3");
        int status = HttpUtil.deleteUserStatus(userUpdate, URL_USER);
        System.out.println("Статус видалення: " + status);
        System.out.println();

        // Завдання № 1.4
        System.out.println("Завдання № 1.4\nВивід інформації про усіх користувачів:");
        List<User> allUser = HttpUtil.getAllUser(URL_TASK_1);
        for (User user : allUser) {
            System.out.println(user);
        }
        System.out.println();

        // Завдання № 1.5
        System.out.println("Завдання № 1.5\nОтримуєм інформацію про користувача під ID=2");
        System.out.println(HttpUtil.getUserById(2, URL_TASK_1));
        System.out.println();

        // Завдання № 1.6
        System.out.println("Завдання № 1.6\nОтримуєм інформацію про користувача під ніком = Bret");
        System.out.println(HttpUtil.getUserByUsername("Bret", URL_USER_BY_USERNAME));
        System.out.println();

        // Завдання № 2
        System.out.println("Завдання № 2");
        PostUser lastPost = HttpUtil.getLastPost(URL_POST_PART_1, URL_POST_PART_2, 1);
        Path path = HttpUtil.userComments(URL_COMMENTS_PART_1, URL_COMMENTS_PART_2, lastPost);
        System.out.println("Коментарі користувача виведено і записано у файл за адресою: " + path);
        System.out.println();

        // Завдання № 3
        System.out.println("Завдання № 3\nВиводим усі відкриті задачі користувача з ID - 5");
        List<TaskUser> openTask = HttpUtil.allOpenTasksUser(URL_PART_1, URL_PART_2, 5);
        System.out.println(openTask);
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
