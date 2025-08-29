package de.greensurvivors.implementation.queryparam.filter;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.Paste;
import de.greensurvivors.exception.NestedFilterException;
import de.greensurvivors.exception.UnsupportedFilterException;
import de.greensurvivors.queryparam.FilterBuilder;
import de.greensurvivors.queryparam.FilterParameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// I really hate how we encode the filter into the url here.
// it isn't scalable, just the nested encoding takes away so much space;
// it also results hard to understand code and I can't do much about it...
// Maybe using the html body for a filter would be feasible since that is somewhat undefined, somewhat supported in the html specs,
// maybe ditching the GET request and instead fetching everything via POST would be the better alternative,
// maybe waiting for the QUERY request would be the right call here... I don't know.

// Just wanted to dokument how awful my solution of nesting builders to create the correct paths at the end is,
// but between everything I have tested so far it is my best way to bundle complex filters,
// while still keeping the api abstract from the implementation.
// If you, dear reader, have a bright idea, that does not involve chaining the web api of pastify, please let me know!
public non-sealed class FilterBuilderImpl implements FilterBuilder, IFilterLike {
    private final @NotNull SequencedMap<@NotNull FilterConnection, @NotNull SequencedSet<@NotNull IFilterLike>> filters = new LinkedHashMap<>();

    @Override
    public @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> build() throws UnsupportedFilterException  {
        return this.build(List.of("filter"));
    }

    protected @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> build(final @NotNull List<@NotNull String> path) throws UnsupportedFilterException {
        if (filters.isEmpty()) {
            return Collections.emptySortedSet();
        } else if (filters.size() == 1 && filters.get(FilterConnection.EQUALS) != null) {// simple
            final @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> result = new LinkedHashSet<>();

            for (final @NotNull IFilterLike filterLike : filters.get(FilterConnection.EQUALS)) {
                switch (filterLike) {
                    case FilterBuilderImpl nestedBuilder -> result.addAll(nestedBuilder.build(path));
                    case AProtoFilterImpl<?> protoFilter -> result.add(protoFilter.build(path));
                    //noinspection rawtypes - note: the compiler doesn't accept the correct 'AProtoFilterImpl<?>.FilterImpl filter -> result.add(filter);'
                    case AProtoFilterImpl.FilterImpl filter -> result.add(filter); // how???
                    default -> throw new  UnsupportedFilterException ("Encountered filterLike of type " + filterLike.getClass() + ", but I don't know how to build it!");
                }
            }

            return result;
        } else { // complex
            final @NotNull SequencedSet<@NotNull FilterParameter<? extends @NotNull Object>> result = new LinkedHashSet<>();

            for (final Map.@NotNull Entry<@NotNull FilterConnection, @NotNull SequencedSet<@NotNull IFilterLike>> entry : filters.sequencedEntrySet()) {
                final @NotNull List<@NotNull String> deeperPath = new ArrayList<>(path);
                deeperPath.add(entry.getKey().internalName);

                for (final @NotNull IFilterLike filterLike : entry.getValue()) {
                    switch (filterLike) {
                        case FilterBuilderImpl nestedBuilder -> result.addAll(nestedBuilder.build(deeperPath));
                        case AProtoFilterImpl<?> protoFilter -> result.add(protoFilter.build(deeperPath));
                        //noinspection rawtypes - note: the compiler doesn't accept the correct 'AProtoFilterImpl<?>.FilterImpl filter -> result.add(filter);'
                        case AProtoFilterImpl.FilterImpl filter -> result.add(filter); // how???
                        default -> throw new  UnsupportedFilterException ("Encountered filterLike of type " + filterLike.getClass() + ", but I don't know how to build it!");
                    }
                }
            }

            return result;
        }
    }

    @Override
    public @NotNull FilterBuilderImpl pasteVisibility(final @NotNull Paste.@NotNull PasteVisibility visibility) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.PasteVisibilityProtoFilterImpl(visibility));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl isEncrypted(final boolean isEncrypted) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.IsEncryptedProtoFilterImpl(isEncrypted));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl pasteFolder(final @NotNull String folderId) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.FolderProtoFilterImpl(folderId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl userId(final @NotNull String userId) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.UserIdProtoFilterImpl(userId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl forkedFromPaste(final @NotNull String pasteId) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.PasteForkedFromProtoFilterImpl(pasteId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl pasteType(final @NotNull Paste.PasteType pasteType) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.PasteTypeProtoFilterImpl(pasteType));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl starredBy(final @NotNull String userId) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.StarredByProtoFilterImpl(userId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl folderParent(final @NotNull String folderId) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.FolderParentProtoFilterImpl(folderId));

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl accountStatus(final @NotNull AccountStaus accountStaus) {
        filters.computeIfAbsent(FilterConnection.EQUALS, ignored -> new LinkedHashSet<>()).add(new AProtoFilterImpl.AccountStatusProtoFilterImpl(accountStaus));

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

    @Override
    public @NotNull FilterBuilderImpl not(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.NOT, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl isNull(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.IS_NULL, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl notNull(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.NOT_NULL, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl greaterThan(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.GREATER_THAN, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl greaterThenOrEquals(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.GREATER_THAN_EQUALS, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl lowerThan(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.LOWER_THAN, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    @Override
    public @NotNull FilterBuilderImpl lowerThenOrEquals(final @NotNull FilterBuilder other) throws NestedFilterException {
        validateAllTopLevel((FilterBuilderImpl)other);

        filters.computeIfAbsent(FilterConnection.LOWER_THAN_EQUALS, ignored -> new LinkedHashSet<>()).add((FilterBuilderImpl)other);

        return this;
    }

    protected void validateAllTopLevel(final @NotNull FilterBuilderImpl other) throws NestedFilterException {
        for (SequencedSet<IFilterLike> iFilterLikes : other.filters.values()) {
            for (IFilterLike filterLike : iFilterLikes) {
                if (filterLike instanceof FilterBuilder) {
                    throw new NestedFilterException("");
                }
            }
        }
    }

    protected enum FilterConnection {
        AND("$and"),
        OR("$OR"),
        EQUALS("$EQ"), // gets added in case of pastes automatically!
        NOT("$ne"),
        IS_NULL("$null"),
        NOT_NULL("$notNull"),
        GREATER_THAN("$gt"),
        GREATER_THAN_EQUALS("$gte"),
        LOWER_THAN("$lt"),
        LOWER_THAN_EQUALS("$lte");

        private final @NotNull String internalName;

        FilterConnection(final @NotNull String internalName) {
            this.internalName = internalName;
        }
    }
}
