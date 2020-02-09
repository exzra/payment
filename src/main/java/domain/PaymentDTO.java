package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class PaymentDTO {
    private UUID id;
    private long diff;
}
