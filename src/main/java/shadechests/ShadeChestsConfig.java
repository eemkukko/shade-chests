package shadechests;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("shadechests")
public interface ShadeChestsConfig extends Config
{
	@ConfigItem(
		keyName = "chestHighlightColor",
		name = "Highlight Color",
		description = "The color to highlight chests with"
	)
	default Color chestHighlightColor()
	{
		return Color.GREEN;
	}
}
