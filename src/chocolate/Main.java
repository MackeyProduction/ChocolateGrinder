package chocolate;

import org.tbot.bot.Account;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.io.PriceLookup;
import org.tbot.methods.tabs.Inventory;

import java.awt.*;

/**
 * Created by Til Anheier on 10.03.2017.
 */
@Manifest(authors = "choice96", name = "ChocolateGrinder", description = "Grinding Chocolate", version = 1, category = ScriptCategory.MONEY_MAKING)
public class Main extends AbstractScript implements PaintListener {
    private int chocolateID = 1973;
    private int chocolateDustID = 1975;
    private int knifeID = 946;
    private int chocolate = 0;
    private boolean count = false;

    // Paint
    private final Color greenColor = new Color(51, 153, 0);
    private final Color blackColor = new Color(0, 0, 0);
    private final Color whiteColor = new Color(255, 255, 255);

    private final BasicStroke stroke = new BasicStroke(1);

    private final Font headlineFont = new Font("Arial", 0, 9);
    private final Font basicFont = new Font("Arial", 1, 11);

    private long startTime = 0;
    private int chocolatePrice = 0;
    private int chocolateDustPrice = 0;

    @Override
    public boolean onStart() {
        startTime = System.currentTimeMillis();
        chocolatePrice = PriceLookup.getPrice(chocolateID);
        chocolateDustPrice = PriceLookup.getPrice(chocolateDustID);

        LogHandler.log("Script started.");
        return true;
    }

    private enum State {
        BANK, GRIND, ANTIBAN
    }

    private State getState() {
        if (!Inventory.contains(chocolateID) || !Inventory.contains(knifeID)) {
            return State.BANK;
        }

        if (Inventory.contains(chocolateID) && Inventory.contains(knifeID) && !Bank.isOpen()) {
            return State.GRIND;
        }
        return State.ANTIBAN;
    }

    @Override
    public int loop() {
        switch (getState()) {
            case GRIND:
                Inventory.useItemOn(knifeID, chocolateID);
                Time.sleep(600, 800);
                break;
            case BANK:
                if (Bank.isOpen()) {
                    if (Inventory.contains(knifeID)) {
                        if (Inventory.contains(chocolateDustID)) {
                            Bank.depositAllExcept(knifeID);
                        }

                        if (Bank.contains(chocolateID)) {
                            Bank.withdrawAll(chocolateID);
                        } else {
                            LogHandler.log("D`oh! No chocolate bars available.");
                            return -1;
                        }

                        Time.sleep(600, 800);
                        Bank.close();
                    } else {
                        Bank.withdraw(knifeID, 1);
                    }
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
        LogHandler.log("Script finished.");
    }

    private int getDusts()  {
        if (Inventory.contains(chocolateID)) {
            count = true;
            return chocolate + Inventory.getCount(chocolateDustID);
        }

        if (count) {
            count = false;
            chocolate += Inventory.getCount(chocolateDustID);
        }
        return chocolate;
    }

    @Override
    public void onRepaint(Graphics g1) {
        // Zeit
        long millis = System.currentTimeMillis() - startTime;
        long hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;

        // Schokolade in der Stunde
        int chocolateHour = (int) ((getDusts()) * 3600000D / (System.currentTimeMillis() - startTime));

        // Gold in der Stunde
        int profit = chocolateDustPrice - chocolatePrice;
        int profitHour = (int) ((profit) * 3600000D / (System.currentTimeMillis() - startTime));

        Graphics2D g = (Graphics2D) g1;
        g.setColor(greenColor);
        g.fillRoundRect(5, 5, 121, 143, 16, 16);
        g.setColor(blackColor);
        g.setStroke(stroke);
        g.drawRoundRect(5, 5, 121, 143, 16, 16);
        g.setFont(headlineFont);
        g.setColor(whiteColor);
        g.drawString("Version: " + getManifest().version(), 14, 52);
        g.drawString("Runtime: " + hours + ":" + minutes + ":" + seconds, 14, 70);
        g.drawString("Chocolate: " + getDusts(), 14, 88);
        g.drawString("Chocolate/HR: " + chocolateHour, 14, 106);
        g.drawString("Profit: " + getDusts() * profit, 14, 124);
        g.drawString("Profit/HR: " + getDusts() * profitHour, 14, 142);
        g.setFont(basicFont);
        g.drawString("Chocolate Grinder", 15, 27);
    }
}
