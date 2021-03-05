package ga.ozli.minecraftmods.trailblaze;

import cpw.mods.modlauncher.ArgumentHandler;
import cpw.mods.modlauncher.Launcher;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("trailblaze")
public class Trailblaze
{
    // Directly reference a log4j logger.
    static final Logger LOGGER = LogManager.getLogger();

    public Trailblaze() {
        // Register the setup method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);

        setup();
    }

    //private void setup(final FMLCommonSetupEvent event) {
    private void setup() {
        LOGGER.info("Detected GC: " + GCUtils.detectedGC());

        String[] launchArgs = null;
        try {
            launchArgs = getLaunchArgs();
            /*LOGGER.info(System.getProperties());
            AtomicInteger argsNum = new AtomicInteger();
            Arrays.stream(launchArgs).forEach((arg) -> {
                LOGGER.info(argsNum + ": " + arg);
                argsNum.getAndIncrement();
            });*/
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error("Unable to grab a copy of the game args from ModLauncher: ", e);
            e.printStackTrace();
        }

        try {
            assert launchArgs != null;
            RelaunchUtils.relaunchGame(new ArrayList<>(Arrays.asList(launchArgs)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getLaunchArgs() throws IllegalAccessException, NoSuchFieldException {
        final Field argHandlerField = Launcher.INSTANCE.getClass().getDeclaredField("argumentHandler");
        argHandlerField.setAccessible(true);
        final ArgumentHandler argumentHandler = (ArgumentHandler) argHandlerField.get(Launcher.INSTANCE);

        final Field argsField = argumentHandler.getClass().getDeclaredField("args");
        argsField.setAccessible(true);
        return (String[]) argsField.get(argumentHandler);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("trailblaze", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
}
