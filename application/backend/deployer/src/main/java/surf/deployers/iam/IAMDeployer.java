package surf.deployers.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.ExitCode;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.deployment.IAMRoles;

import javax.annotation.Nonnull;

public class IAMDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(IAMDeployer.class);
    private static final String DEPLOYER_NAME = "IAMDeployer";

    private final DeployerConfiguration deployerConfiguration;

    @Inject
    public IAMDeployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull Context context) {
        Preconditions.checkNotNull(context);
        final AmazonIdentityManagement iamClient = initializeIAMClient();

        final Role helloWorldLambdaRole = createHelloWorldLambdaIAMRole(iamClient);
        /* Add more IAM roles here when needed */

        LOG.info("Updating context with the created/existing lambda roles.");
        final IAMRoles IAMRoles = new IAMRoles.Builder()
                .withHelloWorldLambdaRole(helloWorldLambdaRole)
                .build();
        context.setIAMRoles(IAMRoles);

        return context;
    }

    private Role createHelloWorldLambdaIAMRole(final AmazonIdentityManagement iamClient) {
        final HelloWorldLambdaIAMRoleConfig helloWorldLambdaIAMRoleConfig = new HelloWorldLambdaIAMRoleConfig();
        final Role helloWorldLambdaRole = createIAMRole(iamClient, helloWorldLambdaIAMRoleConfig);
        putAccessPolicyOnRole(iamClient, helloWorldLambdaIAMRoleConfig);
        return helloWorldLambdaRole;
    }

    private PutRolePolicyResult putAccessPolicyOnRole(
            final AmazonIdentityManagement iamClient,final IAMRoleConfig helloWorldLambdaIAMRoleConfig) {
        try {
            LOG.info("Putting an access policy on the role with name '{}'", helloWorldLambdaIAMRoleConfig.getRoleName());
            return iamClient.putRolePolicy(new PutRolePolicyRequest()
                    .withPolicyName(helloWorldLambdaIAMRoleConfig.getAccessPolicyName())
                    .withRoleName(helloWorldLambdaIAMRoleConfig.getRoleName())
                    .withPolicyDocument(helloWorldLambdaIAMRoleConfig.getAccessPolicyDocument()));
        } catch (LimitExceededException
                | MalformedPolicyDocumentException
                | NoSuchEntityException
                | ServiceFailureException e) {
            LOG.error("Exception while trying to put access policy on an IAM role! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private Role createIAMRole(final AmazonIdentityManagement iamClient, final IAMRoleConfig iamRoleConfig) {
        try {
            LOG.info("Trying to create IAM role with name '{}'...", iamRoleConfig.getRoleName());

            final CreateRoleResult createRoleResult = iamClient.createRole(new CreateRoleRequest()
                    .withRoleName(iamRoleConfig.getRoleName())
                    .withAssumeRolePolicyDocument(iamRoleConfig.getAssumeRolePolicyDocument()));
            return createRoleResult.getRole();

        } catch (EntityAlreadyExistsException ignored) {
            LOG.warn("IAM role with name '{}' already exists! Trying to use it's ARN instead.",
                    iamRoleConfig.getRoleName());
            return getIAMRole(iamClient, iamRoleConfig);
        } catch (LimitExceededException
                | ServiceFailureException
                | InvalidInputException
                | MalformedPolicyDocumentException e) {
            LOG.error("Exception while trying to create IAM role! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private Role getIAMRole(final AmazonIdentityManagement iamClient, final IAMRoleConfig iamRoleConfig) {
        try {
            LOG.info("Trying to get IAM role with name '{}'...", iamRoleConfig.getRoleName());
            GetRoleResult getRoleResult = iamClient.getRole(new GetRoleRequest()
                    .withRoleName(iamRoleConfig.getRoleName()));
            return getRoleResult.getRole();

        } catch (NoSuchEntityException ignored) {
            LOG.warn("IAM role with name '{}' could not be found!", iamRoleConfig.getRoleName());
            System.exit(ExitCode.Error.getCode());
        } catch (ServiceFailureException e) {
            LOG.error("Exception while trying to get IAM role! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private AmazonIdentityManagement initializeIAMClient() {
        LOG.info("Initializing IAM client with region set to '{}'...", deployerConfiguration.getRegion().getName());
        return AmazonIdentityManagementClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getRegion())
                .build();
    }
}
