package org.afob.prices;

import java.math.BigDecimal;

public interface PriceListener {

    void priceTick(String productId, BigDecimal price);

}
