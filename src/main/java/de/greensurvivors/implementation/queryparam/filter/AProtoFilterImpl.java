package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.Paste;
import de.greensurvivors.implementation.queryparam.AQueryParameter;
import de.greensurvivors.queryparam.FilterParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AProtoFilterImpl<T extends @NotNull Object> implements IFilterLike {
    protected final @NotNull String internalName;
    protected final @NotNull T value;

    protected AProtoFilterImpl(final @NotNull String internalName, T value) {
        this.internalName = internalName;
        this.value = value;
    }

    public @NotNull T getValue() {
        return value;
    }

    public abstract @NotNull String getFormData();

    public @NotNull FilterImpl build(final @NotNull List<@NotNull String> path) {
        return new FilterImpl(joinPath(path, internalName), value);
    }

    @VisibleForTesting
    public static @NotNull String joinPath(final @NotNull List<@NotNull String> path, final @NotNull String name) {
        final @NotNull List<@NotNull String> extendedPath = new ArrayList<>(path.size() + 1);
        extendedPath.addAll(path);
        extendedPath.add(name);

        final @NotNull Iterator<@NotNull String> pathIterator = extendedPath.iterator();
        final @NotNull StringBuilder builder = new StringBuilder(pathIterator.next());

        while (pathIterator.hasNext()) {
            builder.append('[').append(pathIterator.next()).append(']');
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
            return getValue().name();
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

    // paste, folder, star
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

    // paste
    public static class ForkedFromPasteIdProtoFilterImpl extends AProtoFilterImpl<@NotNull String> {

        protected ForkedFromPasteIdProtoFilterImpl(final @NotNull String forkedFromPasteId) {
            super("forkedFrom", forkedFromPasteId);
        }

        @Override
        public @NotNull String getFormData() {
            return getValue();
        }
    }

    // paste
    public static class EncryptedProtoFilterImpl extends AProtoFilterImpl<@NotNull Boolean> {

        protected EncryptedProtoFilterImpl(final boolean value) {
            super("encrypted", value);
        }

        @Override
        public @NotNull String getFormData() {
            return String.valueOf(getValue());
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

    /* Paste columns:

    @Column
    public Timestamp expireAt = null;
    @Column
    @Searchable
    public Timestamp createdAt; // todo this is used in backend but also not Marked as filterable. Does it work? And if yes the annotations are broken.
    @Column
    public Timestamp updatedAt;
    protected String cachedContents = null;
    @Column
    private int id;
    @Column(size = 8)
    @Searchable
    @Filterable
    private String key; // why da heck would you filter for this instead of search??; maybe useful for filtering stars and Tags, but you can't get the stars directly from web api as far as I know. And filtering a tag for a specific paste id seams backwards; Not implementing this!
    @Column
    @Searchable
    private String title;
    @Column(size = 16777215)
    @Searchable
    private String content;
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

    @Column
    public String avatar;

    @Column
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;
    */
}
