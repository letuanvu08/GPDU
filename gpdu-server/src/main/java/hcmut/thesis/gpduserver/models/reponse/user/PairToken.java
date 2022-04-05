package hcmut.thesis.gpduserver.models.reponse.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PairToken {
    private String token;
    private String refreshToken;
}
