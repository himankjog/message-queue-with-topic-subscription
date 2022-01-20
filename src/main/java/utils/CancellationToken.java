package utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class CancellationToken {
    private final AtomicBoolean cancelled;

    public CancellationToken() {
        this.cancelled = new AtomicBoolean(false);
    }

    public boolean cancel() {
        return cancelled.compareAndSet(false, true);
    }

    public boolean isCancelled() {
        return cancelled.get();
    }
}
