package me.ilikesarcasm.houseupgrade.events;

import me.ilikesarcasm.houseupgrade.houses.House;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HouseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    protected House house;

    public HouseEvent(House house) {
        this.house = house;
    }

    @Override
    public HandlerList getHandlers() {
        return HouseEvent.HANDLERS;
    }

    public House getHouse() {
        return this.house;
    }

}
