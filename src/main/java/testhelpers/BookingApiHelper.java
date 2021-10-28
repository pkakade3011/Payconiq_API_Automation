package testhelpers;

import endpoints.BookingEndpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.RestUtils;
import java.util.HashMap;
import java.util.Map;

public class BookingApiHelper {

    RestUtils restUtils;
    JsonUtils jsonUtils;

    public BookingApiHelper() {
        restUtils = new RestUtils();
        jsonUtils = new JsonUtils();
    }

    public Response postBooking(JSONObject requestParams) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                                                    .contentType(ContentType.JSON)
                                                    .body(requestParams.toString());

        String url =  BookingEndpoints.baseUrl + BookingEndpoints.createBookingUrl;
        return restUtils.executePOST(requestSpecification, url);
    }

    public Response putBooking(JSONObject requestParams, int bookingId, String token) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Cookie", "token="+token)
                .body(requestParams.toString());

        String url =  BookingEndpoints.baseUrl + String.format(BookingEndpoints.updateBookingUrl, bookingId);
        return restUtils.executePUT(requestSpecification, url);
    }

    public Response patchBooking(JSONObject requestParams, int bookingId, String token) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Cookie", "token="+token)
                .body(requestParams.toString());

        String url =  BookingEndpoints.baseUrl + String.format(BookingEndpoints.partialUpdateBookingUrl, bookingId);
        return restUtils.executePATCH(requestSpecification, url);
    }

    public Response getBookingById(int bookingId) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON);

        String url =  BookingEndpoints.baseUrl + String.format(BookingEndpoints.getBookingUrl, bookingId);
        return restUtils.executeGET(requestSpecification, url);
    }

    public Response getAllBookingIds() {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON);

        String url =  BookingEndpoints.baseUrl + BookingEndpoints.getAllBookingIdsUrl;
        return restUtils.executeGET(requestSpecification, url);
    }

    public Response getAllBookingIds(HashMap<String,String> filters) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON);

        String url =  BookingEndpoints.baseUrl +BookingEndpoints.getAllBookingIdsByNameUrl + generateQueryString(filters);
        return restUtils.executeGET(requestSpecification, url);
    }

    public Response deleteBooking(int id, String token) {
        RequestSpecification requestSpecification = RestAssured.with().urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Cookie", "token="+token);

        String url =  BookingEndpoints.baseUrl + String.format(BookingEndpoints.deleteBookingUrl, id);
        return restUtils.executeDELETE(requestSpecification, url);
    }

    public JSONObject generatePostBookingParams() {
        Map<String, Object> mapObject = jsonUtils.readJsonFields("postBookingPayload.json");
        return new JSONObject(mapObject);
    }

    public JSONObject generatePutBookingParams() {
        Map<String, Object> mapObject = jsonUtils.readJsonFields("putBookingPayload.json");
        return new JSONObject(mapObject);
    }

    public JSONObject generatePatchBookingParams() {
        Map<String, Object> mapObject = jsonUtils.readJsonFields("patchBookingPayload.json");
        return new JSONObject(mapObject);
    }

    private String generateQueryString(HashMap<String,String> filters) {
        int count = 1;
        String queryString = "";
        for(Map.Entry<String,String> map: filters.entrySet()){
            queryString = queryString + map.getKey() + "=" + map.getValue();

            if(count != filters.size()){
                queryString = queryString + "&";
            }
            count++;
        }

        return queryString;
    }

}
