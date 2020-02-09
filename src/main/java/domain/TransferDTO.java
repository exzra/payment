package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class TransferDTO {
    UUID senderID;
    UUID receiverID;
    long value;
}
