package se.qred.loan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.qred.loan.exception.ResourceNotFoundException;
import se.qred.loan.model.Application;
import se.qred.loan.model.CurrencyAmount;
import se.qred.loan.model.Offer;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author shahbazhussain
 */
@Slf4j
@RequiredArgsConstructor
public class OfferService {
    private static final BigDecimal MIN_AMOUNT = new BigDecimal(10000);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal(250000);
    private static final int OFFER_EXPIRED_DAYS = 7;
    private final Map<UUID, Offer> offerInMemDb = new HashMap<>();
    private final ApplicationService applicationService;

    public Offer createNewOffer(UUID applicationId) {

        final Application existingApplication = applicationService.getApplication(applicationId);

        final UUID offerUid = UUID.randomUUID();
        final BigDecimal applicationAskedAmount = existingApplication.getAmountApplied().getAmount();
        final BigDecimal amountOffer = getAmountOffer(applicationAskedAmount);
        final BigDecimal interestRate = getInterestRate(applicationAskedAmount);
        final BigDecimal totalCommission = applicationAskedAmount.multiply(interestRate);
        final BigDecimal totalAmount = applicationAskedAmount.add(totalCommission);
        final Currency applicationCurrency = existingApplication.getAmountApplied().getCurrency();

        final Offer newOffer = Offer.builder()
                .id(offerUid)
                .applicationId(applicationId)
                .offerAmount(CurrencyAmount.builder().amount(amountOffer).currency(applicationCurrency).build())
                .term("Dumy-term-for-now.")
                .interest(interestRate)
                .totalCommission(CurrencyAmount.builder().amount(totalCommission).currency(applicationCurrency).build())
                .totalAmount(CurrencyAmount.builder().amount(totalAmount).currency(applicationCurrency).build())
                .offerExpiredAt(Instant.now().plus(OFFER_EXPIRED_DAYS, ChronoUnit.DAYS))
                .build();

        offerInMemDb.put(offerUid, newOffer);
        return newOffer;
    }

    public Offer reviewOffer(UUID applicationId, UUID offerId, Offer offerToUpdate) {

        final Application existingApplication = applicationService.getApplication(applicationId);
        final Offer existingOffer = getOffer(offerId);
        boolean changeDetected = false;

        // It means, there is change in offerAmount and we need to recalculate interest, commission,totalCommission,totalAmount
        if (!offerToUpdate.getOfferAmount().getAmount().equals(existingOffer.getOfferAmount().getAmount())) {
            final BigDecimal updatedAskedAmount = offerToUpdate.getOfferAmount().getAmount();
            final BigDecimal amountOffer = getAmountOffer(updatedAskedAmount);
            final BigDecimal interestRate = getInterestRate(updatedAskedAmount);
            final BigDecimal totalCommission = updatedAskedAmount.multiply(interestRate);
            final BigDecimal totalAmount = updatedAskedAmount.add(totalCommission);
            final Currency applicationCurrency = existingApplication.getAmountApplied().getCurrency();

            existingOffer.getOfferAmount().setAmount(amountOffer);
            existingOffer.setInterest(interestRate);
            existingOffer.getTotalCommission().setAmount(totalCommission);
            existingOffer.getTotalAmount().setAmount(totalAmount);
            changeDetected = true;
        }
        if (!offerToUpdate.getTerm().equals(existingOffer.getTerm())) {
            existingOffer.setTerm(offerToUpdate.getTerm());
            changeDetected = true;
        }
        if (changeDetected) {
            existingOffer.setOfferExpiredAt(Instant.now().plus(7, ChronoUnit.DAYS));
        }

        final Offer updatedOffer = offerInMemDb.put(offerToUpdate.getId(), existingOffer);
        return updatedOffer;
    }


    public Offer getOffer(UUID offerId) {
        return Optional.ofNullable(offerInMemDb.get(offerId))
                .orElseThrow(() -> new ResourceNotFoundException("No Offer found with offerId=" + offerId));

    }

    public Offer getOfferByUserAndApplicationId(String userId, UUID applicationId) {
        final Application existingApplication = applicationService.getApplication(userId, applicationId);
        final List<Offer> allOffersByApplicationId = getAllOffersByApplicationId(existingApplication.getId());
        if (allOffersByApplicationId.size() != 0) {
            return allOffersByApplicationId.get(0);
        } else {
            throw new ResourceNotFoundException("No Offer found for applicationId=" + applicationId);
        }
    }


    public List<Offer> getAllOffers() {

        return new ArrayList<>(offerInMemDb.values());
    }

    public List<Offer> getAllOffersByApplicationId(UUID applicationId) {
        return offerInMemDb.values()
                .stream()
                .filter(offer -> offer.getApplicationId().equals(applicationId))
                .sorted(Comparator.comparing(Offer::getOfferExpiredAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns a rough offer amount based on the application amount.
     *
     * @param amount
     * @return
     */
    private BigDecimal getAmountOffer(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.9"));
//        return Double.toString(Integer.parseInt(amount) * 0.9);
    }

    /**
     * Returns a rough estimate of the interest rate
     *
     * @param amount
     * @return
     */
    private BigDecimal getInterestRate(BigDecimal amount) {
        return new BigDecimal("0.6")
                .multiply(new BigDecimal(1).subtract(amount.min(MAX_AMOUNT).abs().divide(MAX_AMOUNT)))
                .max(new BigDecimal("0.03"));

    }
}
