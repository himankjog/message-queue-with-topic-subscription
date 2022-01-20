package queue;

import lombok.NonNull;
import resources.message.Message;
import resources.subscriber.ISubscriber;
import resources.subscriber.Subscriber;
import resources.topic.Topic;
import resources.topic.TopicHandler;
import resources.topic.TopicSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MessageQueue {
    private final Map<UUID, TopicHandler> topicHandlers;
    private final Map<UUID, TopicSubscriber> topicSubscribers;

    public MessageQueue() {
        topicHandlers = new HashMap<>();
        topicSubscribers = new HashMap<>();
    }

    public Topic createTopic(@NonNull final String title, @NonNull final Integer capacity) {
        final UUID topicId = UUID.randomUUID();
        final Topic newTopic = new Topic(topicId, title);
        final TopicHandler newTopicHandler = new TopicHandler(newTopic, capacity);
        topicHandlers.put(topicId, newTopicHandler);
        return newTopic;
    }

    public void subscribe(@NonNull final ISubscriber subscriber, @NonNull final Topic topic) {
        final TopicSubscriber topicSubscriber = TopicSubscriber.builder().subscriber(subscriber).topic(topic).build();
        topicSubscribers.put(subscriber.getId(), topicSubscriber);
    }


    public void publish(@NonNull final Message message, @NonNull final Topic topic) {
        final TopicHandler topicHandler = topicHandlers.get(topic.getId());
        new Thread(
                () -> topicHandler.publishMessage(message)
        ).start();
    }

    public Future<Message> consume(@NonNull final Subscriber subscriber) {
        final TopicSubscriber topicSubscriber = topicSubscribers.get(subscriber.getId());
        final Topic topic = topicSubscriber.getTopic();
        final TopicHandler topicHandler = topicHandlers.get(topic.getId());
        final Callable<Message> messageCallable = topicHandler::getMessage;
        final FutureTask<Message> messageFuture = new FutureTask<>(messageCallable);
        new Thread(messageFuture).start();
        return messageFuture;
    }
}
