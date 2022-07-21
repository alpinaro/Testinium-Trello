package tests;

import utilities.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author alpinaro (Alper Çınaroğlu)
 * https://github.com/alpinaro
 */
public class Trello {

    private RequestSpecification spec = new RequestSpecBuilder().setBaseUri("https://api.trello.com").build();
    private Response response;
    private JsonPath jsonPath;
    private HashMap<String,String> request;

    private static String idBoard;
    
    private static ArrayList<String> idLists = new ArrayList<>();
    private static String idListSelected;

    private static ArrayList<String> idCards = new ArrayList<>();
    private static String idCardSelected;

    @Test
    @Order(1)
    public void createBoard() {

        spec.pathParams("pp1",1,"pp2","boards");

        request = new HashMap<>();
        request.put("name","Testinium");
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                post("/{pp1}/{pp2}");

        jsonPath = response.jsonPath();
        idBoard = jsonPath.get("id");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void getLists() {

        spec.pathParams("pp1",1,"pp2","boards", "pp3", idBoard, "pp4", "lists");

        request = new HashMap<>();
        request.put("id", idBoard);
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                get("/{pp1}/{pp2}/{pp3}/{pp4}");

        jsonPath = response.jsonPath();
        idLists = jsonPath.get("id");
        idListSelected = idLists.get(0);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void createCard1() {

        spec.pathParams("pp1",1,"pp2","cards");

        request = new HashMap<>();
        request.put("name", "Beymen");
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));
        request.put("idList", idListSelected);

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                post("/{pp1}/{pp2}");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void createCard2() {

        spec.pathParams("pp1", 1, "pp2", "cards");

        request = new HashMap<>();
        request.put("name", "Trello");
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));
        request.put("idList", idListSelected);

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                post("/{pp1}/{pp2}");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void getCards() {

        spec.pathParams("pp1",1,"pp2","lists", "pp3", idListSelected, "pp4", "cards");

        request = new HashMap<>();
        request.put("id", idListSelected);
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                get("/{pp1}/{pp2}/{pp3}/{pp4}");

        jsonPath = response.jsonPath();
        idCards = jsonPath.get("id");
        Random random = new Random();
        idCardSelected = idCards.get(random.nextInt(2));

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(6)
    public void updateCard() {

        spec.pathParams("pp1",1,"pp2","cards", "pp3", idCardSelected);

        request = new HashMap<>();
        request.put("desc", "DONE");
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                put("/{pp1}/{pp2}/{pp3}");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void deleteCard1() {

        spec.pathParams("pp1", 1, "pp2", "cards", "pp3", idCards.get(1));

        request = new HashMap<>();
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                delete("/{pp1}/{pp2}/{pp3}");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void deleteCard2() {

        spec.pathParams("pp1", 1, "pp2", "cards", "pp3", idCards.get(0));

        request = new HashMap<>();
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                delete("/{pp1}/{pp2}/{pp3}");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void deleteBoard() {

        spec.pathParams("pp1", 1, "pp2", "boards", "pp3", idBoard);

        request = new HashMap<>();
        request.put("key", ConfigReader.getProperty("key"));
        request.put("token", ConfigReader.getProperty("token"));

        response = given().
                spec(spec).
                contentType("application/json").
                body(request).
                when().
                delete("/{pp1}/{pp2}/{pp3}");

        assertEquals(200,response.getStatusCode());
    }
}