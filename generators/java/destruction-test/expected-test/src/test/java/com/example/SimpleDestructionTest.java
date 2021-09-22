package com.example;

import com.example.routes.AbstractSimpleDestructionTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fomin.oasgen.RequestExecutor;
import io.github.fomin.oasgen.executor.RestAssuredExecutor;

public class SimpleDestructionTest extends AbstractSimpleDestructionTest {

    private RequestExecutor executor;

    @Override
    public RequestExecutor getExecutor() {
        if (executor == null) {
            executor = new RestAssuredExecutor("https://google.com/", new ObjectMapper());
        }
        return executor;
    }

    @Override
    public void healthCheck() {
        System.out.println("check");
    }
}


