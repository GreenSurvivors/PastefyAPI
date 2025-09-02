package de.greensurvivors.admin.queryparam;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.implementation.queryparam.SearchParameterImpl;
import de.greensurvivors.implementation.queryparam.filter.AAdminProtoFilterImpl;
import de.greensurvivors.implementation.queryparam.filter.AdminFilterBuilderImpl;
import de.greensurvivors.queryparam.QueryParameter;
import de.greensurvivors.queryparam.SearchParameter;
import org.jetbrains.annotations.NotNull;

public sealed interface AdminQueryParameter<T extends @NotNull Object> extends QueryParameter<T> permits AAdminProtoFilterImpl.AdminFilterImpl {
    static @NotNull AdminFilterBuilder newAdminFilterBuilder() {
        return new AdminFilterBuilderImpl();
    }

    /// note: all searchable aspects (like paste content, enclosing folder, etc.) will get searched at once.
    /// this is purely a convenient method to provide the correct formatting
    static @NotNull SearchParameter newSearchParameter(final @NotNull AccountStaus accountStaus) {
        return new SearchParameterImpl.AccountStausSearchParameterImpl(accountStaus);
    }

    /// note: all searchable aspects (like paste content, enclosing folder, etc.) will get searched at once.
    /// this is purely a convenient method to provide the correct formatting
    static @NotNull SearchParameter newSearchParameter(final @NotNull AuthenticationProvider authenticationProvider) {
        return new SearchParameterImpl.AuthenticationProviderSearchParameterImpl(authenticationProvider);
    }
}
