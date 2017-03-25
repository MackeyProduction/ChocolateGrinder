package chocolate;

import org.tbot.internal.AbstractRandom;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Til Anheier on 22.03.2017.
 */
@Manifest(authors = "choice96", name = "Trader", description = "Script for trading.", version = 1, category = ScriptCategory.OTHER)
public class Trader extends AbstractRandom {
    private Date date;
    private DateFormat df;

    public Trader() {
        date = new Date();
        df = DateFormat.getTimeInstance(DateFormat.SHORT);
    }

    private enum State {
        TRADE, WALK, ANTIBAN
    }

    private State getState() {
        return State.ANTIBAN;
    }

    @Override
    public int loop() {
        switch (getState()) {
            case TRADE:
                break;
            case WALK:
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
    public boolean shouldActivate() {
        return Objects.equals(df.format(date), "22:30");
    }
}
