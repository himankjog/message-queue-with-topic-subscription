package resources.topic;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import resources.message.Message;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class TopicHandler {
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition EMPTY_QUEUE = LOCK.newCondition();
    private static final Condition QUEUE_FULL = LOCK.newCondition();

    private final Topic topic;
    private final Queue<Message> messages;
    private final Integer capacity;

    public TopicHandler(@NonNull Topic topic, @NonNull Integer capacity) {
        this.topic = topic;
        this.capacity = capacity;
        messages = new LinkedList<>();
    }

    public UUID getTopicId() {
        return this.topic.getId();
    }

    public void publishMessage(@NonNull Message message) {
        LOCK.lock();
        try {
            while (messages.size() == capacity) {
                QUEUE_FULL.await();
            }
            messages.add(message);
            log.info("Published message: {} to topic: {}", message.getData(), topic.getTitle());
            EMPTY_QUEUE.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public Message getMessage() throws InterruptedException {
        LOCK.lock();
        try {
            while (messages.isEmpty()) {
                EMPTY_QUEUE.await();
            }
            final Message message = messages.remove();
            QUEUE_FULL.signalAll();
            return message;
        } finally {
            LOCK.unlock();
        }
    }
}
