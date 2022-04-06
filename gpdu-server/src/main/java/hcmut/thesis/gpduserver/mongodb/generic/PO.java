package hcmut.thesis.gpduserver.mongodb.generic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.Builder.Default;

@Getter
@Setter
public abstract class PO {
    private Long createdTime;
    private Long updatedTime;

    @JsonIgnore
    @Default
    private Boolean deleted= false;

}
