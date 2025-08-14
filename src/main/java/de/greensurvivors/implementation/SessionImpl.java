package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteReply;
import de.greensurvivors.Session;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Security;
import java.util.concurrent.CompletableFuture;

public class SessionImpl implements Session {
    private final @Nullable String apiKey;
    private final @Nullable HttpClient httpClient;
    private final @NotNull String baseURL;
    private final @NotNull Gson gson;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SessionImpl(final @NotNull String serverAddress, final @Nullable String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseURL = serverAddress + "/api/v2/";
        this.apiKey = apiKey;

        this.gson = new GsonBuilder().create();
    }

    public SessionImpl(final @Nullable String apiKey) {
        this("https://pastefy.app", apiKey);
    }

    public SessionImpl() {
        this("https://pastefy.app", null);
    }

    @Override
    public <T> @NotNull CompletableFuture<PasteReply<T>> createPaste(@NotNull PasteBuilder<T> builder) {
        return ;

    }

    @Override
    public @NotNull <T> CompletableFuture<@Nullable PasteReply<T>> getPaste(@NotNull String pasteID) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(baseURL + "paste/" + pasteID))
            .header("Authorization", "Bearer " + apiKey)
            .GET()
            .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        return gson.fromJson(body, Paste.class);
                    } else {
                        return null;
                    }
                } else {
                    throw ;
                }
            });
    }
}
