package surf.deployers.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.deployment.IAMRoles;
import surf.exceptions.OperationFailedException;

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
    public Context deploy(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        final AmazonIdentityManagement iamClient = initializeIAMClient();

        final Role facebookWebIdentityBasicRole
                = createRoleWithConfig(iamClient, new FacebookWebIdentityBasicRoleConfig());
        final Role helloWorldLambdaRole
                = createRoleWithConfig(iamClient, new HelloWorldLambdaIAMRoleConfig());
        final Role apiGatewayPushToCloudWatchLogsRole
                = createRoleWithConfig(iamClient, new ApiGatewayPushToCloudWatchLogsRoleConfig());

        LOG.info("Updating context with the created/existing lambda roles.");
        final IAMRoles IAMRoles = new IAMRoles.Builder()
                .withHelloWorldLambdaRole(helloWorldLambdaRole)
                .withApiGatewayPushToCloudWatchLogsRole(apiGatewayPushToCloudWatchLogsRole)
                .withFacebookWebIdentityBasicRole(facebookWebIdentityBasicRole)
                .build();
        context.setIAMRoles(IAMRoles);

        return context;
    }

    private AmazonIdentityManagement initializeIAMClient() {
        LOG.info("Initializing IAM client with region set to '{}'...", deployerConfiguration.getAwsClientRegion().getName());
        return AmazonIdentityManagementClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }

    private Role createRoleWithConfig(
            @Nonnull final AmazonIdentityManagement iamClient,
            @Nonnull final IAMRoleConfig roleConfig) {

        final Role role = createIAMRole(iamClient, roleConfig);
        putAccessPolicyOnRole(iamClient, roleConfig);
        return role;
    }

    private Role createIAMRole(
            @Nonnull final AmazonIdentityManagement iamClient,
            @Nonnull final IAMRoleConfig iamRoleConfig) {
        final String roleName = iamRoleConfig.getRoleName();
        final String assumeRolePolicyDocument = iamRoleConfig.getAssumeRolePolicyDocument();

        try {
            LOG.info("Trying to create IAM role with name='{}' and assumeRolePolicyDocument='{}'",
                    roleName,
                    assumeRolePolicyDocument);

            final CreateRoleResult createRoleResult = iamClient.createRole(new CreateRoleRequest()
                    .withRoleName(roleName)
                    .withAssumeRolePolicyDocument(assumeRolePolicyDocument));

            LOG.info("Successfully created IAM role with name='{}'!", roleName);

            return createRoleResult.getRole();

        } catch (EntityAlreadyExistsException ignored) {
            LOG.warn("!!! IAM role with name '{}' already exists! Trying to use it's ARN instead !!!", roleName);
            return getIAMRole(iamClient, iamRoleConfig);
        } catch (LimitExceededException
                | ServiceFailureException
                | InvalidInputException
                | MalformedPolicyDocumentException e) {
            LOG.error("Exception while trying to create IAM role! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private Role getIAMRole(
            @Nonnull final AmazonIdentityManagement iamClient,
            @Nonnull final IAMRoleConfig iamRoleConfig) {
        final String roleName = iamRoleConfig.getRoleName();

        try {
            LOG.info("Trying to get IAM role with name='{}'...", roleName);
            final GetRoleResult getRoleResult = iamClient.getRole(new GetRoleRequest()
                    .withRoleName(roleName));

            LOG.info("Successfully found IAM role with name='{}'", roleName);
            return getRoleResult.getRole();

        } catch (NoSuchEntityException e) {
            LOG.warn("IAM role with name '{}' could not be found!", roleName);
            throw new OperationFailedException(e);
        } catch (ServiceFailureException e) {
            LOG.error("Exception while trying to get IAM role! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private void putAccessPolicyOnRole(
            @Nonnull final AmazonIdentityManagement iamClient,
            @Nonnull final IAMRoleConfig helloWorldLambdaIAMRoleConfig) {

        final String accessPolicyName = helloWorldLambdaIAMRoleConfig.getAccessPolicyName();
        final String accessPolicyDocument = helloWorldLambdaIAMRoleConfig.getAccessPolicyDocument();
        final String roleName = helloWorldLambdaIAMRoleConfig.getRoleName();

        try {
            LOG.info("Putting an access policy with policyName='{}' and policyDocument='{}' on the role with name '{}'",
                    accessPolicyName,
                    accessPolicyDocument,
                    roleName);

            iamClient.putRolePolicy(new PutRolePolicyRequest()
                    .withRoleName(roleName)
                    .withPolicyName(accessPolicyName)
                    .withPolicyDocument(accessPolicyDocument));

            LOG.info("Successfully put access policy on the role with name='{}'", roleName);

        } catch (LimitExceededException
                | MalformedPolicyDocumentException
                | NoSuchEntityException
                | ServiceFailureException e) {
            LOG.error("Exception while trying to put access policy on an IAM role! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }
}
