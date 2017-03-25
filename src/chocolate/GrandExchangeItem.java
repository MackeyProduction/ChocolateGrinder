package chocolate;

/**
 * Created by Til Anheier on 22.03.2017.
 */
public class GrandExchangeItem {
    private int buyItem;
    private int sellItem;
    private int buyItemBuyPrice = 0;
    private int buyItemSellPrice = 0;

    public GrandExchangeItem() {
    }

    public int getBuyItem() {
        return buyItem;
    }

    public void setBuyItem(int id) {
        this.buyItem = id;
    }

    public int getSellItem() {
        return sellItem;
    }

    public void setSellItem(int id) {
        this.sellItem = id;
    }

    public int getBuyItemBuyPrice() {
        return buyItemBuyPrice;
    }

    public void setBuyItemBuyPrice(int buyItemBuyPrice) {
        this.buyItemBuyPrice = buyItemBuyPrice;
    }

    public int getBuyItemSellPrice() {
        return buyItemSellPrice;
    }

    public void setBuyItemSellPrice(int buyItemSellPrice) {
        this.buyItemSellPrice = buyItemSellPrice;
    }
}
