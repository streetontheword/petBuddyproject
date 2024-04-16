package vttp.server.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.Stripe;
import com.google.gson.Gson;

import com.stripe.exception.StripeException;
import vttp.server.Model.CheckoutPayment;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;

@RestController
@CrossOrigin 
@RequestMapping(path="/api")
public class PaymentController {

	 @Value("${host.url}")
	private String url; 

	@Value("${stripe.api.key}")
	private String STRIPE_API_KEY;
	
    private static Gson gson = new Gson();

    //   private static void init() {
	// 	Stripe.apiKey = "sk_test_51OrN5mDuPe4zY1Gs4tWOlSl8haSDLlpfmjsNtICcb8o1XNKyg0YgDf64iI7dBZCs53HKIEapzAl5F6Ix5fwwYXBe00yc81u01Z";
	// }

    // @PostMapping (path="/create-payment-intent")
    // public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
          
    //   PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
    //       .setAmount(15 * 100L)
    //       .setCurrency("sgd")
    //       .build();

    //   // Create a PaymentIntent with the order amount and currency
    //   PaymentIntent paymentIntent = PaymentIntent.create(params);

    //   return new CreatePaymentResponse(paymentIntent.getClientSecret());

    // };

	
    @PostMapping(path="/payment")
    public String paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
		Stripe.apiKey = STRIPE_API_KEY;
		String success = url + payment.getSuccessUrl();
		
		
		String cancel =  url + payment.getCancelUrl();
		System.out.println(cancel);

      System.out.println("FROM ANGULAR  " + payment.getAmount());
		// We initilize stripe object with the api key
		// init();
		// We create a  stripe session parameters
		SessionCreateParams params = SessionCreateParams.builder()
				// We will use the credit card payment method 
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(success)
				.setCancelUrl(
					cancel)
				.addLineItem(
						SessionCreateParams.LineItem.builder().setQuantity(1L)
								.setPriceData(
										SessionCreateParams.LineItem.PriceData.builder()
                     .setCurrency("sgd")
												.setUnitAmount(payment.getAmount())
												.setProductData(SessionCreateParams.LineItem.PriceData.ProductData
														.builder().setName(payment.getName()).build())
												.build())
								.build())
				.build();
  // create a stripe session
		Session session = Session.create(params);
		Map<String, String> responseData = new HashMap<>();
    // We get the sessionId and we putted inside the response data you can get more info from the session object
		responseData.put("id", session.getId());
      // We can return only the sessionId as a String
		return gson.toJson(responseData);
	}

}

   
    

