package resources.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Topic {
    private final UUID id;
    private final String title;
}
