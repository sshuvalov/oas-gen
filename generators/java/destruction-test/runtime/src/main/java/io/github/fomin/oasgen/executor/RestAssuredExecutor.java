package io.github.fomin.oasgen.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fomin.oasgen.Request;
import io.github.fomin.oasgen.RequestExecutor;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;

// TODO разные реализации под разные базовые клиенты: OkHttp, RestAssured, Apache etc.
public class RestAssuredExecutor implements RequestExecutor {
    private final String baseUri;
    private final ObjectMapper mapper;
    private final List<Filter> filters = new ArrayList<>();

    public RestAssuredExecutor(String baseUri) {
        this(baseUri, new ObjectMapper());
    }

    public RestAssuredExecutor(String baseUri, ObjectMapper mapper) {
        this.baseUri = baseUri;
        this.mapper = mapper;
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    @Override
    public void execute(Request request) {
        RequestSpecification spec = given()
                .baseUri(baseUri)
                .filters(new ResponseLoggingFilter(LogDetail.ALL), new RequestLoggingFilter(LogDetail.ALL))
                .filters(filters);
        spec.headers(request.getHeaders());
        spec.params(request.getParams());
        spec.queryParams(request.getQueryParams());
        spec.pathParams(request.getPathParams());
        if (request.getBody() != null) {
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                request.getBody().writeParam(mapper.createGenerator(outStream));
                spec.body(new ByteArrayInputStream(outStream.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException("Can't build request body", e);
            }
        }

        switch (request.getMethod().toUpperCase(Locale.ROOT)) {
            case "GET":
                spec.get(request.getPath());
                break;
            case "POST":
                spec.post(request.getPath());
                break;
            default:
                throw new IllegalStateException("Invalid http method " + request.getMethod());
        }
    }
}
