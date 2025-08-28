package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.implementation.queryparam.AQueryParameter;
import de.greensurvivors.queryparam.FilterParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AProtoFilterImpl<T extends @NotNull Object> implements IFilterLike {
    protected final @NotNull String internalName;
    private final @NotNull T value;

    protected AProtoFilterImpl(final @NotNull String internalName, T value) {
        this.internalName = internalName;
        this.value = value;
    }

    public @NotNull T getValue() {
        return value;
    }

    public abstract @NotNull String getFormData();

    public @NotNull FilterImpl build (final @NotNull List<@NotNull String> path) {
        return new FilterImpl(joinPath(path, internalName), value);
    }

    @VisibleForTesting
    public static @NotNull String joinPath (final @NotNull List<@NotNull String> path, final @NotNull String name) {
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


    public non-sealed class FilterImpl extends AQueryParameter<T> implements FilterParameter<T>, IFilterLike {
        protected FilterImpl(final @NotNull String internalName, @NotNull T value) {
            super(internalName, value);
        }

        @Override
        public @NotNull String getFormData() {
            return AProtoFilterImpl.this.getFormData();
        }
    }

    // paste
    public static class PasteVisibilityProtoFilterImpl extends AProtoFilterImpl<Paste.@NotNull PasteVisibility> {
        public PasteVisibilityProtoFilterImpl(final @NotNull Paste.PasteVisibility visibility) {
            super("visibility", visibility);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name(); // todo ?
        }
    }

    // paste
    public static class IsEncryptedProtoFilterImpl extends AProtoFilterImpl<@NotNull Boolean> {
        public IsEncryptedProtoFilterImpl(final boolean isEncrypted) {
            super("encrypted", isEncrypted);
        }

        @Override
        public @NotNull String getFormData() {
            return String.valueOf(getValue());
        }
    }

    // paste
    public static class FolderProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {
        public FolderProtoFilterImpl(final @NotNull String folderId) {
            super("folder", folderId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste, folder
    public static class UserIdProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {
        public UserIdProtoFilterImpl(final @NotNull String userId) {
            super("userId", userId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste
    public static class PasteForkedFromProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {
        public PasteForkedFromProtoFilterImpl(final @NotNull String pasteId) {
            super("forkedFrom", pasteId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste
    public static class PasteTypeProtoFilterImpl extends AProtoFilterImpl<Paste.@NotNull PasteType> {
        public PasteTypeProtoFilterImpl(final @NotNull Paste.PasteType pasteType) {
            super("type", pasteType);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue().name();
        }
    }

    // paste
    public static class StarredByProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {
        public StarredByProtoFilterImpl(final @NotNull String userId) {
            super("starredBy", userId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // folder
    public static class FolderParentProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {
        public FolderParentProtoFilterImpl(final @NotNull String parentId) {
            super("parent", parentId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // user
    public static class AccountStatusProtoFilterImpl extends AProtoFilterImpl<@NotNull AccountStaus> {
        public AccountStatusProtoFilterImpl(final @NotNull AccountStaus accountStaus) {
            super("type", accountStaus);
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
