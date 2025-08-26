package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public class SortParameter extends AQueryParameter<@NotNull Map<SortParameter.SortType, @NotNull Boolean>> {
    protected SortParameter(final @NotNull Map<@NotNull SortType, Boolean> value) {
        super("sort", value);
    }

    // return an unmodifiable copy
    @Override
    public @NotNull @Unmodifiable Map<@NotNull SortType, Boolean> getValue() {
        return Map.copyOf(super.getValue());
    }

    @Override
    public @NotNull String getFormData() throws IllegalArgumentException {
        if (super.getValue().isEmpty()) {
            throw new IllegalArgumentException("Sort types are missing!");
        }

        return String.join(",", super.getValue().entrySet().stream().map(sortTypeEntry ->
                !sortTypeEntry.getValue() ? sortTypeEntry.getKey().internalName + "+" : sortTypeEntry.getKey().internalName
            ).toArray(String[]::new));
    }

    public abstract static class SortType {// todo move to api
        private final @NotNull String internalName;

        protected SortType(final @NotNull String internalName) {
            this.internalName = internalName;
        }

        public static SortType TIME_CREATED = new SortType ("createdAt") {}; // paste, folder, authkey, user
        public static SortType TIME_EXPIRES = new SortType ("expireAt") {}; // paste
        public static SortType POPULARITY = new SortType("engagementScore") {}; // paste
        public static SortType TITLE = new SortType("title") {}; // paste
        public static SortType DISPLAY_NAME = new SortType("uniqueName") {}; // user
        public static SortType ID_USER = new SortType("userId") {}; // paste, folder, authkey
        public static SortType ID_FORKED_FROM = new SortType("forkedFrom") {}; // paste
        public static SortType ENCRYPTED = new SortType("encrypted") {}; // paste
        public static SortType PASTE_TYPE = new SortType("type") {}; // paste // todo
        public static SortType VISIBILITY = new SortType("visibility") {}; // paste // todo
        public static SortType FOLDER = new SortType("folder") {}; // paste
        public static SortType PARENT_FOLDER = new SortType("parent") {}; // folder
        public static SortType TIME_UPDATED = new SortType("updatedAt") {}; // folder, authkey, user
    }

    // Note: sort just sorts by internal used column.
    // I tried to not expose any unnecessary internal data
    // but all currently missing ones are listed below.
    // attention: there be dragons!

    /* Paste columns:

    protected String cachedContents = null;
    @Column
    private int id;
    @Column(size = 8)
    @Searchable
    @Filterable
    private String key;
    @Column(size = 16777215)
    @Searchable
    private String content;
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
     */

    /* authkey columns:

    @Column
    public int id;

    @Column(size = 60)
    private String key;

    @Column(size = 255)
    public String accessToken;

    @Column(size = 255)
    public String refreshToken;

    @Column
    public Type type = Type.USER;

    @Column
    private AbstractArray scopes;
    */

    /* User columns:

    @Column(size = 8, id = true)
    public String id;

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
    @Filterable
    @Searchable
    public Type type = Type.USER;
    */
}
