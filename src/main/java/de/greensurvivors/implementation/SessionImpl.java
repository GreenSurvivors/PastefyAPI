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
import de.greensurvivors.implementation.queryparam.AQueryParameter;
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
import java.util.*;
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
        this(null);
    }

    @Override
    public <T> @NotNull CompletableFuture<@NotNull PasteReply> createPaste(final @NotNull PasteBuilder<T> pasteBuilder) {
        final HttpRequest request = createRequestBuilder(null, "paste").
            POST(HttpRequest.BodyPublishers.ofString(gson.toJson(pasteBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyWrapper.class)).
            thenApply(PasteReplyWrapper::getPaste); // note: I'm 150% sure the wrapper doesn't return false currently, if not also accompanied by an error http status code
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PasteReply> getPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull PasteReply>> getPastes() {
        final HttpRequest request = createRequestBuilder(null, "paste").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull <T> CompletableFuture<@NotNull Boolean> editPaste(final @NotNull String pasteID, final @NotNull PasteBuilder<T> pasteBuilder) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID).
            PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(pasteBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess); // note: I'm 150% sure the wrapper doesn't return false currently, if not also accompanied by an error http status code
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> starPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID, "star").POST(HttpRequest.BodyPublishers.noBody()).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> unstarPaste(final @NotNull String pasteID) {
        final HttpRequest request = createRequestBuilder(null, "paste", pasteID, "star").DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMyStarredPastes() {
        final HttpRequest request = createRequestBuilder(null, "user/starred-pastes").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getMySharedPastes() {
        final HttpRequest request = createRequestBuilder(null, "user/sharedpastes").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getPublicPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getTrendingPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes/trending").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull PasteReply>> getLatestPastes() {
        final HttpRequest request = createRequestBuilder(null, "public-pastes/latest").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PasteReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull FolderReply> createFolder(final @NotNull FolderBuilder folderBuilder) {
        final HttpRequest request = createRequestBuilder(null, "folder").POST(HttpRequest.BodyPublishers.ofString(gson.toJson(folderBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyWrapper.class)).
            thenApply(FolderReplyWrapper::getFolder); // note: I'm 150% sure the wrapper doesn't return false currently, if not also accompanied by an error http status code
    }

    @Override
    public @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId) {
        return getFolder(folderId, Collections.emptySet());
    }

    @Override
    public @NotNull CompletableFuture<@NotNull FolderReply> getFolder(final @NotNull String folderId, final @NotNull Set<? extends @NotNull QueryParameter<? extends @NotNull Object>> queryParameters) {
        @SuppressWarnings("unchecked") // since the QueryParameter interface is sealed and only permits AQueryParameter, every Set of QueryParameter is a Set of AQueryParameter.
        final HttpRequest request = createRequestBuilder((Set<AQueryParameter<? extends @NotNull Object>>) queryParameters, "folder", folderId).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull @Unmodifiable Set<@NotNull FolderReply>> getFolders() {
        final HttpRequest request = createRequestBuilder(null, "folder").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(FolderReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID) {
        final HttpRequest request = createRequestBuilder(null, "folder", folderID).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PublicUserReply> getPublicUserInformation(final @NotNull String userName) {
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
    public @NotNull CompletableFuture<@NotNull String> createNewAPIKey() {
        final HttpRequest request = createRequestBuilder(null, "user/keys").POST(HttpRequest.BodyPublishers.noBody()).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(APIKeyReplyWrapper.class)).
            thenApply(APIKeyReplyWrapper::getKey);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull String>> getMyAPIKeys() {
        final HttpRequest request = createRequestBuilder(null, "user/keys").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(String[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteAPIKey(final @NotNull String keyToDelete) {
        final HttpRequest request = createRequestBuilder(null, "user/keys", keyToDelete).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<@NotNull NotificationReply>> getNotifications() {
        final HttpRequest request = createRequestBuilder(null, "user/notification").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(NotificationReplyImpl[].class)).
            thenApply(List::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> markAllNotificationsRead(){
        final HttpRequest request = createRequestBuilder(null, "user/notification/readall").GET().build(); // why not POST??

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull TagReply>> getAllTags() {
        final HttpRequest request = createRequestBuilder(null,  "public/tags").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(TagReplyImpl[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull TagReply> getTag(final @NotNull String tag) {
        final HttpRequest request = createRequestBuilder(null, "public/tags", tag).GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(TagReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PlatformInfoReply> getPlatformInfo() {
        final HttpRequest request = createRequestBuilder(null, "app/info").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(PlatformInfoReplyImpl.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull StatsReply> getPlatformStats() {
        final HttpRequest request = createRequestBuilder(null, "app/stats").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(StatsReplyImpl.class));
    }

    // ADMIN API BELOW! DANGER! NO DUCKS ALLOWED!

    @Override
    public @NotNull CompletableFuture<@NotNull Set<@NotNull AdminUserReply>> getUsers() {
        final HttpRequest request = createRequestBuilder(null, "admin/users").GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(AdminUserReply[].class)).
            thenApply(Set::of);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull AdminUserReply> getUser(final @NotNull String userId) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).GET().build();
                
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(AdminUserReply.class));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteUser(final @NotNull String userId) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).DELETE().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> editUser(final @NotNull String userId, final @NotNull UserEditBuilder userEditBuilder) {
        final HttpRequest request = createRequestBuilder(null, "admin/users", userId).
            PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(userEditBuilder))).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
            thenApply(deserializeBody(SuccessReply.class)).
            thenApply(SuccessReply::isSuccess);
    }

    // ----------------------- END API -----------------------

    protected @NotNull HttpRequest.Builder createRequestBuilder(final @Nullable Set<@NotNull AQueryParameter<? extends @NotNull Object>> queryParameters, final @NotNull String... path) {
        final @NotNull String url = baseURL + String.join("/", path);

        final @NotNull HttpRequest.Builder requestBuilder;
        if (queryParameters == null || queryParameters.isEmpty()) {
            requestBuilder = HttpRequest.newBuilder(URI.create(url));
        } else {
            final StringJoiner queryJoiner = new StringJoiner("&");
            for (AQueryParameter<? extends @NotNull Object> parameter : queryParameters) {
                queryJoiner.add(URLEncoder.encode(parameter.getName(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(parameter.getFormData(), StandardCharsets.UTF_8));
            }

            requestBuilder = HttpRequest.newBuilder(URI.create(url + "?" + queryJoiner));
        }

        requestBuilder.header("Accept", "application/json");

        if (apiKey != null) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        return requestBuilder;
    }

    protected <T> @NotNull Function<HttpResponse<String>, T> deserializeBody(final @NotNull Class<T> clazz) {
        return stringHttpResponse -> {
            final @Nullable String body = stringHttpResponse.body();

            // note: even though not needed for some requests to convey success (or falliere) the pastify web api always has a body
            // and always returns code 200 for success, no matter if other codes could fit better.
            if (body == null) {
                throw new HttpRequestFailedException(stringHttpResponse.statusCode());
            }

            if (stringHttpResponse.statusCode() == HttpURLConnection.HTTP_OK) {
                return gson.fromJson(body, clazz);
            } else {
                throw new HttpRequestFailedException(stringHttpResponse.statusCode(), gson.fromJson(body, ErrorReply.class).getExceptionName());
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
