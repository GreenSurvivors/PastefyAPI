package de.greensurvivors.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.greensurvivors.*;
import de.greensurvivors.admin.AdminSession;
import de.greensurvivors.admin.AdminUserReply;
import de.greensurvivors.admin.UserEditBuilder;
import de.greensurvivors.exception.HttpRequestFailedException;
import de.greensurvivors.implementation.reply.*;
import de.greensurvivors.implementation.reply.replywrapper.*;
import de.greensurvivors.reply.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class SessionImpl implements AdminSession { // todo throw exception for methods that needs a api key and our is null
    private final @Nullable String apiKey;
    private final @NotNull HttpClient httpClient;
    private final @NotNull String baseURL;
    private final @NotNull Gson gson;

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
    public <T> @NotNull CompletableFuture<@Nullable PasteReply> createPaste(final @NotNull PasteBuilder<T> pasteBuilder) {
        final HttpRequest request = createRequestBuilder(null, "paste").POST(HttpRequest.BodyPublishers.ofString(gson.toJson(pasteBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyWrapper.class)).
            thenApply(it -> {
                if (it != null && it.isSuccess()) {
                    return it.getPaste();
                } else {
                    return null;
                }
            });
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PasteReply> getPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable @Unmodifiable Set<@NotNull PasteReply>> getPastes() {
        final HttpRequest request = createRequestBuilder(null, "paste").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(pasteReplies -> pasteReplies == null ? null : Set.of(pasteReplies));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public <T> @NotNull CompletableFuture<PasteReply> editPaste(final @NotNull PasteBuilder<T> pasteBuilder) {
        final HttpRequest request = createRequestBuilder(null, "paste").PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(pasteBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyWrapper.class)).
            thenApply(pasteReplyWrapper -> pasteReplyWrapper.isSuccess() ? pasteReplyWrapper.getPaste() : null);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> starPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID, "star").POST(HttpRequest.BodyPublishers.noBody()).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> unstarPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID, "star").DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getMyStarredPastes() {
        final HttpRequest request = createRequestBuilder(null, "user/sharedpastes").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(pasteReplies -> pasteReplies == null ? null : Set.of(pasteReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getPublicPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(pasteReplies -> pasteReplies == null ? null : Set.of(pasteReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getTrendingPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes/trending").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(pasteReplies -> pasteReplies == null ? null : Set.of(pasteReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull PasteReply>> getLatestPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes/latest").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(pasteReplies -> pasteReplies == null ? null : Set.of(pasteReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> createFolder(final @NotNull FolderBuilder folderBuilder) {
        final HttpRequest request = createRequestBuilder(null, "folder").POST(HttpRequest.BodyPublishers.ofString(gson.toJson(folderBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyWrapper.class)).
            thenApply(folderReplyWrapper -> folderReplyWrapper.isSuccess() ? folderReplyWrapper.getFolder() : null);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId) {
        return getFolder(folderId, false);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId, final boolean hideSubFolder) {
        final HttpRequest request = createRequestBuilder(Map.of("hide_children", hideSubFolder), "folder", folderId).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable @Unmodifiable Set<@NotNull FolderReply>> getFolders() {
        final HttpRequest request = createRequestBuilder(null, "folder").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyImpl[].class)).
            thenApply(folderReplies -> folderReplies == null ? null : Set.of(folderReplies));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID) {
        final HttpRequest request = createRequestBuilder(null, "folder", folderID).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PublicUserReply> getPublicUserInformation(final @NotNull String userName) {
        final HttpRequest request = createRequestBuilder(null, "public/user", userName).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PublicUserReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PrivateUserReply> getMyAccountInfo() {
        final HttpRequest request = createRequestBuilder(null, "user").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PrivateUserReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable String> createNewAPIKey() {
        final HttpRequest request = createRequestBuilder(null, "user/keys").POST(HttpRequest.BodyPublishers.noBody()).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(APIKeyReplyWrapper.class)).
            thenApply(apiKeyReplyWrapper -> apiKeyReplyWrapper == null ? null : apiKeyReplyWrapper.getKey());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull String>> getMyAPIKeys() {
        final HttpRequest request = createRequestBuilder(null, "user/keys").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(String[].class)).
            thenApply(apiKeys -> apiKeys == null ? null : Set.of(apiKeys));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteAPIKey(final @NotNull String keyToDelete) {
        final HttpRequest request = createRequestBuilder(null, "user/keys", keyToDelete).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable List<@NotNull NotificationReply>> getNotifications() {
        final HttpRequest request = createRequestBuilder(null, "user/notification").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(NotificationReplyImpl[].class)).
            thenApply(notificationReplies -> notificationReplies == null ? null : List.of(notificationReplies));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> markAllNotificationsRead(){
        final HttpRequest request = createRequestBuilder(null, "user/notification/readall").GET().build(); // why not POST??

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Set<TagReply>> getAllTags() {
        final HttpRequest request = createRequestBuilder(null,  "public/tags").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(TagReplyImpl[].class)).
            thenApply(tagReplies -> tagReplies == null ? null : Set.of(tagReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable TagReply> getTag(final @NotNull String tag) {
        final HttpRequest request = createRequestBuilder(null, "public/tags", tag).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(TagReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PlatformInfoReply> getPlatformInfo() {
        final HttpRequest request = createRequestBuilder(null, "app/info").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PlatformInfoReplyImpl.class));
    }

    // ADMIN API BELOW! DANGER! NO DUCKS ALLOWED!

    @Override
    public @NotNull CompletableFuture<@Nullable Set<@NotNull AdminUserReply>> getUsers() {
        final HttpRequest request = createRequestBuilder(null, "admin/users").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(AdminUserReply[].class)).
            thenApply(adminUserReplies -> adminUserReplies == null ? null : Set.of(adminUserReplies));
    }

    @Override
    public @NotNull CompletableFuture<@Nullable AdminUserReply> getUser(final @NotNull String userId) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).GET().build();
                
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(AdminUserReply.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteUser(final @NotNull String userId) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Boolean> editUser(final @NotNull String userId, final @NotNull UserEditBuilder userEditBuilder) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(userEditBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(successReply -> successReply != null && successReply.isSuccess());
    }

    protected @NotNull HttpRequest.Builder createRequestBuilder(final @Nullable Map<@NotNull String, ? extends @NotNull Object> queryParameters, final @NotNull String... path) {
        final @NotNull String url = baseURL + String.join("/", path);

        final @NotNull HttpRequest.Builder requestBuilder;
        if (queryParameters == null || queryParameters.isEmpty()) {
            requestBuilder = HttpRequest.newBuilder(URI.create(url));
        } else {
            final StringJoiner queryJoiner = new StringJoiner("?", "&", "");
            for (Map.Entry<@NotNull String, ?> entry : queryParameters.entrySet()) {
                queryJoiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8));
            }
            requestBuilder = HttpRequest.newBuilder(URI.create(url + queryJoiner));
        }

        requestBuilder.header("Accept", "application/json");

        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        return requestBuilder;
    }

    protected <T> @NotNull Function<HttpResponse<String>, @Nullable T> deserializeBody(final @NotNull Class<T> clazz) {
        return stringHttpResponse -> {
            final @Nullable String body = stringHttpResponse.body();

            if (stringHttpResponse.statusCode() == HttpURLConnection.HTTP_OK) {
                if (body != null) {
                    return gson.fromJson(body, clazz);
                } else {
                    return null;
                }
            } else {
                if (body != null) {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode(), gson.fromJson(body, ErrorReply.class).getExceptionName());
                } else {
                    throw new HttpRequestFailedException(stringHttpResponse.statusCode());
                }
            }
        };
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
