package sg.skylvsme.dispolitics.messaging;

import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LobbyBroadcaster {

    public enum LobbyMessages {
        UPDATE,
        REDIRECT_TO_GAME
    }

    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<LobbyMessages>> listeners = new LinkedList<>();

    public static synchronized Registration register(
            Consumer<LobbyMessages> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (LobbyBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(LobbyMessages message) {
        for (Consumer<LobbyMessages> listener : listeners) {
            executor.execute(() -> listener.accept(message));
        }
    }
}

