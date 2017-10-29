package com.example.benchmarks;

import static spark.Spark.*;

public class HelloSparkBenchmark {

    public static void main(final String[] args) {
        port(8080);
        get("/", (request, response) -> "Hello World!");
    }
}
