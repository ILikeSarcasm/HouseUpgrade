package me.ilikesarcasm.houseupgrade.events;

import me.ilikesarcasm.houseupgrade.houses.House;

public class HouseRegistered extends HouseEvent {

    /**
     * Constructor.
     * @param region The house that will be created.
     */
    public HouseRegistered(House house) {
        super(house);
    }
    
}
