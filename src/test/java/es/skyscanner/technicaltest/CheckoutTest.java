package es.skyscanner.technicaltest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CheckoutTest extends TestCase {

  List<CartItem> todaysPrices = new ArrayList<CartItem>();

  @Override
  @Before
  public void setUp() {
    todaysPrices.clear();
  }

  @Test
  public void testCheckoutOneCartItem() {    	
    todaysPrices.add(new CartItem.CartItemBuilder("Milk", BigDecimal.valueOf(2.50)).build());
    
    Cart cart = new Cart(todaysPrices);

    cart.scan(2, "Milk");

    assertTrue(cart.total().compareTo(BigDecimal.valueOf(5.00)) >= 0);
  }

  @Test
  public void testScanCartItemNotInTodaysPrices() {
    todaysPrices.add(new CartItem.CartItemBuilder("Milk", BigDecimal.valueOf(1.00)).build());
    Cart cart = new Cart(todaysPrices);

    cart.scan(3, "Product Not In Todays Prices");

    assertTrue(cart.total().compareTo(BigDecimal.ZERO) >= 0);
  }
    
  @Test
  public void testCheckoutCartHappyPath() {
    todaysPrices.add(new CartItem.CartItemBuilder("Butter", BigDecimal.valueOf(1.94)).build());
    todaysPrices.add(new CartItem.CartItemBuilder("Bread", BigDecimal.valueOf(1.60))
        .itemSpecialOffer(SpecialOffers.TWO_FOR_ONE).build());
    todaysPrices.add(new CartItem.CartItemBuilder("Tuna can", BigDecimal.valueOf(0.99))
        .discountToApply(5).build());
    todaysPrices.add(new CartItem.CartItemBuilder("Milk", BigDecimal.valueOf(2.00))
        .discountToApply(10).itemSpecialOffer(SpecialOffers.TWO_FOR_ONE).build());
    todaysPrices.add(new CartItem.CartItemBuilder("Beef", BigDecimal.valueOf(2.95))
        .discountToApply(30).itemSpecialOffer(SpecialOffers.TWO_FOR_ONE).build());
        
    Cart cart = new Cart(todaysPrices);

    cart.scan(3, "Butter");
    cart.scan(2, "Bread");
    cart.scan(4, "Tuna can");
    cart.scan(2, "Milk");
    cart.scan(1120, "Beef");
    
    assertTrue(cart.total().compareTo(BigDecimal.valueOf(27.72)) >= 0);
  }
    
  @Test
  public void testScanWithoutTodaysPricesInitialized() {
    Cart cart = new Cart(todaysPrices);
      
    cart.scan(1, "Wine");
        
    assertTrue(cart.total().compareTo(BigDecimal.ZERO) >= 0);
  }
  
  @Test
  public void testCheckoutSingleCartItemWithAllFields() {
	todaysPrices.add(new CartItem.CartItemBuilder("Beef", BigDecimal.valueOf(2.95)).baseUnit(100)
	   .discountToApply(30).itemSpecialOffer(SpecialOffers.TWO_FOR_ONE).build());
      
	Cart cart = new Cart(todaysPrices);

    cart.scan(1120, "Beef");

    assertTrue(cart.total().compareTo(BigDecimal.valueOf(12.80)) >= 0);
  }
  
  @Test
  public void testCheckoutSingleCartItemWithSpecialOfferFourPerThree() {
	todaysPrices.add(new CartItem.CartItemBuilder("Water", BigDecimal.valueOf(2.00))
	    .itemSpecialOffer(SpecialOffers.FOUR_FOR_THREE_EUROS).build());
	  
    Cart cart = new Cart(todaysPrices);

    cart.scan(4, "Water");

    assertTrue(cart.total().compareTo(BigDecimal.valueOf(3.00)) >= 0 );
  }
  
  @Test
  public void testCheckoutSingleCartItemWithBaseUnit() {
	todaysPrices.add(new CartItem.CartItemBuilder("Beef", BigDecimal.valueOf(3.00)).baseUnit(100)
	    .build());
      
	Cart cart = new Cart(todaysPrices);

    cart.scan(250, "Beef");

    assertTrue(cart.total().compareTo(BigDecimal.valueOf(7.50)) >= 0 );
  }
}
