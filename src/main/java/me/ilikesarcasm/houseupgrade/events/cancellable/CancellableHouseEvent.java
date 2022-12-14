package me.ilikesarcasm.houseupgrade.events.cancellable;

import me.ilikesarcasm.houseupgrade.events.HouseEvent;
import me.ilikesarcasm.houseupgrade.houses.House;
import org.bukkit.event.Cancellable;

public class CancellableHouseEvent extends HouseEvent implements Cancellable {

    private boolean cancelled;
    private String reason;

    public CancellableHouseEvent(House house) {
        super(house);
        this.cancelled = false;
    }

    /**
     * Check if the event has been cancelled.
     * @return true if the event has been cancelled, otherwise false
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets if the event is cancelled or not.
     * @param cancelled true if the event is cancelled, false otherwise
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Check if the event has been cancelled.
     * @return true if the event has been cancelled, otherwise false
     */
    public boolean hasBeenCancelled() {
        return this.cancelled;
    }

    /**
     * Returns the reason why this event is cancelled.
     * @return null if there is no reason or the event is not cancelled, otherwise a string
     */
    public String getReason() {
        return reason;
    }

    /**
     * Cancel the event from happening.
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Cancel the event from happening.
     * @param reason The reason of cancelling, used for display to the user, should end with a dot
     */
    public void cancel(String reason) {
        this.cancelled = true;
        this.reason = reason;
    }

    /**
     * Let the event continue, possiblly overwriting a cancel() call from another plugin
     */
    public void allow() {
        this.cancelled = false;
        this.reason = null;
    }

}
