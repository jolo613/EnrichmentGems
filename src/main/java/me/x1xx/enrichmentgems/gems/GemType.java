package me.x1xx.enrichmentgems.gems;

import me.x1xx.enrichmentgems.gems.types.*;

public enum GemType {
    COAL(new Coal()),
    LAPIS(new Lapis()),
    REDSTONE(new Redstone()),
    IRON(new Iron()),
    GOLD(new Gold()),
    DIAMOND(new Diamond()),
    EMERALD(new Emerald());


    private final Gem gem;

    GemType(Gem gem) {
        this.gem = gem;
    }

    public Gem getGem() {
        return gem;
    }

    public static GemType fromString(String s) {
        for (GemType value : values()) {
            if (value.toString().equalsIgnoreCase(s)) {
                return value;
            }
        }
        return null;
    }
}
