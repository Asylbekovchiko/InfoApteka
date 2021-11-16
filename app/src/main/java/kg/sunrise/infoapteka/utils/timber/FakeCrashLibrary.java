package kg.sunrise.infoapteka.utils.timber;

import timber.log.Timber;

public class FakeCrashLibrary {
    public static void log(int priority, String tag, String message) {
        Timber.e(message);
    }

    public static void logWarning(Throwable t) {
        Timber.w(t);
    }

    public static void logError(Throwable t) {
        Timber.e(t);
    }

    private FakeCrashLibrary() {
        throw new AssertionError("No instances.");
    }
}
