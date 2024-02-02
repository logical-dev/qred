package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Percentage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.qred.loan.validation.SwedishOrganizationNumberValidation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @NotNull
    private UUID id;
    @NotNull
    private UUID applicationId;   // FK to application. Application <=> Offer has 1 to m relation

    // offerAmount can be less than 10000 or greator than 250000.
    // since user can either negotiate the amount or the term with manager.
    // Hence, NO @Min or @Max Validation
    @NotNull
    @JsonProperty
    private CurrencyAmount offerAmount;

    @NotEmpty
    @JsonProperty
    private String term;

    @Percentage
    @JsonProperty
    private BigDecimal interest;

    @NotNull
    @JsonProperty
    private CurrencyAmount totalCommission;

    @NotNull
    @JsonProperty
    private CurrencyAmount totalAmount;

    @JsonProperty
    private Instant offerExpiredAt;
}
