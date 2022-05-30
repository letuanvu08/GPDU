package hcmut.thesis.gpduserver.models.request.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateRandomRequest {
    int number;
}
