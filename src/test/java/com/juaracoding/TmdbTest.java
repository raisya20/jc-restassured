package com.juaracoding;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class TmdbTest {

    String myToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMDdiNjAyYjBmNGY2M2IxOWQyZTU5OWUxYjQ3ZWI1NSIsIm5iZiI6MTcyOTg1ODA0NC43NTAzNTUsInN1YiI6IjY3MTkwODZhZTgzM2Q5MmVmMDVmYjIwNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ty7Xh7VSUIU1QpXekDVocI3GCR85eNttgPg3IOmap4M";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    // Scenario Test 1: Get Movie Now Playing
    @Test
    public void testGetMovieNowPlaying() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .get("/movie/now_playing");

        response.then()
                .statusCode(200)
                .body("results[0].title", equalTo("Venom: The Last Dance")); // Title Validation
        System.out.println("Now Playing Movie Title: " + response.jsonPath().getString("results[0].title"));
    }

    // Negative test: Invalid Now Playing Page
    @Test
    public void testGetMovieNowPlayingInvalidPage() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 999999) // Invalid Page
                .get("/movie/popular");

        response.then()
                .statusCode(400); // Bad Request
        System.out.println("Invalid Page Now Playing, Status Code: " + response.getStatusCode());
    }


    // Scenario Test 2: Get Movie Popular
    @Test
    public void testGetMoviePopular() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .get("/movie/popular");

        response.then()
                .statusCode(200)
                .body("results[0].title", equalTo("The Wild Robot")); // Title Validation
        System.out.println("Popular Movie Title: " + response.jsonPath().getString("results[0].title"));
    }

    // Negative test: Negative Popular Page
    @Test
    public void testGetMoviePopularInvalidPage() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", -1) // Negative page for Negative Test Scenario
                .get("/movie/popular");

        response.then()
                .statusCode(400); // Bad Request
        System.out.println("Invalid Page Popular, Status Code: " + response.getStatusCode());
    }

    // Scenario Test 3: Post Rating
    @Test
    public void testPostMovieRating() {
        int movieId = 100; // Replace with a valid movie ID
        Response response = given()
                .header("Authorization", myToken)
                .contentType("application/json")
                .body("{\"value\": 8.5}")
                .post("/movie/" + movieId + "/rating");

        response.then()
                .statusCode(201)
                .body("status_message", equalTo("Success.")); // Validate Succes Message
        System.out.println("Post Rating Response: " + response.jsonPath().getString("status_message"));
    }

    // Negative test: Post Rating >10
    @Test
    public void testPostInvalidMovieRating() {
        int movieId = 123; // Valid movie ID
        Response response = given()
                .header("Authorization", myToken)
                .contentType("application/json")
                .body("{\"value\": 15}") // Rate >10
                .post("/movie/" + movieId + "/rating");

        response.then()
                .statusCode(400); // Bad Request
        System.out.println("Invalid Rating Response Status Code: " + response.getStatusCode());
    }

    // Scenario Test 4: Get Movie Details
    @Test
    public void testGetMovieDetails() {
        int movieId = 550; // Movie Id "Fight Club"
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .get("/movie/" + movieId);

        response.then()
                .statusCode(200) // Verify Status Code : 200
                .body("title", notNullValue()) // Verify Response Get Movie Details
                .body("title", equalTo("Fight Club")); // Verify Movie Had "title movie"
        System.out.println("Movie Details Title: " + response.jsonPath().getString("title"));
    }


    // Negative test: Get Movie Details Invalid Id
    @Test
    public void testGetMovieDetailsNotFound() {
        int invalidMovieId = 99999999; // Invalid Id
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .get("/movie/" + invalidMovieId);
        response.then()
                .statusCode(404); // code Not Found
        System.out.println("Invalid Id Status Code: " + response.getStatusCode());
    }

    // Scenario Test 5: Get Movie Top Rated
    @Test
    public void testGetMovieTopRated() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .get("/movie/top_rated");

        response.then()
                .statusCode(200)
                .body("results[0].title", equalTo("The Shawshank Redemption")); // Title Validation
        System.out.println("Top Rated Movie Title: " + response.jsonPath().getString("results[0].title"));
    }

    // Negative test: Negative Top Rated Page
    @Test
    public void testGetMovieTopRatedInvalidPage() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 99999) // Negative page for Negative Test Scenario
                .get("/movie/top_rated");

        response.then()
                .statusCode(400); // Bad Request
        System.out.println("Invalid Page Top Rated, Status Code: " + response.getStatusCode());
    }

    // Scenario Test 6: Get Movie Upcoming
    @Test
    public void testGetMovieUpcoming() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .get("/movie/upcoming");

        response.then()
                .statusCode(200)
                .body("results[0].title", equalTo("The Wild Robot")); // Title Validation
        System.out.println("Upcoming Movie Title: " + response.jsonPath().getString("results[0].title"));
    }

    // Negative test: Negative Upcoming Page
    @Test
    public void testGetMovieUpcomingPage() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", 99999) // Negative page for Negative Test Scenario
                .get("/movie/upcoming");

        response.then()
                .statusCode(400); // Bad Request
        System.out.println("Invalid Page Upcoming, Status Code: " + response.getStatusCode());
    }

}
