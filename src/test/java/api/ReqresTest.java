package api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.Clock;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresTest {

    private final static String URL = "https://reqres.in";

    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOk200());
        List<UserData> users =                  //в список положили UserData
                given()                         //начинаются запросы
                        .when()
                        .contentType(ContentType.JSON)
                        .get("/api/users?page=2")
                        .then().log().all()
                        .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map((UserData::getAvatar)).toList();
        List<String> ids = users.stream().map(x -> x.getId().toString()).toList();
        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void successRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOk200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Register user = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("/api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());

        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    @Test
    public void unSuccessRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());
        String error = "Missing password";

        Register user = new Register("sydney@fife", "");

        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("/api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);

        Assertions.assertEquals(error, unSuccessReg.getError());
    }

    @Test
    public void sortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOk200());

        List<ColorsData> colors = given()
                .when()
                .get("/api/unknown")
                .then().log().all()
                .extract().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).toList();
        List<Integer> sortedYears = years.stream().sorted().toList();
        Assertions.assertEquals(sortedYears, years);

    }

    @Test
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(204));
        given()
                .when()
                .delete("/api/users/2")
                .then().log().all();
    }

    @Test
    public void timeTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOk200());
        Usertime user = new Usertime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("/api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String regexEx = "(.{11})$";
        String regexAct = "(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regexEx, "");

        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regexAct, ""));

    }
}
