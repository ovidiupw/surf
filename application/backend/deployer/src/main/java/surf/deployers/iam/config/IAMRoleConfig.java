package surf.deployers.iam.config;

public interface IAMRoleConfig {

    String getRoleName();

    String getTrustPolicyDocument();

    String getAccessPolicyName();

    String getAccessPolicyDocument();
}
