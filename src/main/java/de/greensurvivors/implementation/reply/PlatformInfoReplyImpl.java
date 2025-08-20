package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.PlatformInfoReply;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class PlatformInfoReplyImpl implements PlatformInfoReply {
    @SerializedName("login_required_for_create")
    private final boolean arePublicPastesEnabled;
    @SerializedName("login_required_for_read")
    private final boolean isLoginRequiredForRead;
    @SerializedName("login_required_for_create")
    private final boolean isLoginRequiredForWrite;
    @SerializedName("login_required_for_create")
    private final boolean isEncryptedByDefault;
    @SerializedName("ai_enabled")
    private final boolean isAIEnabled;
    @SerializedName("custom_footer")
    private final @NotNull Map<@NotNull String, @NotNull String> customFooter;

    private PlatformInfoReplyImpl(boolean arePublicPastesEnabled, boolean isLoginRequiredForRead, boolean isLoginRequiredForWrite, boolean isEncryptedByDefault, boolean isAIEnabled, @NotNull Map<@NotNull String, @NotNull String> customFooter) {
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
}
