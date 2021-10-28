package testcases;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.GetBookingByIdResponse;
import pojo.PartialUpdateBookingResponse;
import pojo.PostBookingResponse;
import pojo.PutBookingResponse;
import testhelpers.AuthApiHelper;
import testhelpers.BookingApiHelper;
import utils.RestUtils;
import java.util.HashMap;

public class BookingApiTests {

    RestUtils restUtils;
    BookingApiHelper bookingApiHelper;
    AuthApiHelper authApiHelper;
    String token="";

    public BookingApiTests(){
        bookingApiHelper = new BookingApiHelper();
        authApiHelper = new AuthApiHelper();
        restUtils = new RestUtils();
    }

    @Before
    public void generateToken(){
        if(token.isEmpty()) {
            JSONObject requestParams = authApiHelper.generatePostAuthParams();
            Response response = authApiHelper.postAuth(requestParams);

            Assert.assertEquals(response.getStatusCode(), 200);
            token = (String) restUtils.getValueFromResponse(response, "token");
        }
    }

    @Test
    public void createBooking()  {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();

        String checkin = requestParams.getJSONObject("bookingdates").get("checkin").toString();
        String checkout = requestParams.getJSONObject("bookingdates").get("checkout").toString();

        Response response = bookingApiHelper.postBooking(requestParams);
        PostBookingResponse postBookingResponse = response.getBody().as(PostBookingResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(postBookingResponse.getBookingid() > 0);
        Assert.assertEquals(requestParams.get("firstname"), postBookingResponse.getBooking().getFirstname());
        Assert.assertEquals(requestParams.get("lastname"), postBookingResponse.getBooking().getLastname());
        Assert.assertEquals(requestParams.get("totalprice"), postBookingResponse.getBooking().getTotalprice());
        Assert.assertEquals(requestParams.get("depositpaid"), postBookingResponse.getBooking().getDepositpaid());
        Assert.assertEquals(checkin, postBookingResponse.getBooking().getBookingdates().getCheckin());
        Assert.assertEquals(checkout, postBookingResponse.getBooking().getBookingdates().getCheckout());
    }

    @Test
    public void getAllBookingIds() {
        Response response = bookingApiHelper.getAllBookingIds();

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void getAllBookingIdsByName() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();
        Response postResponse = bookingApiHelper.postBooking(requestParams);

        String firstName = (String) restUtils.getValueFromResponse(postResponse, "booking.firstname");
        String lastName = (String) restUtils.getValueFromResponse(postResponse, "booking.lastname");

        HashMap<String, String> filters = new HashMap<String, String>();
        filters.put("firstname", firstName);
        filters.put("lastname", lastName);

        Response response = bookingApiHelper.getAllBookingIds(filters);

        Assert.assertEquals(response.getStatusCode(), 200);
        JSONArray JSONResponseBody = new JSONArray(response.body().asString());
        Assert.assertTrue(JSONResponseBody.length() > 0 );
    }

    @Test
    public void getAllBookingIdsByDates() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();
        Response postResponse = bookingApiHelper.postBooking(requestParams);

        String checkIn = (String) restUtils.getValueFromResponse(postResponse, "booking.bookingdates.checkin");
        String checkOut = (String) restUtils.getValueFromResponse(postResponse, "booking.bookingdates.checkout");

        HashMap<String, String> filters = new HashMap<String, String>();
        filters.put("checkin", checkIn);
        filters.put("checkout", checkOut);

        Response response = bookingApiHelper.getAllBookingIds(filters);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void getBookingById() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();

        Response postResponse = bookingApiHelper.postBooking(requestParams);
        int createdBookingId = (int) restUtils.getValueFromResponse(postResponse, "bookingid");

        Response response = bookingApiHelper.getBookingById(createdBookingId);
        GetBookingByIdResponse getBookingByIdResponse = response.getBody().as(GetBookingByIdResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(requestParams.get("firstname"), getBookingByIdResponse.getFirstname());
        Assert.assertEquals(requestParams.get("lastname"), getBookingByIdResponse.getLastname());
        Assert.assertEquals(requestParams.get("totalprice"), getBookingByIdResponse.getTotalprice());
        Assert.assertEquals(requestParams.get("depositpaid"), getBookingByIdResponse.getDepositpaid());
    }

    @Test
    public void updateBooking() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();

        Response postResponse = bookingApiHelper.postBooking(requestParams);
        int createdBookingId = (int) restUtils.getValueFromResponse(postResponse, "bookingid");

        JSONObject putRequestParams = bookingApiHelper.generatePutBookingParams();
        Response response = bookingApiHelper.putBooking(putRequestParams, createdBookingId, token);

        PutBookingResponse putBookingResponse = response.getBody().as(PutBookingResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(putRequestParams.get("firstname"), putBookingResponse.getFirstname());
        Assert.assertEquals(putRequestParams.get("lastname"), putBookingResponse.getLastname());
        Assert.assertEquals(putRequestParams.get("totalprice"), putBookingResponse.getTotalprice());
        Assert.assertEquals(putRequestParams.get("depositpaid"), putBookingResponse.getDepositpaid());
    }

    @Test
    public void partialUpdateBooking() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();

        Response postResponse = bookingApiHelper.postBooking(requestParams);
        int createdBookingId = (int) restUtils.getValueFromResponse(postResponse, "bookingid");

        JSONObject patchRequestParams = bookingApiHelper.generatePatchBookingParams();
        Response response = bookingApiHelper.patchBooking(patchRequestParams, createdBookingId, token);

        PartialUpdateBookingResponse partialUpdate= response.getBody().as(PartialUpdateBookingResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(patchRequestParams.get("firstname"), partialUpdate.getFirstname());
        Assert.assertEquals(patchRequestParams.get("lastname"), partialUpdate.getLastname());
    }

    @Test
    public void deleteBooking() {
        JSONObject requestParams = bookingApiHelper.generatePostBookingParams();

        Response postResponse = bookingApiHelper.postBooking(requestParams);
        int createdBookingId = (int) restUtils.getValueFromResponse(postResponse, "bookingid");

        Response response = bookingApiHelper.deleteBooking(createdBookingId, token);
        Assert.assertEquals(response.getStatusCode(), 201);
    }

}