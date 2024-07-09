package com.provismet.tooltipscroll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.stream.JsonReader;

import com.provismet.lilylib.util.JsonBuilder;
import net.minecraft.util.math.MathHelper;

public abstract class Options {
    public static boolean canScroll = true;
    public static boolean useWASD = false;
    public static boolean resetOnUnlock = true;
    public static boolean useLShift = true;
    public static boolean invertXScroll = false;
    public static boolean invertYScroll = false;

    public static final String CAN_SCROLL = "canScroll";
    public static final String USE_WASD = "useWASD";
    public static final String RESET_ON_UNLOCK = "resetOnUnlock";
    public static final String USE_LEFT_SHIFT = "useLShift";
    public static final String INVERT_X_SCROLL = "invertXScroll";
    public static final String INVERT_Y_SCROLL = "invertYScroll";
    public static final String SCROLL_SPEED = "scrollSpeed";
    public static final String SCROLL_SPEED_KEYBOARD = "keyboardScrollSpeed";
    public static final String SMOOTHNESS = "scrollSmoothness";

    public static void saveJSON () {
        JsonBuilder builder = new JsonBuilder();
        String json = builder.start()
            .append(CAN_SCROLL, canScroll).newLine()
            .append(USE_WASD, useWASD).newLine()
            .append(RESET_ON_UNLOCK, resetOnUnlock).newLine()
            .append(USE_LEFT_SHIFT, useLShift).newLine()
            .append(INVERT_X_SCROLL, invertXScroll).newLine()
            .append(INVERT_Y_SCROLL, invertYScroll).newLine()
            .append(SCROLL_SPEED, ScrollTracker.scrollSize).newLine()
            .append(SCROLL_SPEED_KEYBOARD, ScrollTracker.scrollSizeKeyboard).newLine()
            .append(SMOOTHNESS, ScrollTracker.smoothnessModifier).newLine(false)
            .closeObject()
            .toString();

        try {
            FileWriter writer = new FileWriter("config/tooltipscroll.json");
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            TooltipScrollClient.LOGGER.error("Encountered error whilst trying to save config JSON.", e);
        }
    }

    public static void readJSON () {
        try {
            FileReader reader = new FileReader("config/tooltipscroll.json");
            JsonReader parser = new JsonReader(reader);

            parser.beginObject();
            while (parser.hasNext()) {
                final String label = parser.nextName();
                switch (label) {
                    case CAN_SCROLL:
                        Options.canScroll = parser.nextBoolean();
                        break;
                    
                    case USE_WASD:
                        Options.useWASD = parser.nextBoolean();
                        break;
                    
                    case RESET_ON_UNLOCK:
                        Options.resetOnUnlock = parser.nextBoolean();
                        break;

                    case USE_LEFT_SHIFT:
                        Options.useLShift = parser.nextBoolean();
                        break;

                    case INVERT_X_SCROLL:
                        Options.invertXScroll = parser.nextBoolean();
                        break;

                    case INVERT_Y_SCROLL:
                        Options.invertYScroll = parser.nextBoolean();
                        break;

                    case SCROLL_SPEED:
                        ScrollTracker.scrollSize = (int)MathHelper.absMax(1, parser.nextInt());
                        break;
                    
                    case SCROLL_SPEED_KEYBOARD:
                        ScrollTracker.scrollSizeKeyboard = (int)MathHelper.absMax(1, parser.nextInt());
                        break;

                    case SMOOTHNESS:
                        ScrollTracker.smoothnessModifier = MathHelper.absMax(0.05, parser.nextDouble());
                        if (ScrollTracker.smoothnessModifier > 1.0) ScrollTracker.smoothnessModifier = 1.0;
                        break;
                
                    default:
                        break;
                }
            }
            parser.close();
        } catch (FileNotFoundException e) {
            try {
                (new File("config")).mkdirs();
                saveJSON();
            } catch (Exception e2) {
                // Do nothing.
            }
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
