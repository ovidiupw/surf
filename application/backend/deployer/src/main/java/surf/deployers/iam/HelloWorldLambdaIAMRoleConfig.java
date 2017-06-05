package surf.deployers.iam;

import surf.utility.FileReader;

class HelloWorldLambdaIAMRoleConfig implements IAMRoleConfig {

    private static final String ROLE_NAME = "hello_world_lambda";
    private static final String LAMBDA_ASSUME_ROLE_POLICY_PATH
            = "src/main/resources/iam_policy_documents/lambda_trust_policy_assume_role.json";
    private static final String LAMBDA_ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/hello_world_lambda_access_policy.json";

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }

    @Override
    public String getAssumeRolePolicyDocument() {
        return FileReader.readFile(LAMBDA_ASSUME_ROLE_POLICY_PATH);
    }

    @Override
    public String getAccessPolicyName() {
        return ROLE_NAME;
    }

    @Override
    public String getAccessPolicyDocument() {
        return FileReader.readFile(LAMBDA_ACCESS_POLICY_PATH);
    }
}
