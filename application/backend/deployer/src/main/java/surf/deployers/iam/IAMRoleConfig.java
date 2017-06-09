package surf.deployers.iam;

interface IAMRoleConfig {

    String getRoleName();

    String getTrustPolicyDocument();

    String getAccessPolicyName();

    String getAccessPolicyDocument();
}
