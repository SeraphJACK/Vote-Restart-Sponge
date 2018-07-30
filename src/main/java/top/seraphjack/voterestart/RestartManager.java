package top.seraphjack.voterestart;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.HashSet;
import java.util.UUID;

public class RestartManager {
    private HashSet<UUID> voters;

    public RestartManager() {
        voters = new HashSet<>();
    }

    public boolean vote(Player p) {
        UUID uuid = p.getUniqueId();
        if (voters.contains(uuid)) return false;
        voters.add(uuid);
        update();
        return true;
    }

    public boolean deVote(Player p) {
        UUID uuid = p.getUniqueId();
        if (voters.contains(uuid)) {
            voters.remove(uuid);
            update();
            return true;
        }
        return false;
    }

    private void update() {
        Sponge.getServer().getBroadcastChannel().send(Text.of("[VoteRestart] Current votes: " + voters.size()
                + ", Server will restart once votes reached "
                + Sponge.getServer().getOnlinePlayers().size() * ConfigLoader.votePercentage + "."));

        if (voters.size() >= Sponge.getGame().getServer().getOnlinePlayers().size() * ConfigLoader.votePercentage) {
            new Thread(() -> {
                // Behold, long arrow in Java!
                for (int i = 10; i--> 0; ) {
                    final int I = i + 1;
                    Sponge.getScheduler().createTaskBuilder()
                            .execute(() -> Sponge.getServer().getBroadcastChannel().send(Text.of("[VoteRestart] Server restart in " + I + " secs")))
                            .submit(VoteRestart.INSTANCE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sponge.getScheduler().createTaskBuilder()
                        .execute(() -> Sponge.getServer().shutdown(Text.of("Server restarting, please wait")))
                        .submit(VoteRestart.INSTANCE);
            }).start();
        }
    }
}
