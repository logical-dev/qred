package se.qred.loan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.qred.loan.exception.OperationFailureException;
import se.qred.loan.exception.ResourceNotFoundException;
import se.qred.loan.model.Application;
import se.qred.loan.model.ApplicationStatus;
import se.qred.loan.model.Contract;
import se.qred.loan.model.Offer;
import se.qred.loan.model.Organization;

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
@RequiredArgsConstructor
public class ContractService {
    private final Map<UUID, Contract> contractInMemDb = new HashMap<>();

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final OrganizationService organizationService;

    public Contract signOffer(String userId, UUID applicationId, UUID offerId) {
        final Application existingApplication = applicationService.getApplication(userId, applicationId);
        final Offer existingOffer = offerService.getOfferByUserAndApplicationId(userId, existingApplication.getId());
        final String organizationNumber = existingApplication.getOrganizationNumber();
        final Optional<Organization> organization = organizationService.getOrganization(existingApplication.getOrganizationNumber());

        if (organization.isEmpty()) {
            log.error("No organization exit for current applicationId={} ,offerId={},organizationNumber={} ", applicationId, offerId, organizationNumber);
            throw new OperationFailureException("Expected happened. Please check with team. ");
        }
        final UUID contractId = UUID.randomUUID();
        final Contract newContract = Contract.builder()
                .id(contractId)
                .offerId(offerId)
                .offerAmount(existingOffer.getOfferAmount())
                .organizationNumber(organization.get().getOrganizationNumber())
                .name(organization.get().getName())
                .type(organization.get().getType())
                .offerAmount(existingOffer.getOfferAmount())
                .term(existingOffer.getTerm())
                .interest(existingOffer.getInterest())
                .totalCommission(existingOffer.getTotalCommission())
                .totalAmount(existingOffer.getTotalAmount())
                .signedAt(Instant.now())
                .build();

        existingOffer.setOfferExpiredAt(Instant.now());
        applicationService.changeApplicationStatus(applicationId, ApplicationStatus.SIGNED.toString());
        contractInMemDb.put(contractId, newContract);
        log.info("New Contract has been signed. applicationId={} ,offerId={}, contractId={}", applicationId, offerId, newContract.getId());
        return newContract;
    }

    public Contract getContract(String userId, UUID applicationId, UUID offerId, UUID contractId) {
        offerService.getOfferByUserAndApplicationId(userId, applicationId); // Used as authentication to verify if user is eligible to access this contract

        return Optional.ofNullable(contractInMemDb.get(contractId))
                .orElseThrow(() -> new ResourceNotFoundException("No Contract found with contractId=" + contractId));

    }


    public List<Contract> getAllContracts() {

        return new ArrayList<>(contractInMemDb.values());

    }

}
