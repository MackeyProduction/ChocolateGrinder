package chocolate;

import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.ScriptController;
import org.tbot.internal.handlers.LogHandler;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Til Anheier on 22.03.2017.
 */
@Manifest(authors = "choice96", name = "RandomHandler", description = "Handling randoms.", version = 1, category = ScriptCategory.OTHER)
public class RandomHandler {
    private GrandExchangeItem values;
    private Date date = new Date();

//    @Override
//    public void run() {
//
//    }

    public void setValues(GrandExchangeItem data) {
        this.values = data;
    }

    public GrandExchangeItem getValues() {
        return this.values;
    }

//    @Override
//    public boolean onStart() {
//        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
//
//        if (this.getValues() != null) {
//            LogHandler.log("Yep.");
//            //GEScript geScript = new GEScript();
//        }
//
//        // Uhrzeit erreicht?
//        if (Objects.equals(df.format(date), "22:30")) {
//            // execute script
//        }
//        return true;
//    }

//    @Override
//    public void onScriptLoop(int i) {
//        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
//
//        LogHandler.log(this.getValues());
//
//        if (this.getValues() != null) {
//            LogHandler.log("Yep.");
//            GEScript geScript = new GEScript();
//            runScript(geScript);
//        }
//
//        // Uhrzeit erreicht?
//        if (Objects.equals(df.format(date), "22:30")) {
//            // execute script
//        }
//    }

//    @Override
//    public void onFinishScript() {
//
//    }
}
