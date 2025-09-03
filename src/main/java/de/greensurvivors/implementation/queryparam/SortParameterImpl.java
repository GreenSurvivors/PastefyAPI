package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.admin.queryparam.AdminSortParameter;
import de.greensurvivors.queryparam.SortParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public non-sealed class SortParameterImpl extends AQueryParameter<@NotNull Map<? extends SortParameter.@NotNull SortType, @NotNull Boolean>> implements AdminSortParameter {
    public SortParameterImpl(final @NotNull Map<? extends @NotNull SortType, Boolean> value) {
        super("sort", value);
    }

    // return an unmodifiable copy
    @Override
    public @NotNull @Unmodifiable Map<? extends @NotNull SortType, Boolean> getValue() {
        return Map.copyOf(super.getValue());
    }

    @Override
    public @NotNull String getFormData() throws IllegalArgumentException {
        if (super.getValue().isEmpty()) {
            throw new IllegalArgumentException("Sort types are missing!");
        }

        return String.join(",", super.getValue().entrySet().stream().map(sortTypeEntry ->
            !sortTypeEntry.getValue() ? sortTypeEntry.getKey().getInternalName() + "+" : sortTypeEntry.getKey().getInternalName()
        ).toArray(String[]::new));
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
    private AbstractArray scopes;
    */

    /* User columns:

    @Column(size = 8, id = true)
    public String id;

    @Column
    public String avatar;
    */
}
