package de.greensurvivors.implementation.filter;

import de.greensurvivors.Paste;
import de.greensurvivors.Session;
import de.greensurvivors.exception.NestedFilterException;
import de.greensurvivors.implementation.queryparam.AQueryParameter;
import de.greensurvivors.implementation.queryparam.filter.AProtoFilterImpl;
import de.greensurvivors.queryparam.FilterParameter;
import de.greensurvivors.queryparam.QueryParameter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.SequencedSet;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class FilterTest {
    private static Session session;
    private static boolean hasAPIKey;

    @BeforeAll
    public static void Setup() {
        final String apiKey = System.getenv("PastefyAPIKey");
        session = Session.newSession(apiKey);
        hasAPIKey = apiKey != null;
    }

    @AfterAll
    public static void close() throws Exception {
        session.close();
    }

    @Test
    public void pathTest() {
        assertEquals("filter[1][2][3][folder]", AProtoFilterImpl.joinPath(List.of("filter", "1", "2", "3"), "folder"));
    }

    @Test
    public void builderTest() throws NestedFilterException {
        final SequencedSet<FilterParameter<?>> filters = QueryParameter.newFilterBuilder().
            userId("user").
            and(
                QueryParameter.newFilterBuilder().
                    isEncrypted(false).
                    isNull(
                        QueryParameter.newFilterBuilder().
                            forkedFromPaste("ignored")
                    )
            ).build();

        assertFalse(filters.isEmpty());

        final StringJoiner queryJoiner = new StringJoiner("&");
        for (FilterParameter<?> parameter : filters) {
            // since the QueryParameter interface is sealed and only permits AQueryParameter, every Set of QueryParameter is a Set of AQueryParameter.
            queryJoiner.add(URLEncoder.encode(((AQueryParameter<?>) parameter).getName(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(((AQueryParameter<?>) parameter).getFormData(), StandardCharsets.UTF_8));
        }

        assertEquals("filter%5BuserId%5D=user&filter%5B%24and%5D%5Bencrypted%5D=false&filter%5B%24and%5D%5B%24null%5D%5BforkedFrom%5D=ignored", queryJoiner.toString());
    }

    @Test
    public void testGetPastesOfUser() {
        assumeTrue(hasAPIKey);

        assertTrue(session.getPastes(
            QueryParameter.newFilterBuilder().and(
                    QueryParameter.newFilterBuilder().
                        pasteVisibility(Paste.PasteVisibility.UNLISTED)
                ).userId("1fO2HTkr").
                build()
        ).join().isEmpty());

        assertFalse(session.getPastes(
            QueryParameter.newFilterBuilder().
                userId("1fO2HTkr").
                build()
        ).join().isEmpty());
    }
}
