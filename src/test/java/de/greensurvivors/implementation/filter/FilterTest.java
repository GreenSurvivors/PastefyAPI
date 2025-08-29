package de.greensurvivors.implementation.filter;

import de.greensurvivors.exception.NestedFilterException;
import de.greensurvivors.implementation.queryparam.AQueryParameter;
import de.greensurvivors.implementation.queryparam.filter.AProtoFilterImpl;
import de.greensurvivors.queryparam.FilterParameter;
import de.greensurvivors.queryparam.QueryParameter;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.SequencedSet;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

public class FilterTest {
    @Test
    public void pathTest() {
        assertEquals("filter[1[2[3[folder]]]]", AProtoFilterImpl.joinPath(List.of("filter", "1", "2", "3"), "folder"));
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

        assertEquals("filter%5B%2524EQ%5BuserId%5D%5D=user&filter%5B%2524and%5B%2524EQ%5Bencrypted%5D%5D%5D=false&filter%5B%2524and%5B%2524null%5BforkedFrom%5D%5D%5D=ignored", queryJoiner.toString());
    }
}
