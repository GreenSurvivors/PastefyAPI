package de.greensurvivors.implementation.content;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteContent;

import java.util.Map;

public class BundledContent<T> implements PasteContent<Map<String, PasteContent<T>>> {
    @Override
    public Map<String, PasteContent<T>> getContent() {
        return null;
    }

    @Override
    public Paste.PasteType getPasteType() {
        return Paste.PasteType.MULTI_PASTE;
    }
}
