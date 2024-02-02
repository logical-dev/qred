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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import se.qred.loan.model.Application;
import se.qred.loan.model.Offer;
import se.qred.loan.model.User;
import se.qred.loan.service.ApplicationService;
import se.qred.loan.service.ContractService;
import se.qred.loan.service.OfferService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/api/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ApplicationManagerController {
    private final ApplicationService applicationService;
    private final OfferService offerService;

    private final ContractService contractService;


    @GET
    @Path("/applications")
    public Response getApplications(@Auth User user) {
        final List<Application> latestUserApplication = applicationService.getAllApplication();
        return Response.ok(latestUserApplication).build();
    }

    @POST
    @Path("/applications/applicationId/{applicationId}/offer")
    public Response createOffer(@Auth User user,
                                @PathParam("applicationId") UUID applicationId) {

        final Offer newOffer = offerService.createNewOffer(applicationId);
        return Response.ok(newOffer).build();
    }

    @GET
    @Path("/applications/offers")
    public Response getAllOffers(@Auth User user) {

        final List<Offer> allOffers = offerService.getAllOffers();
        return Response.ok(allOffers).build();
    }

    @GET
    @Path("/applications/applicationId/{applicationId}/offer")
    public Response getAllOffersForApplicationId(@Auth User user,
                                                 @PathParam("applicationId") UUID applicationId) {

        final List<Offer> applicationAllOffers = offerService.getAllOffersByApplicationId(applicationId);
        return Response.ok(applicationAllOffers).build();
    }


    //Manager must have opportunity to terminate the process anytime.
    @PUT
    @Path("/applications/applicationId/{applicationId}/change-status-to")
    public Response updateApplicationStatus(@Auth User user,
                                            @PathParam("applicationId") UUID applicationId,
                                            @QueryParam("status") String status) {

        final Optional<Application> application = applicationService.changeApplicationStatus(applicationId, status);
        return Response.ok(application).build();
    }

    @PUT
    @Path("/applications/applicationId/{applicationId}/offerId/{offerId}/review")
    public Response updateOffer(@Auth User user,
                                @PathParam("applicationId") UUID applicationId,
                                @PathParam("offerId") UUID offerId,
                                @Valid Offer offer) {

        final Offer updatedOffer = offerService.reviewOffer(applicationId, offerId, offer);

        return Response.ok(updatedOffer).build();
    }


    @GET
    @Path("/ping")
    public Response heartBeat(@Auth User user) {
        return Response.ok("Pong").build();
    }
}
