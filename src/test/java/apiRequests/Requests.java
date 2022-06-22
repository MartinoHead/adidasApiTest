package apiRequests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Requests {

    private static String baseURI = "https://petstore.swagger.io/v2/pet";
    public static final String POST_PET_BODY_FILE = "src/test/resources/jsonBody.json";
    public static String storedPetId;

    public void getPetsByStatus(String status) {
        System.out.println("GET request: get pets with status " + status);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(baseURI + "/findByStatus?status=" + status)
                .then().statusCode(200)
                .log().all();
    }

    public void deletePetById() {
        System.out.println("Delete pet with ID: " + storedPetId);

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .delete(baseURI + "/" + storedPetId)
                .then().statusCode(200)
                .extract().response();

        Assert.assertTrue(response.getBody().asString().contains(storedPetId), "Pet with ID" + storedPetId + "is deleted");
    }

    public void postNewPet(String status) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(getRequestBody());
        json.put("status", status);
        System.out.println("Adding new " + status + " pet in the store list");

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(json.toJSONString())
                .post(baseURI)
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        String id = response.jsonPath().getString("id");
        System.out.println("New pet is added. ID = " + id);
        storedPetId = id;
    }

    public void updatePetStatus(String status) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(getRequestBody());

        json.put("id", storedPetId);
        json.put("status", status);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(json.toJSONString())
                .when()
                .put(baseURI)
                .then().statusCode(200)
                .log().all();
        System.out.println("Pet with ID=" + storedPetId + " and status=" + status + " is updated.");
    }

    public String getRequestBody() {
        String jsonBody = null;
        try {
            jsonBody = generateStringFromResource(POST_PET_BODY_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    public String generateStringFromResource(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
