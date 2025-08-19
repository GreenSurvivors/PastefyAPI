package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.greensurvivors.*;
import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.implementation.response.FolderResponse;
import de.greensurvivors.implementation.response.PasteResponse;
import de.greensurvivors.implementation.response.SuccessResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Security;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class SessionImpl implements Session {
    private final @Nullable String apiKey;
    private final @NotNull HttpClient httpClient;
    private final @NotNull String baseURL;
    private final @NotNull Gson gson;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SessionImpl(final @NotNull String serverAddress, final @Nullable String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseURL = serverAddress + "/api/v2/";
        this.apiKey = apiKey;

        this.gson = new GsonBuilder().
            registerTypeAdapter(Instant.class, new InstantAdapter()).
            registerTypeAdapter(PasteBuilderImpl.class, new PasteBuilderImpl.PasteBuilderJsonSerializer()).
            create();
    }

    public SessionImpl(final @Nullable String apiKey) {
        this("https://pastefy.app", apiKey);
    }

    public SessionImpl() {
        this("https://pastefy.app");
    }

    @Override
    public <T> @NotNull CompletableFuture<PasteReply> createPaste(final @NotNull PasteBuilder<T> pasteBuilder) {
        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(baseURL + "paste"));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        final HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(pasteBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        final @NotNull PasteResponse pastResponse = gson.fromJson(body, PasteResponse.class);

                        if (pastResponse.isSuccess()) {
                            return pastResponse.getPaste();
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PasteReply> getPaste(final @NotNull String pasteID) {
        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(baseURL + "paste/"+pasteID));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        final HttpRequest request = requestBuilder.GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        return gson.fromJson(body, PasteReplyImpl.class);
                    } else {
                        return null;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID) {
        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(baseURL + "paste/"+pasteID));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        final HttpRequest request = requestBuilder.DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        return gson.fromJson(body, SuccessResponse.class).isSuccess();
                    } else {
                        return false;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> createFolder(final @NotNull FolderBuilder folderBuilder) {
        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(baseURL + "folder"));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        final HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(folderBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        final @NotNull FolderResponse folderResponse = gson.fromJson(body, FolderResponse.class);

                        if (folderResponse.isSuccess()) {
                            return folderResponse.getFolder();
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId) {
        return getFolder(folderId, false);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId, final boolean hideSubFolder) {
        @NotNull String urlStr = baseURL + "folder/" + folderId;

        if (hideSubFolder) {
            urlStr += "?hide_children=true";
        }

        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(urlStr));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        final HttpRequest request = requestBuilder.GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        return gson.fromJson(body, FolderReplyImpl.class);
                    } else {
                        return null;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID) {
        final @NotNull HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(baseURL + "folder/"+folderID));
        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        final HttpRequest request = requestBuilder.DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(stringHttpResponse -> {
                if (stringHttpResponse.statusCode() == 200) { // status == ok
                    final @Nullable String body = stringHttpResponse.body();

                    if (body != null) {
                        return gson.fromJson(body, SuccessResponse.class).isSuccess();
                    } else {
                        return false;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }
}
