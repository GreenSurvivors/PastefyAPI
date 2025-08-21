package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.PlatformInfoReply;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class PlatformInfoReplyImpl implements PlatformInfoReply {
    @SerializedName("login_required_for_create")
    private final boolean arePublicPastesEnabled;
    @SerializedName("login_required_for_read")
    private final boolean isLoginRequiredForRead;
    @SerializedName("login_required_for_write")
    private final boolean isLoginRequiredForWrite;
    @SerializedName("is_encrypted_by_default")
    private final boolean isEncryptedByDefault;
    @SerializedName("ai_enabled")
    private final boolean isAIEnabled;
    @SerializedName("custom_footer")
    private final @NotNull Map<@NotNull String, @NotNull String> customFooter;

    public PlatformInfoReplyImpl(boolean arePublicPastesEnabled,
                                 boolean isLoginRequiredForRead,
                                 boolean isLoginRequiredForWrite,
                                 boolean isEncryptedByDefault,
                                 boolean isAIEnabled,
                                 @NotNull Map<@NotNull String, @NotNull String> customFooter) {
        this.arePublicPastesEnabled = arePublicPastesEnabled;
        this.isLoginRequiredForRead = isLoginRequiredForRead;
        this.isLoginRequiredForWrite = isLoginRequiredForWrite;
        this.isEncryptedByDefault = isEncryptedByDefault;
        this.isAIEnabled = isAIEnabled;
        this.customFooter = customFooter;
    }

    @Override
    public boolean arePublicPastesEnabled() {
        return arePublicPastesEnabled;
    }

    @Override
    public boolean isLoginRequiredForRead() {
        return isLoginRequiredForRead;
    }

    @Override
    public boolean isLoginRequiredForWrite() {
        return isLoginRequiredForWrite;
    }

    @Override
    public boolean isEncryptedByDefault() {
        return isEncryptedByDefault;
    }

    @Override
    public boolean isAIEnabled() {
        return isAIEnabled;
    }

    @Override
    public @NotNull Map<@NotNull String, @NotNull String> getCustomFooter() {
        return customFooter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlatformInfoReplyImpl) obj;
        return this.arePublicPastesEnabled == that.arePublicPastesEnabled &&
            this.isLoginRequiredForRead == that.isLoginRequiredForRead &&
            this.isLoginRequiredForWrite == that.isLoginRequiredForWrite &&
            this.isEncryptedByDefault == that.isEncryptedByDefault &&
            this.isAIEnabled == that.isAIEnabled &&
            Objects.equals(this.customFooter, that.customFooter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arePublicPastesEnabled, isLoginRequiredForRead, isLoginRequiredForWrite, isEncryptedByDefault, isAIEnabled, customFooter);
    }

    @Override
    public String toString() {
        return "PlatformInfoReplyImpl[" +
            "arePublicPastesEnabled=" + arePublicPastesEnabled + ", " +
            "isLoginRequiredForRead=" + isLoginRequiredForRead + ", " +
            "isLoginRequiredForWrite=" + isLoginRequiredForWrite + ", " +
            "isEncryptedByDefault=" + isEncryptedByDefault + ", " +
            "isAIEnabled=" + isAIEnabled + ", " +
            "customFooter=" + customFooter + ']';
    }
}
