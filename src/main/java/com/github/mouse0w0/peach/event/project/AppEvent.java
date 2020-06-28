package com.github.mouse0w0.peach.event.project;

import com.github.mouse0w0.eventbus.Cancellable;
import com.github.mouse0w0.eventbus.Event;

public class AppEvent implements Event {

    protected AppEvent() {
    }

    public static final class Closing extends AppEvent {
    }

    public static final class CanClose extends AppEvent implements Cancellable {

        private boolean cancelled;

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }

    public static final class WillBeClosed extends AppEvent {
    }
}
