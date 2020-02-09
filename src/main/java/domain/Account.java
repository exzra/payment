package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

//@JsonTypeName("echo")
@AllArgsConstructor
@Data
public class Account {
    private UUID id;
    private long value;
}
