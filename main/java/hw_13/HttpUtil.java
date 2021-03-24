package hw_13;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hw_13.taskOne.User;
import hw_13.taskThree.TaskUser;
import hw_13.taskTwo.PostUser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final String URL_MAIN = "https://jsonplaceholder.typicode.com/";
    private static final String URL_USERS = "users";
    private static final String URL_USERNAME = "users?username=";
    private static final String URL_POSTS = "posts";
    private static final String URL_COMMENTS = "/comments";
    private static final String URL_TODOS = "/todos";
    private static String urlUserById;

    public static User createNewObject(User user) throws IOException, InterruptedException {
        String requestBody = new Gson().toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", URL_MAIN, URL_USERS)))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User updateObject(User userUpdate) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlUserById))
                .PUT(HttpRequest.BodyPublishers.ofString(GSON.toJson(userUpdate)))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserById(int idUser) throws IOException, InterruptedException {
        urlUserById = String.format("%s%s/%d", URL_MAIN, URL_USERS, idUser);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlUserById))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static int deleteUserStatus(User user) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlUserById))
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(GSON.toJson(user)))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> getAllUser() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", URL_MAIN, URL_USERS)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
    }

    public static String getUserByUsername(String username) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s%s", URL_MAIN, URL_USERNAME, username)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static PostUser getLastPost(int userID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d/%s", URL_MAIN, URL_USERS, userID, URL_POSTS)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<PostUser> allPost = GSON.fromJson(response.body(), new TypeToken<List<PostUser>>() {
        }.getType());
        return allPost.stream()
                .max(Comparator.comparing(PostUser::getId))
                .get();
    }

    public static Path userComments(PostUser post) throws IOException, InterruptedException {
        String fileName = "user-" + post.getUserId() + "-post-" + post.getId() + "-comments.json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", URL_MAIN, URL_POSTS, post.getId(), URL_COMMENTS)))
                .GET()
                .build();
        HttpResponse<Path> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofFile(
                Paths.get("src/main/resources/" + fileName)));
        return response.body();
    }

    public static List<TaskUser> allOpenTasksUser(int idUser) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", URL_MAIN, URL_USERS, idUser, URL_TODOS)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<TaskUser> allTask = GSON.fromJson(response.body(), new TypeToken<List<TaskUser>>() {
        }.getType());
        return allTask.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
}