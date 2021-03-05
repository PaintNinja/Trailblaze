package ga.ozli.minecraftmods.trailblaze;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class GCUtils {

    public static String getCurrentGCName() {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans())
            if (bean.isValid()) return bean.getName();
        return "Unknown";
    }

    public static Pair<GC, String> detectedGC() {
        final String gcNameString = getCurrentGCName();
        final GC detectedGC;

        if (gcNameString.contains("G1")) detectedGC = GC.G1;
        else if (gcNameString.contains("Shenandoah")) detectedGC = GC.Shenandoah;
        else if (gcNameString.contains("ZGC")) detectedGC = GC.ZGC;
        else if (gcNameString.startsWith("PS")) detectedGC = GC.Parallel;
        else if (gcNameString.contains("ParNew")) detectedGC = GC.CMS;
        else if (gcNameString.equals("Copy")) detectedGC = GC.Serial;
        else if (gcNameString.contains("Epsilon")) detectedGC = GC.Epsilon;
        else detectedGC = GC.Unknown;

        return Pair.of(detectedGC, gcNameString);
    }

    enum GC { Epsilon, Serial, Parallel, CMS, G1, Shenandoah, ZGC, Unknown }

}
