package de.greensurvivors;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused") // api, no test to use all reasonable
public enum AccountStaus {
    @SerializedName("USER")
    VALID_USER,
    @SerializedName("ADMIN")
    VALID_ADMIN,
    BLOCKED,
    AWAITING_ACCESS
}
