package surf.deployers.iam;

import surf.utility.FileReader;

public class SurfApiUserIAMRoleConfig implements IAMRoleConfig {

    private static final String ROLE_NAME = "surf_api_user";
    private static final String ASSUME_ROLE_POLICY_PATH
            = "src/main/resources/iam_policy_documents/api_gateway_assume_role_policy.json";
    private static final String ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/hello_world_lambda_access_policy.json";

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }

    @Override
    public String getAssumeRolePolicyDocument() {
        return FileReader.readFile(ASSUME_ROLE_POLICY_PATH);
    }

    @Override
    public String getAccessPolicyName() {
        return null;
    }

    @Override
    public String getAccessPolicyDocument() {
        return null;
    }
}
