package surf.deployers.iam;

interface IAMRoleConfig {

    String getRoleName();

    String getAssumeRolePolicyDocument();

    String getAccessPolicyName();

    String getAccessPolicyDocument();
}
