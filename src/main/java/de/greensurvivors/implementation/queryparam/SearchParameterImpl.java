package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.admin.queryparam.AuthenticationProvider;
import de.greensurvivors.implementation.AdminSessionImpl;
import de.greensurvivors.queryparam.SearchParameter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public non-sealed class SearchParameterImpl extends AQueryParameter<@NotNull String> implements SearchParameter {
    public SearchParameterImpl(final @NotNull String value) {
        super("search", value);
    }

    @Override
    public @NotNull String getFormData() {
        return getValue();
    }

    // ADMIN
    public static class AccountStausSearchParameterImpl extends SearchParameterImpl {
        public AccountStausSearchParameterImpl(final @NotNull AccountStaus accountStaus) {
            super(accountStaus.name());
        }
    }

    // ADMIN
    public static class AuthenticationProviderSearchParameterImpl extends SearchParameterImpl {

        public AuthenticationProviderSearchParameterImpl(final @NotNull AuthenticationProvider authenticationProvider) {
            super(authenticationProvider.name());
        }
    }

    public static class CreatedAtSearchParameterImpl extends SearchParameterImpl {

        public CreatedAtSearchParameterImpl(final @NotNull Instant createdAtInstant) {
            super(AdminSessionImpl.DATE_TIME_FORMATTER.format(createdAtInstant));
        }
    }

    public static class PasteTypeSearchParameterImpl extends SearchParameterImpl {
        public PasteTypeSearchParameterImpl(final @NotNull Paste.PasteType type) {
            super(type.name());
        }
    }

    /* paste parameters

    @Column(size = 8)
    @Searchable
    @Filterable
    public String folder;
    @Column
    public Timestamp expireAt = null;
    @Column
    public Timestamp updatedAt;
    protected String cachedContents = null;
    @Column
    private int id;
    @Column(size = 8)
    @Searchable
    @Filterable
    private String key;
    @Column
    @Searchable
    private String title;
    @Column(size = 16777215)
    @Searchable
    private String content;
    @Column(size = 8)
    @Filterable
    private String userId;
    @Column(size = 8)
    @Filterable
    private String forkedFrom;
    @Column
    @Filterable
    private boolean encrypted = false;
    @Column
    @Filterable
    private Visibility visibility = Visibility.UNLISTED;
    @Column
    private StorageType storageType = StorageType.DATABASE;
    @Column
    private Integer version = 0;
    @Column
    private Boolean indexedInElastic = false;
    */

    /* folder parameters:

    @Column
    private int id;

    @Column(size = 8)
    private String key;

    @Column
    @Searchable
    private String name = "";

    @Column(size = 8)
    @Filterable
    public String userId;

    @Column
    @Searchable
    @Filterable
    private String parent;

    @Column
    public Timestamp updatedAt;
    */

    /* user parameters:


    @Column(size = 8, id = true)
    public String id;

    @Column
    @Searchable
    public String name;

    @Column(size = 33)
    @Filterable
    public String uniqueName;

    @Column(name = "email")
    @Filterable
    public String eMail;

    @Column
    public String avatar;

    @Column(size = 455)
    @Filterable
    public String authId;

    @Column
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;
    */
}
