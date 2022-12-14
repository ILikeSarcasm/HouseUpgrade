package me.ilikesarcasm.houseupgrade.events.cancellable;

import me.ilikesarcasm.houseupgrade.houses.House;

/**
 * Broadcasted when a house will be upgraded.
 */
public class UpgradingHouseEvent extends CancellableHouseEvent {

    private final int newLevel;

    /**
     * Constructor.
     * @param house The house that will be upgraded.
     * @param level The new level of the house.
     */
    public UpgradingHouseEvent(House house, int newLevel) {
        super(house);
        this.newLevel = newLevel;
    }

    /**
     * Returns the new level of the house.
     * @return The new level of the house.
     */
    public int getNewLevel() {
        return this.newLevel;
    }

}
