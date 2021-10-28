package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {

    private Response response = null;
    private RequestSpecification requestSpec;

    public Response executePOST(RequestSpecification rs, String url) {
        requestSpec = rs;
        response = requestSpec.post(url);
        return response;
    }

    public Response executePUT(RequestSpecification rs, String url) {
        requestSpec = rs;
        response = requestSpec.put(url);
        return response;
    }

    public Response executePATCH(RequestSpecification rs, String url) {
        requestSpec = rs;
        response = requestSpec.patch(url);
        return response;
    }

    public Response executeGET(RequestSpecification rs, String url) {
        requestSpec = rs;
        response = requestSpec.get(url);
        return response;
    }

    public Response executeDELETE(RequestSpecification rs, String url) {
        requestSpec = rs;
        response = requestSpec.delete(url);
        return response;
    }

    public Object getValueFromResponse (Response response, String key) {
        JsonPath jsonPathEvaluator = response.jsonPath();
        return jsonPathEvaluator.get(key);
    }

}
