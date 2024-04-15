package org.example.data.model.kafka;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "booking")
public class BookingMessage implements Serializable {
    @Id
    String id;

    String userId;

    String arrivalDate;

    String leavingDate;

    public BookingMessage(String userId, String arrivalDate, String leavingDate) {
        this.userId = userId;
        this.arrivalDate = arrivalDate;
        this.leavingDate = leavingDate;
    }
}
