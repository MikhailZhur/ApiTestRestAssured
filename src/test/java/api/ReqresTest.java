package api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresTest {

    private final static String URL = "https://reqres.in";

    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecOk200());
        List<UserData> users =                  //в список положили UserData
                given()                         //начинаются запросы
                        .when()
                        .contentType(ContentType.JSON)// указываем формат чтения запроса
                        .get("/api/users?page=2")
                        .then().log().all()
                        .extract().body().jsonPath().getList("total_pages", UserData.class);

//stream позволяет перебрать список и выбрать метод поочередности
        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assertions.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map((UserData::getAvatar)).toList();
        List<String> ids = users.stream().map(x->x.getId().toString()).toList();
        for(int i = 0; i<avatars.size();i++){
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }
}
