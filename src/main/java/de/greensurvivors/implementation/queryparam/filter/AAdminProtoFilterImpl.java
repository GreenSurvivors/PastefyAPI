package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.admin.queryparam.AdminQueryParameter;
import de.greensurvivors.admin.queryparam.AuthenticationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// note AProtoFilter got to messy holding all the different subclasses
public abstract class AAdminProtoFilterImpl<T extends @NotNull Object> extends AProtoFilterImpl<T> {

    protected AAdminProtoFilterImpl(final @NotNull String internalName, final T value) {
        super(internalName, value);
    }

    @Override
    public @NotNull AdminFilterImpl build(final @NotNull List<@NotNull String> path) {
        return new AdminFilterImpl(joinPath(path, internalName), value);
    }

    public non-sealed class AdminFilterImpl extends FilterImpl implements AdminQueryParameter<T> {
        protected AdminFilterImpl(final @NotNull String internalName, @NotNull T value) {
            super(internalName, value);
        }
    }

    // user
    public static class AccountStatusProtoFilterImpl extends AAdminProtoFilterImpl<@NotNull AccountStaus> {
        public AccountStatusProtoFilterImpl(final @NotNull AccountStaus accountStaus) {
            super("type", accountStaus);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name();
        }
    }

    // user
    public static class AuthenticationProviderProtoFilterImpl extends AAdminProtoFilterImpl<@NotNull AuthenticationProvider> {

        protected AuthenticationProviderProtoFilterImpl(final @NotNull AuthenticationProvider authenticationProvider) {
            super("authProvider", authenticationProvider);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name();
        }
    }

    // user
    public static class AuthIdProtoFilterImpl extends AAdminProtoFilterImpl<@NotNull String> {

        protected AuthIdProtoFilterImpl(final @NotNull String authId) {
            super("authId", authId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // user
    public static class EMailProtoFilterImpl extends AAdminProtoFilterImpl<@NotNull String> {

        protected EMailProtoFilterImpl(final @NotNull String emailAddress) {
            super("eMail", emailAddress);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // user
    public static class UniqueNameProtoFilterImpl extends AAdminProtoFilterImpl<@NotNull String> {

        protected UniqueNameProtoFilterImpl(final @NotNull String uniqueName) {
            super("uniqueName", uniqueName);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }
}
