package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;

public class HelloApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
                HelloResource.class,
                PostResource.class,
                ShoutResource.class));
    }
}
