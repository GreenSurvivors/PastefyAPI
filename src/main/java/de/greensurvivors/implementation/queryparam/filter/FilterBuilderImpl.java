package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.queryparam.FilterBuilder;
import de.greensurvivors.queryparam.FilterParameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public non-sealed class FilterBuilderImpl implements FilterBuilder, IFilterLike {
    private final @NotNull SequencedMap<@NotNull FilterConnection, @NotNull SequencedSet<@NotNull IFilterLike>> filters = new LinkedHashMap<>();

    public FilterBuilderImpl () {
        filters.put(FilterConnection.AND, new LinkedHashSet<>());
    }

    @Override
    public @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> build() {
        return this.build(List.of("filter"));
    }

    protected @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> build(final @NotNull List<@NotNull String> path) {
        if (filters.isEmpty() || filters.get(FilterConnection.AND).isEmpty()) {
            return Collections.emptySortedSet();
        } else if (filters.size() == 1) {// simple
            final @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> result = new LinkedHashSet<>();

            for (final @NotNull IFilterLike filterLike : filters.get(FilterConnection.AND)) {
                switch (filterLike) {
                    case FilterBuilderImpl nestedBuilder -> result.addAll(nestedBuilder.build(path));
                    case AProtoFilterImpl<?> protoFilter -> result.add(protoFilter.build(path));
                    //noinspection rawtypes - note: the compiler doesn't accept the correct 'AProtoFilterImpl<?>.FilterImpl filter -> result.add(filter);'
                    case AProtoFilterImpl.FilterImpl filter -> result.add(filter); // how???
                    default -> { // todo throw!!
                    }
                }
            }

            return result;
        } else { // complex
            final @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> result = new LinkedHashSet<>();

            for (final Map.@NotNull Entry<@NotNull FilterConnection, @NotNull SequencedSet<@NotNull IFilterLike>> entry : filters.sequencedEntrySet()) {
                final @NotNull List<@NotNull String> deeperPath = new ArrayList<>(path);
                deeperPath.add(entry.getKey().internalName); // todo is this correct?

                for (final @NotNull IFilterLike filterLike : entry.getValue()) {
                    switch (filterLike) {
                        case FilterBuilderImpl nestedBuilder -> result.addAll(nestedBuilder.build(deeperPath));
                        case AProtoFilterImpl<?> protoFilter -> result.add(protoFilter.build(deeperPath));
                        //noinspection rawtypes - note: the compiler doesn't accept the correct 'AProtoFilterImpl<?>.FilterImpl filter -> result.add(filter);'
                        case AProtoFilterImpl.FilterImpl filter -> result.add(filter); // how???
                        default -> { // todo throw!!
                        }
                    }
                }
            }

            return result;
        }
    }

    @Override
    public @NotNull FilterBuilderImpl pasteVisibility(final @NotNull Paste.@NotNull PasteVisibility visibility) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.PasteVisibilityProtoFilterImpl(visibility));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl isEncrypted(final boolean isEncrypted) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.IsEncryptedProtoFilterImpl(isEncrypted));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl pasteFolder(final @NotNull String folderId) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.FolderProtoFilterImpl(folderId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl userId(final @NotNull String userId) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.UserIdProtoFilterImpl(userId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl forkedFromPaste(final @NotNull String pasteId) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.PasteForkedFromProtoFilterImpl(pasteId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl pasteType(final @NotNull Paste.PasteType pasteType) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.PasteTypeProtoFilterImpl(pasteType));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl starredBy(final @NotNull String userId) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.StarredByProtoFilterImpl(userId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl folderParent(final @NotNull String folderId) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.FolderParentProtoFilterImpl(folderId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl accountStatus(final @NotNull AccountStaus accountStaus) {
        filters.get(FilterConnection.AND).add(new AProtoFilterImpl.AccountStatusProtoFilterImpl(accountStaus));

        return this;
    }


    @Override
    public @NotNull FilterBuilderImpl and(final @NotNull FilterBuilder other) {
        filters.computeIfAbsent(FilterConnection.AND, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl or(final @NotNull FilterBuilder other) {
        filters.computeIfAbsent(FilterConnection.OR, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    public enum FilterConnection {
        AND("$and"),
        OR("$OR"),
        EQUALS("$EQ"); // todo no idea what this does

        private final @NotNull String internalName;

        FilterConnection(final @NotNull String internalName) {
            this.internalName = internalName;
        }
    }
}
