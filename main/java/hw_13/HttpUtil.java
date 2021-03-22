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

    public static User createNewObject(String url, User user) throws IOException, InterruptedException {
        String requestBody = new Gson().toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User updateObject(String url, User userUpdate) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(GSON.toJson(userUpdate)))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUser(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static int deleteUserStatus(User user, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(GSON.toJson(user)))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> getAllUser(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
    }

    public static User getUserById(int id, String url) throws IOException, InterruptedException {
        String urlWishId = String.format("%s/%d", url, id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWishId))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static String getUserByUsername(String username, String url) throws IOException, InterruptedException {
        String urlWishUsername = String.format("%s%s", url, username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWishUsername))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static PostUser getLastPost(String url1, String url2, int userID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%d%s", url1, userID, url2)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<PostUser> allPost = GSON.fromJson(response.body(), new TypeToken<List<PostUser>>() {
        }.getType());
        return allPost.stream()
                .max(Comparator.comparing(PostUser::getId))
                .get();
    }

    public static Path userComments(String url1, String url2, PostUser post) throws IOException, InterruptedException {
        String fileName = "user-" + post.getUserId() + "-post-" + post.getId() + "-comments.json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%d%s", url1, post.getId(), url2)))
                .GET()
                .build();
        HttpResponse<Path> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofFile(
                Paths.get("src/main/resources/" + fileName)));
        return response.body();
    }

    public static List<TaskUser> allOpenTasksUser(String url1, String url2, int idUser) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%d%s", url1, idUser, url2)))
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