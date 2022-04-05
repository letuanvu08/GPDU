package hcmut.thesis.gpduserver.models.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
