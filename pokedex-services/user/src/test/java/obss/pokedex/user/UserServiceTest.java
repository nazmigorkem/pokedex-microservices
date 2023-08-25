package obss.pokedex.user;

import obss.pokedex.user.model.UserAddRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class UserServiceTest {
    public static final String TEST_USER_USERNAME = "springboottest";
    public static final String TEST_USER_PASSWORD = "test";
    private static final String USER_SERVICE_URL = "http://localhost:8080/api/user-service";
    private String JSESSIONID;

    @Test
    void userCanSignUp() {
        var restTemplate = new RestTemplate();
        var response = restTemplate.postForEntity(USER_SERVICE_URL + "/user/add", UserAddRequest.builder().username(TEST_USER_USERNAME).password(TEST_USER_PASSWORD).build(), Void.class);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void userCanLogin() {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("username=" + TEST_USER_USERNAME + "&password=" + TEST_USER_PASSWORD, headers);
        var response = restTemplate.exchange(USER_SERVICE_URL + "/login", HttpMethod.POST, httpEntity, Void.class);
        assert response.getStatusCode().is2xxSuccessful();
        this.JSESSIONID = response.getHeaders().get("Set-Cookie").get(0).split(";")[0].split("=")[1];
    }

    @Test
    void userAccountCanBeDeleted() {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + this.JSESSIONID);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(USER_SERVICE_URL + "/delete/" + TEST_USER_USERNAME, HttpMethod.DELETE, httpEntity, Void.class);
        assert response.getStatusCode().is2xxSuccessful();
    }
}
