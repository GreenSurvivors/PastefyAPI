package de.greensurvivors.implementation.filter;

import de.greensurvivors.implementation.queryparam.filter.AProtoFilterImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterTest {
    @Test
    public void pathTest() {
        assertEquals("filter[1[2[3[folder]]]]", AProtoFilterImpl.joinPath(List.of("filter", "1", "2", "3"), "folder"));
    }
}
