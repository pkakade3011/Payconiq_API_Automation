package pojo;

import com.google.gson.annotations.Expose;

public class PostBookingResponse {

    @Expose
    private Booking booking;
    @Expose
    private Long bookingid;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Long getBookingid() {
        return bookingid;
    }

    public void setBookingid(Long bookingid) {
        this.bookingid = bookingid;
    }

}
