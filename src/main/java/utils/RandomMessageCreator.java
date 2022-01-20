package utils;

import resources.message.Message;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomMessageCreator {
    private static final String MESSAGE_PREFIX = "MESSAGE";
    private static final AtomicInteger messageCount = new AtomicInteger(0);

    public static Message createRandomMessage() {
        final UUID messageId = UUID.randomUUID();
        final String messageData = MESSAGE_PREFIX + messageCount.incrementAndGet();
        return new Message(messageId, messageData);
    }
}
