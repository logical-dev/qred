package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.qred.loan.validation.SwedishOrganizationNumberValidation;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    private UUID id;

    @NotNull
    @JsonProperty
    private CurrencyAmount amountApplied;

    @NotEmpty
    @Email
    @JsonProperty
    private String email;

    @NotEmpty
    @JsonProperty
    private String phoneNumber;

    @NotEmpty
    @JsonProperty
    @SwedishOrganizationNumberValidation
    private String organizationNumber;

    @JsonProperty
    private ApplicationStatus status;

    @JsonProperty
    private Instant createdAt;

    @JsonProperty
    private String userId;

}
