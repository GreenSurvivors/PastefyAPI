package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.greensurvivors.*;
import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.implementation.response.FolderReplyWrapper;
import de.greensurvivors.implementation.response.PasteReplyWrapper;
import de.greensurvivors.implementation.response.SuccessReply;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Security;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
                        final @NotNull PasteReplyWrapper pastResponse = gson.fromJson(body, PasteReplyWrapper.class);

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
                        return gson.fromJson(body, SuccessReply.class).isSuccess();
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
                        final @NotNull FolderReplyWrapper folderResponse = gson.fromJson(body, FolderReplyWrapper.class);

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
                        return gson.fromJson(body, SuccessReply.class).isSuccess();
                    } else {
                        return false;
                    }
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            });
    }

    protected static class InstantAdapter extends TypeAdapter<Instant> {
        // TimeStamp - as used by the web api - or Instant, as used by this lib, is always utc.
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n").withZone(ZoneOffset.UTC);

        @Override
        public void write(JsonWriter out, Instant value) throws IOException {
            boolean originalSerializeNulls = out.getSerializeNulls();
            out.setSerializeNulls(false);

            try {
                out.value(DATE_TIME_FORMATTER.format(value));
            } finally {
                // Restore original behavior for the rest of the data.
                out.setSerializeNulls(originalSerializeNulls);
            }
        }

        @Override
        public Instant read(JsonReader in) throws IOException {
            return Instant.from(DATE_TIME_FORMATTER.parse(in.nextString()));
        }
    }
}
