package se.qred.loan.service;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import se.qred.loan.exception.ResourceNotFoundException;
import se.qred.loan.exception.ValidationException;
import se.qred.loan.model.Application;
import se.qred.loan.model.ApplicationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author shahbazhussain
 */
@Slf4j
public class ApplicationService {
    private static final BigDecimal MIN_AMOUNT = new BigDecimal(10000);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal(250000);

    // When application is PENDING when waiting for manager review, PENDING when user got offer
    // otherwise CANCELLED after seven days or SIGNED within seven days.
    private static final List<ApplicationStatus> ONGOING_APPLICATIONS = List.of(ApplicationStatus.PENDING, ApplicationStatus.PROCESSED);
    private final Map<UUID, Application> applicationInMemDb = new HashMap<>();

    public Application createNewApplication(final Application application) {

        final Optional<Application> duplicateApplication = isDuplicateApplication(application);
        if (duplicateApplication.isPresent()) {
            log.info("Duplicate application={} detected. Hence ignored.", duplicateApplication.get().getId());
            throw new ValidationException("Similar application with same amount is on going. Please wait for its decision. ");
        }

        final BigDecimal amount = application.getAmountApplied().getAmount();
        if (amount.compareTo(MIN_AMOUNT) < 0 || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new ValidationException("loan amountApplied must be in range. minimum: 10000, maximum:250000");
        }

        final UUID applicationUid = UUID.randomUUID();
        application.setId(applicationUid);
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedAt(Instant.now());

        applicationInMemDb.put(applicationUid, application);
        log.info("New Loan Application has been registered. applicationId={} ", application.getId());
        return application;
    }

    public Application getApplication(String userId, UUID applicationId) {

        return Optional.ofNullable(applicationInMemDb.get(applicationId))
                .filter(application -> application.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("No application found with applicationId=" + applicationId));
    }


    public Application getApplication(UUID applicationId) {

        return Optional.ofNullable(applicationInMemDb.get(applicationId))
                .orElseThrow(() -> new ResourceNotFoundException("No application found with applicationId=" + applicationId));
    }

    public Optional<Application> changeApplicationStatus(UUID applicationId, String status) {

        final ApplicationStatus newApplicationStatus = ApplicationStatus.valueOf(status);

        return Optional.ofNullable(applicationInMemDb.get(applicationId))
                .map(application -> {
                    application.setStatus(newApplicationStatus);
                    return application;
                }).stream().findFirst();
    }

    public List<Application> getAllApplication() {
        return new ArrayList<>(applicationInMemDb.values());

    }


    // No two application with same amount can be run in parallel for same company.
    private Optional<Application> isDuplicateApplication(Application application) {
        return applicationInMemDb.values()
                .stream()
                .filter(app -> Objects.equal(app.getOrganizationNumber(), application.getOrganizationNumber()))
                .filter(app -> ONGOING_APPLICATIONS.contains(app.getStatus()))
                .filter(app -> app.getAmountApplied() == application.getAmountApplied())
                .findFirst();
    }


}
