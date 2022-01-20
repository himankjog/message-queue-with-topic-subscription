package resources.publisher;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import queue.MessageQueue;
import resources.message.Message;
import resources.topic.Topic;
import utils.CancellationToken;
import utils.RandomMessageCreator;

import java.util.UUID;

@Log4j2
@Builder
public class Publisher implements Runnable {
    @NonNull private final CancellationToken cancellationToken;
    @NonNull private final UUID publisherId;
    @NonNull private final MessageQueue queue;
    @Setter
    private Topic topic;
    @Setter
    private Thread thread;

    @Override
    public void run() {
        setThread(Thread.currentThread());
        while (!cancellationToken.isCancelled() && !Thread.currentThread().isInterrupted()) {
            final Message message = RandomMessageCreator.createRandomMessage();
            queue.publish(message, topic);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        cancel();
        thread.interrupt();
        log.error("Interrupted Publisher: {}", publisherId);
    }

    public void cancel() {
        cancellationToken.cancel();
        log.error("Cancelled Publisher: {}", publisherId);
    }
}
