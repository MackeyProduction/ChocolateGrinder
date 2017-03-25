package chocolate;

import org.tbot.internal.*;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.Time;
import org.tbot.methods.ge.GrandExchange;
import org.tbot.methods.tabs.Inventory;

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
        return "GEScript";
    }

    @Override
    public boolean shouldActivate() {
        return !Bank.contains(item.getBuyItem());
    }

    private enum State {
        BUY, SELL, BANK, ANTIBAN
    }

    private State getState() {
        if (!Inventory.contains(item.getBuyItem()) && !Inventory.contains(coins)) {
            return State.BANK;
        }

        if (!Inventory.contains(item.getBuyItem()) && Inventory.contains(coins)) {
            return State.BUY;
        }

        return State.ANTIBAN;
    }

    @Override
    public int loop() {
        switch (getState()) {
            case BUY:
                if (GrandExchange.isOpen()) {
                    if (GrandExchange.hasFreeSlots()) {
                        if (item.getBuyItemSellPrice() == 0) {
                            int slot = GrandExchange.getRandomFreeSlot();
                            GrandExchange.placeBuyOfferPercentage(item.getBuyItem(), 1, 115, slot);
                            Time.sleep(1600, 1800);

                            // offer completed?
                            if (GrandExchange.getOffer(slot).isCompleted()) {
                                int sellPrice = GrandExchange.getOffer(slot).getUnitPrice();
                                item.setBuyItemSellPrice(sellPrice);
                                GrandExchange.collectAll();
                            }
                            Time.sleep(600, 800);
                        }
                    }
                } else {
                    GrandExchange.openMainScreen();
                    Time.sleep(1200, 1800);
                }
                break;
            case SELL:
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
                break;
        }
        return 100;
    }

    @Override
    public void onFinish() {
        LogHandler.log("Random Event finished.");
    }

    @Override
    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setColor(redColor);
        g.setFont(basicFont);
        g.drawString("Random Event: GEScript", 10, 332);
    }
}
