package resources.subscriber;

import java.util.UUID;

public interface ISubscriber extends Runnable {
    UUID getId();
    void consume();
}
