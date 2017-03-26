package chocolate;

import org.tbot.client.ItemStorage;
import org.tbot.client.Node;
import org.tbot.client.reflection.wrappers.ItemStorageWrapper;
import org.tbot.internal.*;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.ge.GrandExchange;
import org.tbot.methods.tabs.Inventory;
import org.tbot.util.Condition;
import org.tbot.util.Filter;
import org.tbot.wrappers.GrandExchangeOffer;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.WidgetChild;
import org.tbot.wrappers.def.ItemDef;

import java.awt.*;

/**
 * Created by Til Anheier on 22.03.2017.
 */
@Manifest(authors = "choice96", name = "GEScript", description = "Script for Grand Exchange.", version = 1, category = ScriptCategory.OTHER)
public class GEScript extends AbstractRandom implements PaintListener {
    // IDs
    private int coins = 995;

    private GrandExchangeItem item = new GrandExchangeItem();

    private final Color redColor = new Color(255, 0, 0);
    private final Font basicFont = new Font("Arial", 1, 11);

    public GEScript(GrandExchangeItem data) {
        item.setBuyItem(data.getBuyItem());
        item.setSellItem(data.getSellItem());
    }

    public String getText() {
        return "Handling Grand Exchange...";
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean shouldActivate() {
        return !Bank.contains(item.getBuyItem());
    }

    private enum State {
        GRANDEXCHANGE, BANK, ANTIBAN
    }

    private State getState() {
        if (!Inventory.contains(item.getBuyItem()) && !Inventory.contains(coins) && !GrandExchange.isOpen()) {
            return State.BANK;
        }

        if ((!Inventory.contains(item.getBuyItem()) && Inventory.contains(coins)) || (Inventory.contains(item.getBuyItem()))) {
            return State.GRANDEXCHANGE;
        }
        return State.ANTIBAN;
    }

    @Override
    public int loop() {
        switch (getState()) {
            case GRANDEXCHANGE:
                if (GrandExchange.isOpen()) {
                    if (GrandExchange.hasFreeSlots()) {
                        log("Grand Exchange...");

                        if (item.getBuyItemSellPrice() == 0) {
                            int sellPrice = priceFromOffer(item.getBuyItem(), "buy");
                            item.setBuyItemSellPrice(sellPrice);
                        }

                        if (item.getBuyItemBuyPrice() == 0) {
                            if (Inventory.contains(item.getBuyItem())) {
                                int buyPrice = priceFromOffer(item.getBuyItem(), "sell");
                                item.setBuyItemBuyPrice(buyPrice);
                            }
                        }
                    }
                } else {
                    GrandExchange.openMainScreen();
                    Time.sleep(1200, 1800);
                }
                break;
            case BANK:
                if (Bank.isOpen()) {
                    if (Bank.contains(item.getSellItem())) {
                        Bank.withdrawAll(item.getSellItem());
                    }

                    if (!Bank.contains(item.getBuyItem()) && Bank.contains(coins)) {
                        Bank.withdrawAll(coins);
                    }

                    Time.sleep(600, 800);
                    Bank.close();
                } else {
                    Bank.open();
                    Time.sleep(600, 800);
                }
                break;
            case ANTIBAN:
                switch (Random.nextInt(60)) {
                    case 20:
                        Camera.setAngle(Random.nextInt(180));
                        break;
                    case 40:
                        Mouse.move(0, 0);
                        break;
                }
                break;
        }
        return 100;
    }

    @Override
    public void onFinish() {
        LogHandler.log("Random Event finished.");
    }

    private void openOffer(GrandExchangeOffer offer) {
        int slot = offer.getSlot();
        int widgetParent = 465;
        int widgetChild = 7;

        WidgetChild slotWidget = Widgets.getWidget(widgetParent, widgetChild + slot).getChild(2);

        if (Mouse.click(slotWidget.getRandomPoint(), true)) {
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return !slotWidget.isVisible();
                }
            }, Random.nextInt(600, 800));
        }
    }

    private int priceFromOffer(int id, String state) {
        int price = 0;

        if (GrandExchange.getOffer(id) != null) {
            // offer completed?
            if (GrandExchange.getOffer(id).isCompleted()) {
                log("Completing...");

                if (GrandExchange.isCollectScreenOpen()) {
                    price = GrandExchange.getOffer(id).getCoinsTransferred();
                    GrandExchange.collectAll();
                    log("Price: " + price);
                } else {
                    log("Opening offer...");

                    // Open offer
                    openOffer(GrandExchange.getOffer(id));
                }
            }
        } else {
            switch (state) {
                case "buy":
                    log("Buying...");
                    if (!GrandExchange.placeBuyOfferPercentage(id, 1, 125, GrandExchange.getRandomFreeSlot())) {
                        Time.sleep(1200, 1800);
                    }
                    break;
                case "sell":
                    log("Selling...");
                    if (!GrandExchange.placeSellOfferPercentage(Inventory.getFirst(id), 1, 75)) {
                        Time.sleep(1200, 1800);
                    }
                    break;
            }
        }
        return price;
    }

    @Override
    public void onRepaint(Graphics g1) {
        super.onRepaint(g1);
//        Graphics2D g = (Graphics2D) g1;
//        g.setColor(redColor);
//        g.setFont(basicFont);
//        g.drawString("Random Event: GEScript", 10, 332);
    }
}
