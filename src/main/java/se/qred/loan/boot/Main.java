package se.qred.loan.boot;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.ws.rs.client.Client;
import se.qred.loan.api.ApplicationController;
import se.qred.loan.api.ApplicationManagerController;
import se.qred.loan.auth.SimpleAuthenticator;
import se.qred.loan.db.UserRepository;
import se.qred.loan.model.User;
import se.qred.loan.service.ApplicationService;
import se.qred.loan.service.ContractService;
import se.qred.loan.service.OfferService;
import se.qred.loan.service.OrganizationService;
import se.qred.loan.validation.validators.SwedishOrganizationNumberValidator;


public class Main extends Application<MainConfiguration> {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "main";
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(MainConfiguration configuration, Environment environment) {
        UserRepository userRepository = new UserRepository();
        userRepository.addDummyData();
        final ApplicationService applicationService = new ApplicationService();
        final OfferService offerService = new OfferService(applicationService);
        final OrganizationService organizationService = new OrganizationService();
        final ContractService contractService = new ContractService(applicationService, offerService, organizationService);
        environment.jersey().register(new ApplicationController(applicationService,offerService,contractService));
        environment.jersey().register(new ApplicationManagerController(applicationService, offerService, contractService));
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new SimpleAuthenticator(userRepository))
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());
        environment.jersey().register(new SwedishOrganizationNumberValidator(client, configuration, organizationService));


    }


}
