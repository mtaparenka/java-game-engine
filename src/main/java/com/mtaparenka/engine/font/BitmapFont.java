package com.mtaparenka.engine.font;

import com.mtaparenka.Texture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitmapFont {
    private static final Pattern CHAR_PATTERN = Pattern.compile("char id=(\\d+)\\s+x=(\\d+)\\s+y=(\\d+)\\s+width=(\\d+)\\s+height=(\\d+)\\s+xoffset=(-?\\d+)\\s+yoffset=(-?\\d+)\\s+xadvance=(-?\\d+)");
    private static final Pattern COMMON_LINE_PATTERN = Pattern.compile("common lineHeight=(\\d+)\\s+base=(\\d+)\\s+scaleW=(\\d+)\\s+scaleH=(\\d+)");

    public final Texture atlasTexture;
    public int lineHeight;
    public int base;
    public int scaleW;
    public int scaleH;
    public Map<Integer, Glyph> glyphs = new HashMap<>();

    public BitmapFont(String fntPath, Texture atlasTexture) {
        this.atlasTexture = atlasTexture;

        try {
            for (String line : Files.readAllLines(Path.of(fntPath))) {
                if (line.startsWith("char id")) {
                    Glyph glyph = getGlyph(line);
                    glyphs.put(glyph.id(), glyph);
                } else if (line.startsWith("common")) {
                    Matcher matcher = COMMON_LINE_PATTERN.matcher(line);
                    matcher.find();
                    lineHeight = Integer.parseInt(matcher.group(1));
                    base = Integer.parseInt(matcher.group(2));
                    scaleW = Integer.parseInt(matcher.group(3));
                    scaleH = Integer.parseInt(matcher.group(4));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Glyph getGlyph(String line) {
        Matcher matcher = CHAR_PATTERN.matcher(line);
        matcher.find();

        int id = Integer.parseInt(matcher.group(1));
        int x = Integer.parseInt(matcher.group(2));
        int y = Integer.parseInt(matcher.group(3));
        int width = Integer.parseInt(matcher.group(4));
        int height = Integer.parseInt(matcher.group(5));
        int xOffset = Integer.parseInt(matcher.group(6));
        int yOffset = Integer.parseInt(matcher.group(7));
        int xAdvance = Integer.parseInt(matcher.group(8));

        return new Glyph(id, x, y, width, height, xOffset, yOffset, xAdvance);
    }
}
