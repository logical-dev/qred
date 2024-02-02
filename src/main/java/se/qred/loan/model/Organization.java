package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shahbazhussain
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {


    @NotEmpty
    @JsonProperty
    private String organizationNumber;   // organizationNumber can become PK here since it will be unique
    @NotEmpty
    @JsonProperty
    private String name;
    @NotEmpty
    @JsonProperty
    private String type;
    //    @NotEmpty
    @JsonProperty
    private boolean active;
}
