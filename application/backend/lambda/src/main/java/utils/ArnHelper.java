package utils;

import javax.annotation.Nonnull;

public class ArnHelper {

    private ArnHelper() {
        throw new UnsupportedOperationException("This class is not instantiable!");
    }

    public static String getOwnerIdFromUserArn(@Nonnull final String userArn) {
        final String ownerId = extractOwnerIdPathPartFromArn(userArn);

        if (ownerId.contains("@")) {
            return ownerId.split("@")[0];
        }

        return ownerId;
    }

    public static String getAuthProviderFromUserArn(@Nonnull final String userArn) {
        final String ownerId = extractOwnerIdPathPartFromArn(userArn);

        if (ownerId.equals("root")) {
            return ownerId;
        }

        if (!ownerId.contains("@")) {
            throw new IllegalArgumentException("The ownerId did not contain any auth provider. You must separate " +
                    "the auth provider with '@' from the ownerId.");
        }

        return ownerId.split("@")[1];
    }

    private static String extractOwnerIdPathPartFromArn(@Nonnull String userArn) {
        if (!userArn.contains("/") && !userArn.contains(":")) {
            return userArn;
        }

        if (!userArn.contains("/")) {
            final String[] arnColonSeparatedParts = userArn.split(":");
            return arnColonSeparatedParts[arnColonSeparatedParts.length - 1];
        }

        final String[] arnSlashSeparatedParts = userArn.split("/");
        return arnSlashSeparatedParts[arnSlashSeparatedParts.length - 1];
    }
}
