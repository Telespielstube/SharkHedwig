package SessionTest;

import Location.GeoSpatial;
import Message.MessageFlag;
import Message.Request.Offer;
import Misc.Utilities;
import Session.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestTest {

    private static Request request;

//    @Test
//    public void testIfMessageIsVerified() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method verify = request.getClass().getDeclaredMethod("verifyOfferData", Offer.class);
//        verify.setAccessible(true);
//        Offer offer = new Offer(Utilities.createUUID(), MessageFlag.OFFER, Utilities.createTimestamp(), 0.0, 3000, new GeoSpatial().getCurrentLocation());
//        verify.invoke(request, offer);
//    }
}
