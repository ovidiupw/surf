package surf.deployers.iam.roles;

import surf.deployers.iam.config.IAMRoleConfig;
import surf.utility.FileReader;

import static surf.utility.ObjectConverter.toNormalizedLambdaRoleName;

public class ApiGatewayInvokeLambdaRole implements IAMRoleConfig {

    private static final String ROLE_NAME = toNormalizedLambdaRoleName("api_gateway_invoke_lambda");
    private static final String TRUST_POLICY_PATH
            = "src/main/resources/iam_policy_documents/api_gateway_assume_role_policy.json";
    private static final String ACCESS_POLICY_NAME = ROLE_NAME;
    private static final String ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/invoke_lambda_access_policy.json";

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
        return FileReader.readFile(ACCESS_POLICY_PATH);
    }
}
