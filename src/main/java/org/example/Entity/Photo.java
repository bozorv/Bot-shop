package org.example.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private String id;
    private String fileId;
    private String caption;
    private String productCode;

}
