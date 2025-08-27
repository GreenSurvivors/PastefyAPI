package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.implementation.queryparam.AQueryParameter;
import de.greensurvivors.queryparam.FilterParameter;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract non-sealed class AFilterImpl<T extends @NotNull Object> extends AQueryParameter<T> implements FilterParameter<T> {

    protected AFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String internalName, T value) {
        super(joinPath(path, internalName), value);
    }

    protected static String joinPath ( final @NotNull List<@NotNull String> path, final @NotNull String name) {
        StringBuilder builder = new StringBuilder(URLEncoder.encode(name, StandardCharsets.UTF_8));

        if (!path.isEmpty()) {
            // reverse pass through the path
            for (final @NotNull String pathPart : path.reversed()) {
                // pathPart[<builder contents until now>]
                builder.insert(0, '[').
                    insert(0, URLEncoder.encode(pathPart, StandardCharsets.UTF_8)).
                    append(']');
            }
        }

        return builder.toString();
    }

    // paste
    public static class PasteVisibilityFilterImpl extends AFilterImpl<Paste.@NotNull PasteVisibility> {
        public PasteVisibilityFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull Paste.PasteVisibility visibility) {
            super(path, "visibility", visibility);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name(); // todo ?
        }
    }

    // paste
    public static class IsEncryptedFilterImpl extends AFilterImpl<@NotNull Boolean> {
        public IsEncryptedFilterImpl(final @NotNull List<@NotNull String> path, final boolean isEncrypted) {
            super(path, "encrypted", isEncrypted);
        }

        @Override
        public @NotNull String getFormData() {
            return String.valueOf(getValue());
        }
    }

    // paste
    public static class FolderFilterImpl extends AFilterImpl<@NotNull String> {
        public FolderFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String folderId) {
            super(path, "folder", folderId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste, folder
    public static class UserIdFilterImpl extends AFilterImpl<@NotNull String> {
        public UserIdFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String userId) {
            super(path, "userId", userId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste
    public static class PasteForkedFromFilterImpl extends AFilterImpl<@NotNull String> {
        public PasteForkedFromFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String pasteId) {
            super(path, "forkedFrom", pasteId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste
    public static class PasteTypeFilterImpl extends AFilterImpl<Paste.@NotNull PasteType> {
        public PasteTypeFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull Paste.PasteType pasteType) {
            super(path, "type", pasteType);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name();
        }
    }

    // paste
    public static class StarredByFilterImpl extends AFilterImpl<@NotNull String> {
        public StarredByFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String userId) {
            super(path, "starredBy", userId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // folder
    public static class FolderParentFilterImpl extends AFilterImpl<@NotNull String> {
        public FolderParentFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull String parentId) {
            super(path, "parent", parentId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // user
    public static class AccountStatusFilterImpl extends AFilterImpl<@NotNull AccountStaus> {
        public AccountStatusFilterImpl(final @NotNull List<@NotNull String> path, final @NotNull AccountStaus accountStaus) {
            super(path, "type", accountStaus);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name();
        }
    }

    /* Paste columns:

    @Column
    public Timestamp expireAt = null;
    @Column
    @Searchable
    public Timestamp createdAt;
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
    @Searchable
    @Filterable
    private Type type = Type.PASTE;
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

    /*Folder columns:

    @Column
    private int id;

    @Column(size = 8)
    private String key;

    @Column
    @Searchable
    private String name = "";

    @Column
    @Searchable
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;
     */

    /* authkey

    @Column
    public int id;

    @Column(size = 60)
    private String key;

    @Column(size = 255)
    public String accessToken;

    @Column(size = 255)
    public String refreshToken;

    @Column(size = 8)
    public String userId;

    @Column
    public Type type = Type.USER;

    @Column
    private AbstractArray scopes;

    @Column
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;
    * */

    /* User:

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
    @Filterable
    @Searchable
    public AuthenticationProvider authProvider;

    @Column
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;
    */
}
