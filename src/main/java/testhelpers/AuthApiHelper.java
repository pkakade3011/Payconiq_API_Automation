package testhelpers;

import endpoints.AuthEndpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.RestUtils;
import java.util.Map;

public class AuthApiHelper {

    RestUtils restUtils;
    JsonUtils jsonUtils;

    public AuthApiHelper() {
        restUtils = new RestUtils();
        jsonUtils = new JsonUtils();
    }

    public Response postAuth(JSONObject requestParams) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                                                    .contentType(ContentType.JSON)
                                                    .body(requestParams.toString());

        String url =  AuthEndpoints.baseUrl + AuthEndpoints.createTokenUrl;
        return restUtils.executePOST(requestSpecification, url);
    }

    public JSONObject generatePostAuthParams() {
        Map<String, Object> mapObject = jsonUtils.readJsonFields("postAuthPayload.json");
        return new JSONObject(mapObject);
    }

}
