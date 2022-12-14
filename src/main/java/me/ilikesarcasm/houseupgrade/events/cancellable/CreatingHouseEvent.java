package me.ilikesarcasm.houseupgrade.events.cancellable;

import me.ilikesarcasm.houseupgrade.houses.House;

/**
 * Broadcasted when a house will be created.
 */
public class CreatingHouseEvent extends CancellableHouseEvent {

    /**
     * Constructor.
     * @param house The house that will be created.
     */
    public CreatingHouseEvent(House house) {
        super(house);
    }

}
