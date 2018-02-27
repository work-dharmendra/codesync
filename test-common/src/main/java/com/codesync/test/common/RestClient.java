package com.codesync.test.common;

import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.internal.HttpUrlConnector;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class RestClient {

    private String baseUrl;
    private Client client;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        client = ClientBuilder.newClient();
    }

    public TestDto post(String url, TestDto testDto) {
        TestDto result = null;
        try {

            result = client.target(new URL(new URL(baseUrl), url).toURI())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(testDto, MediaType.APPLICATION_JSON), TestDto.class)
                    ;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void main(String []args){
        RestClient restClient = new RestClient("http://localhost:8080/inttest/");
        TestDto testDto = new TestDto(IntTestList.BASIC_METHOD_BODY_SYNC, "com.codesync.inttest.test.basic.BasicFunctionality"
                , "sync", new Class[]{});
        //TestDto result = restClient.post("inttest", testDto);

        //System.out.println(result);
        testDto.result = "bb";
        System.out.println(new Gson().toJson(testDto));
    }
}
