package chocolate;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;

/**
 * Created by Til Anheier on 10.03.2017.
 */
@Manifest(authors = "choice96", name = "ChocolateGrinder", description = "Grinding Chocolate", version = 1, category = ScriptCategory.MONEY_MAKING)
public class Main extends AbstractScript {
    private int chocolateID = 1973;
    private int chocolateDustID = 1975;
    private int knifeID = 946;

    @Override
    public boolean onStart() {
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

        if (Inventory.contains(chocolateID) && Inventory.contains(knifeID)) {
            return State.GRIND;
        }
        return State.ANTIBAN;
    }

    @Override
    public int loop() {
        switch (getState()) {
            case GRIND:
                if (Players.getLocal().getAnimation() == -1) {
                    if (Inventory.getInSlot(knifeID).click()) {
                        Inventory.getInSlot(chocolateID).click();
                        sleep(600, 800);
                    }
                }
                break;
            case BANK:
                if (Bank.isOpen()) {
                    if (Inventory.contains(knifeID)) {
                        if (Inventory.contains(chocolateDustID)) {
                            Bank.depositAllExcept(knifeID);
                        }

                        if (Bank.contains(chocolateID)) {
                            Bank.withdrawAll(chocolateID);
                        }
                    } else {
                        Bank.withdraw(knifeID, 1);
                    }
                } else {
                    Bank.open();
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
}
