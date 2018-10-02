package top.seraphjack.voterestart;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashSet;
import java.util.UUID;

public class RestartManager {
    private HashSet<UUID> voters;
    private boolean restarting = false;

    public RestartManager() {
        voters = new HashSet<>();
    }

    public boolean vote(Player p) {
        UUID uuid = p.getUniqueId();
        if (voters.contains(uuid)) return false;
        voters.add(uuid);
        update();
        broadcast(
                Text.builder(p.getDisplayNameData().displayName().toString()).append(
                        Text.builder(" is now voting to restart. "
                                + voters.size() + '/' + Sponge.getServer().getOnlinePlayers().size()
                                + '(' + ((int) (double) voters.size() / (double) Sponge.getServer().getOnlinePlayers().size() * 100) + "%)")
                                .color(TextColors.GOLD)
                                .build()
                )
                        .build());
        return true;
    }

    public boolean deVote(Player p) {
        UUID uuid = p.getUniqueId();
        if (voters.contains(uuid)) {
            voters.remove(uuid);
            update();
            broadcast(
                    Text.builder(p.getDisplayNameData().displayName().toString()).append(
                            Text.builder(" is no longer voting to restart. "
                                    + voters.size() + '/' + Sponge.getServer().getOnlinePlayers().size()
                                    + '(' + ((int) (double) voters.size() / (double) Sponge.getServer().getOnlinePlayers().size() * 100) + "%)")
                                    .color(TextColors.GOLD)
                                    .build()
                    )
                            .build());
            return true;
        }
        return false;
    }

    private void update() {
        if (voters.size() >= Sponge.getGame().getServer().getOnlinePlayers().size() * ConfigLoader.votePercentage) {
            if (!restarting) {
                restarting = true;
                new Thread(() -> {
                    // Behold, long arrow in Java!
                    for (int i = 10; i-->0; ) {
                        if (!restarting) {
                            broadcast(Text.builder("[VoteRestart] Restart canceled").color(TextColors.GOLD).build());
                            return;
                        }
                        final int I = i + 1;
                        broadcast(Text.builder("[VoteRestart] Server restart in " + I + " secs").color(TextColors.GOLD).build());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Sponge.getScheduler().createTaskBuilder()
                            .execute(() -> Sponge.getServer().shutdown(Text.of("Server restarting, please wait...")))
                            .submit(VoteRestart.INSTANCE);
                }).start();
            }
        } else {
            restarting = false;
        }
    }

    private void broadcast(Text text) {
        Sponge.getScheduler().createTaskBuilder()
                .execute(() -> Sponge.getServer().getBroadcastChannel().send(text))
                .submit(VoteRestart.INSTANCE);
    }
}
