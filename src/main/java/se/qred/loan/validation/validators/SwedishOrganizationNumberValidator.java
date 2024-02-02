package se.qred.loan.validation.validators;


import io.dropwizard.core.Configuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.qred.loan.boot.MainConfiguration;
import se.qred.loan.exception.OperationFailureException;
import se.qred.loan.model.Organization;
import se.qred.loan.service.OrganizationService;
import se.qred.loan.validation.SwedishOrganizationNumberValidation;

@Slf4j
@RequiredArgsConstructor
public class SwedishOrganizationNumberValidator implements ConstraintValidator<SwedishOrganizationNumberValidation, String> {

    private final static String ALLABOLOG_VALIDATION_URI = "api/v1/company/validate";

    private final Client client;
    private final Configuration configuration;
    private final OrganizationService organizationService;

    @Override
    public void initialize(SwedishOrganizationNumberValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(String organizationNumber, ConstraintValidatorContext context) {

        final Response response = client.target(((MainConfiguration) configuration).getAllabolagUrl())
                .path(ALLABOLOG_VALIDATION_URI)
                .queryParam("organizationNumber", organizationNumber)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            // Now we know for sure, organization still exists in Swedish system at time of this fresh validation call.
            final Organization fetchedOrganization = response.readEntity(Organization.class);
            if (!fetchedOrganization.isActive()) {
                log.info("Organization with organizationNumber={} is registered but isn't active in Swedish System.", organizationNumber);
                return false;
            }
            ;

            // Only create new Organization if  Organization record didn't exist earlier.
            if (organizationService.getOrganization(organizationNumber).isEmpty()) {
                organizationService.createNewOrganization(fetchedOrganization);
                log.info("Organization with organizationNumber={} is persisted successfully.", organizationNumber);
            } else {
                log.info("Organization with organizationNumber={} already exists.", organizationNumber);
            }

            return true;
        } else if (response.getStatus() == 404) {
            log.info("Organization={} doesn't exit in Swedish System.", organizationNumber);
            return false;
        } else {
            log.error("Failed to fetch organization information for organizationNumber={} in downstream dependency.", organizationNumber);
            throw new OperationFailureException("Intermittent problem in the system. Please try later or contact bank.");
        }
    }
}


