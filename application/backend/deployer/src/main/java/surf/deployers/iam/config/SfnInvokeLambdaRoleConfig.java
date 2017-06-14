package surf.deployers.iam.config;

import surf.utility.FileReader;

public class SfnInvokeLambdaRoleConfig implements IAMRoleConfig {

    private static final String ROLE_NAME = "sfn_invoke_lambda";
    private static final String TRUST_POLICY_PATH
            = "src/main/resources/iam_policy_documents/sfn_invoke_lambda_trust_policy.json";
    private static final String ACCESS_POLICY_NAME = "sfn_invoke_lambda";
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
