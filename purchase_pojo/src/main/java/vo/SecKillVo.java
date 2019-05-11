package vo;

import com.hanjixin.core.pojo.item.Item;
import com.hanjixin.core.pojo.seckill.SeckillGoods;

public class SecKillVo {
    private Item item;
    private SeckillGoods seckillGoods;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public SeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(SeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }
}
