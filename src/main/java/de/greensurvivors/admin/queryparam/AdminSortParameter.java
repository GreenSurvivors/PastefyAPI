package de.greensurvivors.admin.queryparam;

import de.greensurvivors.implementation.queryparam.SortParameterImpl;
import de.greensurvivors.queryparam.SortParameter;
import org.jetbrains.annotations.NotNull;

public sealed interface AdminSortParameter extends SortParameter permits SortParameterImpl {
    abstract class AdminSortType extends SortType {

        protected AdminSortType(@NotNull String internalName) {
            super(internalName);
        }

        public static final AdminSortType PASTE_VERSION = new AdminSortType("version") {}; // paste
        public static final AdminSortType PASTE_STORAGE_TYPE = new AdminSortType("storageType") {}; // paste
        public static final AdminSortType ACCOUNT_STATUS = new AdminSortType("type") {}; // user
        public static final AdminSortType USER_AUTH_PROVIDER = new AdminSortType("authProvider") {}; // user
        public static final AdminSortType USER_AUTH_ID = new AdminSortType("authId") {}; // user
        public static final AdminSortType USER_UNIQUE_NAME = new AdminSortType("uniqueName") {}; // user
        public static final AdminSortType USER_EMAIL_ADDRESS = new AdminSortType("eMail") {}; // user
        public static final AdminSortType AUTH_KEY_TYPE = new AdminSortType("type") {}; // authKey
    }
}
