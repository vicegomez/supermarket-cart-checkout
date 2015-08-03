package es.skyscanner.technicaltest;

import java.math.BigDecimal;

/**
 * Contains Structure to create new Cart Item Objects. It uses Builder pattern
 * instead of telescope constructor pattern. (Overload constructor with
 * different number of parameters)
 *
 * @author vicente.gomez
 */
public class CartItem {

  private String name;
  private BigDecimal pricePerItem;
  private int discountToApply;
  private int baseUnit;
  private SpecialOffers itemSpecialOffer;
  
  // Use of Builder pattern instead of telescoping constructor pattern
  public static class CartItemBuilder {
	private String name;
	private BigDecimal pricePerItem;
	private int discountToApply = 0;
    private int baseUnit = 1;
	
	private SpecialOffers itemSpecialOffer = SpecialOffers.NONE;

	public CartItemBuilder(String name, BigDecimal pricePerItem) {
	  this.name = name;
	  this.pricePerItem = pricePerItem;
	}
	
	public CartItemBuilder discountToApply(int discountToApply) {
	  this.discountToApply = discountToApply;
	  return this;
	}
	
	public CartItemBuilder pricePerItem(BigDecimal pricePerItem) {
	  this.pricePerItem = pricePerItem;
	  return this;
	}
	
    public CartItemBuilder baseUnit(int baseUnit) {
      this.baseUnit = baseUnit;
      return this;
    }

	public CartItemBuilder itemSpecialOffer(SpecialOffers specialOffer) {
	  itemSpecialOffer = specialOffer;
	  return this;
	}

	public CartItem build() {
	  return new CartItem(this);
	}
  }

	public CartItem(CartItemBuilder cartItemBuilder) {
	  name = cartItemBuilder.name;
	  pricePerItem = cartItemBuilder.pricePerItem;	  
	  discountToApply = cartItemBuilder.discountToApply;	
	  itemSpecialOffer = cartItemBuilder.itemSpecialOffer;
      baseUnit = cartItemBuilder.baseUnit;
	}

	public String getItemName() {
	  return name;
	}

	public BigDecimal getItemPricePerItem() {
	  return pricePerItem;
	}

	public int getItemDiscount() {
	  return discountToApply;
	}

	public SpecialOffers getItemSpecialOffer() { 
	  return itemSpecialOffer; 
	}
	
    public int getBaseUnit() {
      return baseUnit;
    }
}
