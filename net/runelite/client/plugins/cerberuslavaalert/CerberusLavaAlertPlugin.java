package net.runelite.client.plugins.cerberuslavaalert;

import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.Notifier;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;

@PluginDescriptor(
        name = "Cerberus Lava Pre-Warn",
        description = "Alerts before Cerberus spawns lava pools",
        tags = {"cerberus", "lava", "pvm", "boss"}
)
public class CerberusLavaAlertPlugin extends Plugin
{
    private static final int CERBERUS_LAVA_ANIMATION = 4491;
    private static final long COOLDOWN_MS = 4000;

    @Inject
    private Notifier notifier;

    private long lastAlert = 0;

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (!(event.getActor() instanceof NPC))
        {
            return;
        }

        NPC npc = (NPC) event.getActor();

        if (!"Cerberus".equalsIgnoreCase(npc.getName()))
        {
            return;
        }

        if (npc.getAnimation() == CERBERUS_LAVA_ANIMATION)
        {
            long now = System.currentTimeMillis();
            if (now - lastAlert > COOLDOWN_MS)
            {
                notifier.notify("CERBERUS LAVA INCOMING — MOVE!");
                lastAlert = now;
            }
        }
    }
}
