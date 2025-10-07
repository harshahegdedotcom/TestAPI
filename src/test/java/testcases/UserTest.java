package testcases;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.Payload;
import pojo.User;
import routes.Routes;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class UserTest  extends  BaseClass{
    @Test
    public void  testGetAllUsers()
    {
        given()
                .when()
                .get(Routes.GET_ALL_USERS)
                .then()
                .statusCode(200);
    }
    @Test
    public void  testGetSpecificUSerById()
    {
        given()
                .pathParam("id", configReader.getIntProperty("userId"))
                .when()
                .get(Routes.GET_USER_BY_ID)
                .then()
                .statusCode(200);
    }
    @Test
    public void  testGetUserByLimit()
    {
        given()
                .pathParam("limit", configReader.getIntProperty("userLimit"))
                .when()
                .get(Routes.GET_PRODUCTS_WITH_LIMIT)
                .then()
                .statusCode(200);
    }
    @Test
    public void  testGetUserBySortOrderDESC()
    {
        Response response = given()
                .pathParam("order", "desc")
                .when()
                .get(Routes.GET_PRODUCTS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();
        // Extract all the user ids to check whether its in ASC or DESC
        List<Integer> userIds = response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedDescending(userIds), is(true));
    }
    @Test
    public void  testGetUserBySortOrderASC()
    {
        Response response = given()
                .pathParam("order", "asc")
                .when()
                .get(Routes.GET_PRODUCTS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();
        // Extract all the user ids to check whether its in ASC or DESC
        List<Integer> userIds = response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedAscending(userIds), is(true));
    }

    // @Test
    // public void testCreateUser()
    // {
    //     User newUser = Payload.userPayload();
    //     int userId = given()
    //             .contentType(ContentType.JSON)
    //             .body(newUser)
    //             .when()
    //             .post(Routes.CREATE_USER)
    //             .then()
    //             .statusCode(201)
    //             .body("id", notNullValue())
    //             .extract().jsonPath().getInt("id");
    // }
    @Test
    public void testUpdateUser()
    {
        User updateUser = Payload.userPayload();
         given()
                .pathParam("id", configReader.getIntProperty("userId"))
                .contentType(ContentType.JSON)
                .body(updateUser)
                .when()
                .put(Routes.UPDATE_USER)
                .then()
                .statusCode(200)
                .body("username", equalTo(updateUser.username()));

    }
    @Test
    public void testDeleteUser()
    {

        given()
                .pathParam("id", configReader.getIntProperty("userId"))
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.DELETE_USER)
                .then()
                .statusCode(200);


    }
    @Test
    public void testUserSchema()
    {
        int userId=configReader.getIntProperty("userId");
        given()
                .pathParam("id",userId)
                .when()
                .get(Routes.GET_USER_BY_ID)
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userSchema.json"));

    }
}
