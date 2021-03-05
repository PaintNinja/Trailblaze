package ga.ozli.minecraftmods.trailblaze;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class RelaunchUtils {

    static void relaunchGame(final ArrayList<String> launchArgs) throws IOException {

        // don't relaunch the game if already relaunched so that we don't cause an infinite loop
        final boolean relaunchedPropertyPresent = System.getProperty("trailblaze.relaunched") != null;
        if (relaunchedPropertyPresent) return;

        // prepend java executable and classpath arg to the launch args
        launchArgs.addAll(0, Arrays.asList("java", "-Dtrailblaze.relaunched=y", "-cp", System.getProperty("java.class.path"), System.getProperty("sun.java.command")));

        // make the process builder and redirect stdout and stderr to where-ever we're currently outputting to
        final ProcessBuilder processBuilder = new ProcessBuilder(launchArgs).inheritIO();

        // for debugging
        /*AtomicInteger argsCount = new AtomicInteger(0);
        StringBuilder sb = new StringBuilder(128);
        processBuilder.command().forEach((arg) -> {
            Trailblaze.LOGGER.info(argsCount + ": " + arg);
            argsCount.getAndIncrement();

            sb.append(arg).append(" ");
        });
        Trailblaze.LOGGER.info("cmd: " + sb.toString());*/

        processBuilder.start();
        System.exit(0);
    }
}
