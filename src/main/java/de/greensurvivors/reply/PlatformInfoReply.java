package de.greensurvivors.reply;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("unused") // api no tests reasonable
public interface PlatformInfoReply {
    boolean arePublicPastesEnabled();
    boolean isLoginRequiredForRead();
    boolean isLoginRequiredForWrite();
    boolean isEncryptedByDefault();
    boolean isAIEnabled();
    @NotNull Map<@NotNull String, @NotNull String> getCustomFooter();
}
