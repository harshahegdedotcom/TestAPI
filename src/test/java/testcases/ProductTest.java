package testcases;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.Payload;
import pojo.Product;
import routes.Routes;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTest extends BaseClass {
    @Test
    public void testGetAllProducts() {
        given()
                .when()
                .get(Routes.GET_ALL_PRODUCTS)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetAllProductById() {
        int productId = configReader.getIntProperty("productId");
        given()
                .pathParam("id", productId)
                .when()
                .get(Routes.GET_PRODUCT_BY_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetLimitedProducts() {
        given()
                .pathParam("limit", configReader.getIntProperty("productLimit"))
                .when()
                .get(Routes.GET_PRODUCTS_WITH_LIMIT)
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetSortedProductsDESC() {
        Response response = given()
                .pathParam("order", "desc")
                .when()
                .get(Routes.GET_PRODUCTS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();

        // Extract all the product ids to check whether its in ASC or DESC
        List<Integer> productIds = response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedDescending(productIds), is(true));
    }

    @Test
    public void testGetSortedProductsASC() {
        Response response = given()
                .pathParam("order", "asc")
                .when()
                .get(Routes.GET_PRODUCTS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();

        // Extract all the product ids to check whether its in ASC or DESC
        List<Integer> productIds = response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedAscending(productIds), is(true));
    }

    @Test
    public void testGetAllCategories() {
        Response response = given()
                .when()
                .get(Routes.GET_ALL_CATEGORIES)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
    }
    @Test
    public void testPorductsByCategory() {
        Response response = given()
                .pathParam("category", "electronics")
                .when()
                .get(Routes.GET_PRODUCTS_BY_CATEGORY)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("category", everyItem(notNullValue()))
                .body("category", everyItem(equalTo("electronics")))
                .extract().response();
    }
    @Test
    public void testAddNewProduct() {
        Product updatedProductLoad = Payload.productPayload();

        Product p = new Product(updatedProductLoad.title(),
                updatedProductLoad.price(),
                updatedProductLoad.description(),
                updatedProductLoad.image(),
                updatedProductLoad.category());

        int productId = given()
                .contentType(ContentType.JSON)
                .body(p)
                .when()
                .post(Routes.CREATE_PRODUCT)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(p.title()))
                .extract().jsonPath().getInt("id");
    }

    @Test(dataProvider = "jsonDataProvider", dataProviderClass = utils.DataProviders.class)
    public void testDataProviderAddNewProduct(Map<String, String> data) {
        String title = data.get("title");
        double price = Double.parseDouble(data.get("price"));
        String category = data.get("category");
        String description = data.get("description");
        String image = data.get("image");

        Product newProduct = new Product(title, price, description, image, category);

        int productId = given()
                .contentType(ContentType.JSON)
                .body(newProduct)
                .when()
                .post(Routes.CREATE_PRODUCT)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(newProduct.title()))
                .extract().jsonPath().getInt("id");
        given()
                .pathParam("id", productId)
                .when()
                .delete(Routes.DELETE_PRODUCT)
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateProduct() {
        Product updatedProductLoad = Payload.productPayload();

        Product p = new Product(updatedProductLoad.title(),
                updatedProductLoad.price(),
                updatedProductLoad.description(),
                updatedProductLoad.image(),
                updatedProductLoad.category());

        given()
                .pathParam("id", configReader.getIntProperty("productId"))
                .contentType(ContentType.JSON)
                .body(updatedProductLoad)
                .when()
                .put(Routes.UPDATE_PRODUCT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo(p.title()))
                .extract().jsonPath().getInt("id");
    }

    @Test
    public void testDeleteProduct() {
        Product updatedProductLoad = Payload.productPayload();
        given()
                .pathParam("id", configReader.getIntProperty("productId"))
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.DELETE_PRODUCT)
                .then()
                .statusCode(200);
    }
    @Test
    public void testProductSchema()
    {
        int productId=configReader.getIntProperty("productId");

        given()
                .pathParam("id", productId)

                .when()
                .get(Routes.GET_PRODUCT_BY_ID)
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("productSchema.json"));
    }
}
