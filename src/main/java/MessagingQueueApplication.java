import lombok.extern.log4j.Log4j2;
import queue.MessageQueue;
import resources.publisher.Publisher;
import resources.subscriber.Subscriber;
import resources.topic.Topic;
import utils.CancellationToken;

import java.util.UUID;

@Log4j2
public class MessagingQueueApplication {
    public static void main(String[] args) throws InterruptedException {
        final MessageQueue queue = new MessageQueue();

        final Topic catTopic = queue.createTopic("Cats", 10);
        final Topic dogTopic = queue.createTopic("Dogs", 10);

        final Publisher catPublisher = Publisher.builder()
                .cancellationToken(new CancellationToken())
                .publisherId(UUID.randomUUID())
                .queue(queue)
                .topic(catTopic)
                .build();

        final Publisher dogPublisher = Publisher.builder()
                .cancellationToken(new CancellationToken())
                .publisherId(UUID.randomUUID())
                .queue(queue)
                .topic(dogTopic)
                .build();

        final Subscriber catSubscriber = Subscriber.builder()
                .id(UUID.randomUUID())
                .cancellationToken(new CancellationToken())
                .queue(queue)
                .build();

        queue.subscribe(catSubscriber, catTopic);

        final Subscriber dogSubscriber = Subscriber.builder()
                .id(UUID.randomUUID())
                .cancellationToken(new CancellationToken())
                .queue(queue)
                .build();

        queue.subscribe(dogSubscriber, dogTopic);

        new Thread(catPublisher).start();
        new Thread(catPublisher).start();
        new Thread(dogPublisher).start();
        new Thread(catSubscriber).start();
        new Thread(dogSubscriber).start();

        Thread.sleep(10000);

        catPublisher.cancel();
        dogPublisher.cancel();
        catSubscriber.cancel();
        dogSubscriber.cancel();
    }
}
