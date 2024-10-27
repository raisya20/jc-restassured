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
                .body("results[0].title", equalTo("Venom: The Last Dance")); // Validasi judul film yang diharapkan
        System.out.println("Now Playing Movie Title: " + response.jsonPath().getString("results[0].title"));
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
                .body("results[0].title", equalTo("The Wild Robot")); // Validasi judul film yang diharapkan
        System.out.println("Popular Movie Title: " + response.jsonPath().getString("results[0].title"));
    }

    // Negative test : Halaman negatif
    @Test
    public void testGetMoviePopularInvalidPage() {
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US")
                .queryParam("page", -1) // Halaman negatif untuk kasus negatif
                .get("/movie/popular");

        response.then()
                .statusCode(400); // Mengharapkan Bad Request
        System.out.println("Invalid Page Test Status Code: " + response.getStatusCode());
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
                .body("status_message", equalTo("Success.")); // Validasi pesan sukses
        System.out.println("Post Rating Response: " + response.jsonPath().getString("status_message"));
    }

    // Negative test : Post Rating >10
    @Test
    public void testPostInvalidMovieRating() {
        int movieId = 123; // Replace with a valid movie ID
        Response response = given()
                .header("Authorization", myToken)
                .contentType("application/json")
                .body("{\"value\": 15}") // Nilai rating di luar batas (1-10)
                .post("/movie/" + movieId + "/rating");

        response.then()
                .statusCode(400); // Mengharapkan Bad Request
        System.out.println("Invalid Rating Response Status Code: " + response.getStatusCode());
    }

    // Scenario Test 4: Get Movie Details
    @Test
    public void testGetMovieDetails() {
        int movieId = 550; // movie ID untuk "Fight Club"
        Response response = given()
                .header("Authorization", myToken)
                .queryParam("language", "en-US") // Menambahkan query parameter 'language'
                .get("/movie/" + movieId);

        response.then()
                .statusCode(200) // Memastikan status code 200 OK
                .body("title", notNullValue()) // Verifikasi bahwa respons berisi movie details
                .body("title", equalTo("Fight Club")); // Memastikan bahwa movie memiliki informasi "title movie"
        System.out.println("Movie Details Title: " + response.jsonPath().getString("title"));
    }
}
