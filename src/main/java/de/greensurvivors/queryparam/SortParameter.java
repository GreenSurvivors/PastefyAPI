package de.greensurvivors.queryparam;

import de.greensurvivors.admin.queryparam.AdminSortParameter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public sealed interface SortParameter extends QueryParameter<@NotNull Map<? extends SortParameter.@NotNull SortType, @NotNull Boolean>> permits AdminSortParameter {
    abstract class SortType {
        protected final @NotNull String internalName;

        protected SortType(final @NotNull String internalName) {
            this.internalName = internalName;
        }

        public @NotNull String getInternalName() {  // todo somehow move this out of api
            return internalName;
        }

        public static final SortType TIME_CREATED = new SortType("createdAt") {}; // paste, folder, authkey, user
        public static final SortType TIME_EXPIRES = new SortType("expireAt") {}; // paste
        public static final SortType POPULARITY = new SortType("engagementScore") {}; // paste
        public static final SortType TITLE = new SortType("title") {}; // paste
        public static final SortType DISPLAY_NAME = new SortType("uniqueName") {}; // user
        public static final SortType ID_USER = new SortType("userId") {}; // paste, folder, authkey
        public static final SortType ID_FORKED_FROM = new SortType("forkedFrom") {}; // paste
        public static final SortType ENCRYPTED = new SortType("encrypted") {}; // paste
        public static final SortType PASTE_TYPE = new SortType("type") {}; // paste
        public static final SortType VISIBILITY = new SortType("visibility") {}; // paste
        public static final SortType FOLDER = new SortType("folder") {}; // paste
        public static final SortType PARENT_FOLDER = new SortType("parent") {}; // folder
        public static final SortType TIME_UPDATED = new SortType("updatedAt") {}; // folder, authkey, user
    }
}
