package com.example;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

public class HelloApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.singleton(HelloResource.class);
    }
}
