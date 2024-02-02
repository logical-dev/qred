package se.qred.loan.api;

import io.dropwizard.auth.Auth;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import se.qred.loan.model.Application;
import se.qred.loan.model.Contract;
import se.qred.loan.model.Offer;
import se.qred.loan.model.User;
import se.qred.loan.service.ApplicationService;
import se.qred.loan.service.ContractService;
import se.qred.loan.service.OfferService;

import java.util.UUID;

@Path("/api/v1/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final ContractService contractService;

    @POST
    @Path("/applications/apply")
    public Response createApplication(@Auth User user, @Valid Application application) {

        application.setUserId(user.getId());

        final Application newApplication = applicationService.createNewApplication(application);

        return Response.ok(newApplication).build();
    }


    @GET
    @Path("/applications/{applicationId}")
    public Response getApplication(@Auth User user, @PathParam("applicationId") UUID applicationId) {

        final Application requestedApplication = applicationService.getApplication(user.getId(), applicationId);
        return Response.ok(requestedApplication).build();
    }

    @GET
    @Path("/applications/applicationId/{applicationId}/offer")
    public Response viewOffer(@Auth User user, @PathParam("applicationId") UUID applicationId) {

        final Offer offerByUserAndApplicationId = offerService.getOfferByUserAndApplicationId(user.getId(), applicationId);
        return Response.ok(offerByUserAndApplicationId).build();
    }

    @PUT
    @Path("/applications/applicationId/{applicationId}/offerId/{offerId}/sign")
    public Response signOffer(@Auth User user,
                              @PathParam("applicationId") UUID applicationId,
                              @PathParam("offerId") UUID offerId) {

        final Contract contract = contractService.signOffer(user.getId(), applicationId, offerId);

        return Response.ok(contract).build();
    }

    @GET
    @Path("/applications/applicationId/{applicationId}/offerId/{offerId}/contractId/{contractId}")
    public Response getContract(@Auth User user,
                                @PathParam("applicationId") UUID applicationId,
                                @PathParam("offerId") UUID offerId,
                                @PathParam("contractId") UUID contractId) {

        final Contract contract = contractService.getContract(user.getId(), applicationId, offerId, contractId);
        return Response.ok(contract).build();
    }

    @GET
    @Path("/ping")
    public Response heartBeat(@Auth User user) {
        return Response.ok("Pong").build();
    }
}
