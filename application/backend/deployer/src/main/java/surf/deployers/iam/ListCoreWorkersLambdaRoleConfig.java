package surf.deployers.iam;

import surf.utility.FileReader;

import static surf.utility.ObjectConverter.toNormalizedLambdaRoleName;

class ListCoreWorkersLambdaRoleConfig implements IAMRoleConfig {

    private static final String ROLE_NAME = toNormalizedLambdaRoleName("list_core_workers");
    private static final String TRUST_POLICY_PATH
            = "src/main/resources/iam_policy_documents/lambda_trust_policy_assume_role.json";
    private static final String ACCESS_POLICY_NAME = ROLE_NAME;
    private static final String ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/list_core_workers_lambda_access_policy.json";

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
