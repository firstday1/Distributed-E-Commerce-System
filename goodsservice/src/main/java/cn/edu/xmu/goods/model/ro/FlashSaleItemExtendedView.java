package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class FlashSaleItemExtendedView {
    private final Long id;
    private final ReturnGoodsSkuVo goodsSku;
    private final Long price;
    private final Integer quantity;
    private final LocalDateTime gmtCreate;
    private final LocalDateTime gmtModified;

    public FlashSaleItemExtendedView(FlashSale.Item item, ReturnGoodsSkuVo sku) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.gmtCreate = item.getGmtCreate();
        this.gmtModified = item.getGmtModified();
        this.goodsSku = sku;
    }
}
