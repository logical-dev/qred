package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Percentage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    private UUID id;
    @NotNull
    private UUID offerId;   // FK to application. Application <=> Offer has 1 to m relation

    @NotEmpty
    @JsonProperty
    private String organizationNumber;

    @NotEmpty
    @JsonProperty
    private String name;

    @NotEmpty
    @JsonProperty
    private String type;

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
    private Instant signedAt;
}
