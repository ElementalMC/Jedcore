package com.jedk1.jedcore.util;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element.SubElement;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Runtime helper for GreenFireAbility support.
 * GreenFireAbility only exists in the ElementalMC fork of ProjectKorra.
 * Green fire is purely cosmetic — no stat modifiers apply.
 *
 * This helper uses reflection so JedCore compiles and runs cleanly against
 * both the upstream ProjectKorra jar and the ElementalMC fork.
 *
 * Usage:
 *   if (GreenFireHelper.canUseGreenFire(bPlayer)) {
 *       // render green particles instead of normal fire
 *   }
 */
public class GreenFireHelper {

    private static final boolean AVAILABLE;
    private static final SubElement GREEN_FIRE_SUBELEMENT;

    static {
        boolean available = false;
        SubElement subelement = null;

        try {
            // Confirm GreenFireAbility exists in this PK build.
            Class.forName("com.projectkorra.projectkorra.ability.GreenFireAbility");

            // Resolve the matching SubElement constant.
            try {
                Field field = SubElement.class.getField("GREEN_FIRE");
                subelement = (SubElement) field.get(null);
                available = true;
            } catch (Exception ignored) {
                // GreenFireAbility exists but no GREEN_FIRE SubElement constant found.
                // Cannot check bPlayer.canUseSubElement — treat as unavailable.
            }
        } catch (Exception ignored) {
            // GreenFireAbility not present in this build of ProjectKorra.
        }

        AVAILABLE = available;
        GREEN_FIRE_SUBELEMENT = subelement;
    }

    /** Returns true if green fire is fully available in this ProjectKorra build. */
    public static boolean isAvailable() {
        return AVAILABLE;
    }

    /**
     * Returns true if the given BendingPlayer can use green fire.
     * Always returns false when running against a PK build without GreenFireAbility.
     */
    public static boolean canUseGreenFire(BendingPlayer bPlayer) {
        if (!AVAILABLE || bPlayer == null) return false;
        return bPlayer.canUseSubElement(GREEN_FIRE_SUBELEMENT);
    }

    /** Convenience overload accepting a Player directly. */
    public static boolean canUseGreenFire(Player player) {
        if (!AVAILABLE || player == null) return false;
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        return canUseGreenFire(bPlayer);
    }
}