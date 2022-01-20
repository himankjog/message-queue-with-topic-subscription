package resources.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message {
    private final UUID messageId;
    private final String data;
}
