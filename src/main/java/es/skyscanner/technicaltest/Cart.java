package es.skyscanner.technicaltest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains Cart Model which allows to performs several actions with a Cart.
 *
 * @author vicente.gomez
 */
public class Cart {

  private BigDecimal total = BigDecimal.ZERO;
  private List<CartItem> todaysItemsPrices;
  private Map<String, Integer> scannedProducts = new HashMap<String, Integer>();

  public Cart(List<CartItem> items) {
	todaysItemsPrices = items;
  }

  public Map<String, Integer> getScannedProducts() {
	return scannedProducts;
  }
  
  /**
   * Scans the cartItem and adds it to scanned cart items list. If it already exists just updates 
   * the quantity.
   * 
   * @param quantity int Number of items. 
   * @param name String Name of the item.
   * */
  public void scan(int quantity, String name) {
	if (scannedProducts.containsKey(name)) {
	  int currentQuantity = scannedProducts.get(name);
	  scannedProducts.put(name, quantity + currentQuantity);
	} else {
	  scannedProducts.put(name, quantity);
	}
  }

  /**
   * Calculates the total of all the scanned products that exists in todays_prices.
   * 
   * @return BigDecimal The total amount
   * */
  public BigDecimal total() {
    for (String scannedProduct : scannedProducts.keySet()) {
	  for (CartItem item : todaysItemsPrices) {
		if (scannedProduct.equalsIgnoreCase(item.getItemName())) {
			
		  int quantity = scannedProducts.get(scannedProduct);
		  
		  BigDecimal totalPrice = item.getItemPricePerItem().multiply(BigDecimal.valueOf(quantity))
		      .setScale(2, RoundingMode.HALF_UP);
		  
		  BigDecimal discount = BigDecimal.valueOf(100 - item.getItemDiscount())
			  .divide(new BigDecimal(100));
		  
		  BigDecimal value = totalPrice.multiply(discount);
		  
		  BigDecimal specialOfferDiscount = 
			  specialOfferDiscount(item.getItemSpecialOffer(), value, quantity, 
				  item.getBaseUnit());
		  
		  total = total.add(value.subtract(specialOfferDiscount).setScale(2, RoundingMode.HALF_UP));
		}
	  }
	}
	return total;
  }

  /**
   * Applies the Special offer discount for each item.
   * 
   * @param specialOffer Type of special offer
   * @param value Price of the item
   * @param quantity Scanned item quantity
   * @param baseUnit Number of minimum units. 
   * 
   *  @return BigDecimal Price with the special offer already applied
   * */
  private BigDecimal specialOfferDiscount(SpecialOffers specialOffer, BigDecimal value, 
		int quantity, int baseUnit) {
	final int threeEuros = 3;
	final int fourForThree = 4;
	
	switch (specialOffer) {
	  case TWO_FOR_ONE:		 
		if (quantity > baseUnit) {
          int module = quantity % (2 * baseUnit);
            if (module == 0) {
                return value.divide(new BigDecimal(2));
            } else {
                int quantityInSpecialOffer = quantity - module;
                
                BigDecimal valueWithoutSpecialOffer = value.divide(BigDecimal.valueOf(quantity))
                    .multiply(BigDecimal.valueOf(quantityInSpecialOffer));
                  
                return valueWithoutSpecialOffer.divide(new BigDecimal(2));
              }
          }
          break;
            
	  case FOUR_FOR_THREE_EUROS:
		if (quantity % fourForThree == 0) {
		  return new BigDecimal(threeEuros); 
		} else {
		  System.out.println("Not enough units of product");
		}
		break;
		  
	  case NONE:
		break;
		  
	  default:
		return BigDecimal.ZERO;
	  }
	  
	  return BigDecimal.ZERO;
	}
}
