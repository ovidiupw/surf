package surf.deployers.iam.config;

import surf.utility.FileReader;

import static surf.utility.ObjectConverter.toNormalizedLambdaRoleName;

public class ApiGatewayPushToCloudWatchLogsRoleConfig implements IAMRoleConfig {

    private static final String ROLE_NAME = toNormalizedLambdaRoleName("api_gateway_push_to_cloudwatch_logs");
    private static final String ACCESS_POLICY_NAME = "AmazonAPIGatewayPushToCloudWatchLogs";
    private static final String ACCESS_POLICY_PATH
            = "src/main/resources/iam_policy_documents/api_gateway_push_to_cloudwatch_logs_policy.json";
    private static final String ASSUME_ROLE_POLICY_PATH
            = "src/main/resources/iam_policy_documents/api_gateway_assume_role_policy.json";

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }

    @Override
    public String getTrustPolicyDocument() {
        return FileReader.readFile(ASSUME_ROLE_POLICY_PATH);
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
