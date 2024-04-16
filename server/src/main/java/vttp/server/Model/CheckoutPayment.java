package vttp.server.Model;

public class CheckoutPayment {

    private String name; 
    private long amount; 
    private String successUrl;
	private String cancelUrl;
    private String currency;
    private long quantity; 

    

    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public String getSuccessUrl() {
        return successUrl;
    }
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
    public String getCancelUrl() {
        return cancelUrl;
    }
    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getCurrency() {
            return currency;
        }
        public void setCurrency(String currency) {
            this.currency = currency;
        }

    public CheckoutPayment(String name, long amount, String successUrl, String cancelUrl, String currency) {
        this.name = name;
        this.amount = amount;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
        this.currency = currency;
    }
    public CheckoutPayment() {
    }
    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
 
   


    
    
}
