package resources.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import resources.subscriber.ISubscriber;

@Getter
@Builder
public class TopicSubscriber {
    @NonNull private final ISubscriber subscriber;
    @Setter
    @NonNull private Topic topic;
}
