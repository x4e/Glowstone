package net.glowstone.advancement;

import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import lombok.Data;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.util.TextMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;


@Data
public class GlowAdvancementDisplay {

    private final TextMessage title;
    private final TextMessage description;
    private final ItemStack icon;
    private final FrameType type;
    private final NamespacedKey background;
    private final float x;
    private final float y;

    private static final int HAS_BACKGROUND_TEXTURE = 0x1;
    private static final int SHOW_TOAST = 0x2;
    private static final int HIDDEN = 0x4;

    /**
     * Writes this notification to the given {@link ByteBuf}.
     *
     * @param buf the buffer to write to
     * @param hasBackgroundTexture Whether the advancement notification has a background texture
     * @param showToast Whether the advancement should be displayed as a toast notification
     * @param hidden
     * @return {@code buf}, with this notification written to it
     * @throws IOException if a string is too long
     */
    public ByteBuf encode(ByteBuf buf, boolean hasBackgroundTexture, boolean showToast, boolean hidden) throws IOException {
        int flags = (hasBackgroundTexture ? HAS_BACKGROUND_TEXTURE : 0)
        | (showToast ? SHOW_TOAST : 0)
        | (hidden ? HIDDEN : 0);

        GlowBufUtils.writeChat(buf, title);
        GlowBufUtils.writeChat(buf, description);
        GlowBufUtils.writeSlot(buf, icon);
        ByteBufUtils.writeVarInt(buf, type.ordinal());
        buf.writeInt(flags);
        ByteBufUtils.writeUTF8(buf, background.toString());
        buf.writeFloat(x);
        buf.writeFloat(y);
        return buf;
    }

    public enum FrameType {
        TASK, CHALLENGE, GOAL
    }
}
