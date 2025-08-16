package de.greensurvivors.implementation.response;

import java.util.Objects;

public class SuccessResponse {
    protected final boolean success;

    public SuccessResponse(boolean success) {
        this.success = success;
    }

    public boolean success() {
        return success;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SuccessResponse) obj;
        return this.success == that.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success);
    }

    @Override
    public String toString() {
        return "SuccessResponse[" +
            "success=" + success + ']';
    }
}
