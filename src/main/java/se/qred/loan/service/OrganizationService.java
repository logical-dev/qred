package se.qred.loan.service;

import se.qred.loan.exception.ResourceAlreadyExistException;
import se.qred.loan.model.Organization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author shahbazhussain on 01/02/2024
 */
public class OrganizationService {

    private final Map<String, Organization> organizationInMemDb = new HashMap<>();

    public Organization createNewOrganization(final Organization organization) {

        if (organizationInMemDb.containsKey(organization.getOrganizationNumber())) {
            throw new ResourceAlreadyExistException("Failed to enter organization in the system. Organization already exist in system.");
        }

        organizationInMemDb.put(organization.getOrganizationNumber(), organization);

        return organization;
    }

    public Organization updateOrganization(final Organization organization) {

        if (!organizationInMemDb.containsKey(organization.getOrganizationNumber())) {
            throw new ResourceAlreadyExistException("No organization exist in the system");
        }

        organizationInMemDb.put(organization.getOrganizationNumber(), organization);

        return organization;
    }

    public Optional<Organization> getOrganization(final String organizationNumber) {
        return Optional.ofNullable(organizationInMemDb.get(organizationNumber));
    }


}
