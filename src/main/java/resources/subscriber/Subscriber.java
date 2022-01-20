package resources.subscriber;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import queue.MessageQueue;
import resources.message.Message;
import utils.CancellationToken;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Log4j2
@Getter
@Builder
public class Subscriber implements ISubscriber {
    @NonNull private final UUID id;
    @NonNull private final CancellationToken cancellationToken;
    @NonNull private final MessageQueue queue;
    @Setter
    private Thread thread;

    @Override
    public void consume() {
        Future<Message> messageFuture = queue.consume(this);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            log.info("Subscriber: {} consumed message: {}", getId(), messageFuture.get().getData());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        setThread(Thread.currentThread());
        while (keepRunning()) {
            consume();
        }
    }

    private boolean keepRunning() {
        return !cancellationToken.isCancelled() && !thread.isInterrupted();
    }

    public void cancel() {
        cancellationToken.cancel();
        log.error("Cancelled Subscriber: {}", getId());
    }

    public void stop() {
        cancel();
        thread.interrupt();
        log.error("Interrupted Subscriber: {}", getId());
    }
}
