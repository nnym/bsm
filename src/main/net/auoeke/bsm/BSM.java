package net.auoeke.bsm;

public class BSM {
    public static RuntimeException rethrow(Throwable throwable) {
        return rethrowInternal(throwable);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException rethrowInternal(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
