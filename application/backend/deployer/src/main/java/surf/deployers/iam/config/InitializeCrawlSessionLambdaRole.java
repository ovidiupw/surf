package surf.deployers.iam.config;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.exceptions.OperationFailedException;
import surf.utility.FileReader;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static surf.utility.ObjectConverter.toNormalizedLambdaRoleName;

public class InitializeCrawlSessionLambdaRole implements IAMRoleConfig {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeCrawlSessionLambdaRole.class);

    private static final String ROLE_NAME = toNormalizedLambdaRoleName("initialize_crawl_session");
    private static final String TRUST_POLICY_PATH
            = "src/main/resources/iam_policy_documents/lambda_trust_policy_assume_role.json";
    private static final String ACCESS_POLICY_NAME = ROLE_NAME;
    private static final String ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/initialize_crawl_session_lambda_access_policy.twig.json";

    private static final String TWIG_VAR_PASS_ROLE_RESOURCE_NAME = "PassRoleResourceName";
    private static final String TWIG_VAR_AWS_ACCOUNT_ID = "AWSAccountId";

    private final Role sfnInvokeLambdaRole;
    private final String awsAccountId;

    public InitializeCrawlSessionLambdaRole(@Nonnull final Role sfnInvokeLambdaRole, @Nonnull final String awsAccountId) {
        Preconditions.checkNotNull(sfnInvokeLambdaRole);
        Preconditions.checkNotNull(sfnInvokeLambdaRole.getRoleName());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(awsAccountId));
        this.sfnInvokeLambdaRole = sfnInvokeLambdaRole;
        this.awsAccountId = awsAccountId;
    }

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }

    @Override
    public String getTrustPolicyDocument() {
        return FileReader.readFile(TRUST_POLICY_PATH);
    }

    @Override
    public String getAccessPolicyName() {
        return ACCESS_POLICY_NAME;
    }

    @Override
    public String getAccessPolicyDocument() {
        final String accessPolicyTemplate = FileReader.readFile(ACCESS_POLICY_PATH);
        final JtwigTemplate template = JtwigTemplate.inlineTemplate(accessPolicyTemplate);
        final JtwigModel model = JtwigModel.newModel()
                .with(TWIG_VAR_PASS_ROLE_RESOURCE_NAME, sfnInvokeLambdaRole.getRoleName())
                .with(TWIG_VAR_AWS_ACCOUNT_ID, awsAccountId);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.render(model, outputStream);

        try {
            return outputStream.toString(Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unrecognized charset!", e);
            throw new OperationFailedException(e);
        }
    }
}
