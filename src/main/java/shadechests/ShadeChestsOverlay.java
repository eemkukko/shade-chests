package shadechests;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;

public class ShadeChestsOverlay extends Overlay
{

	private final Client client;
	private final ShadeChestsPlugin plugin;
	private final ShadeChestsConfig config;

	@Inject
	private ShadeChestsOverlay(Client client, ShadeChestsPlugin plugin, ShadeChestsConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		for (GameObject chest : plugin.getChestsToHighlight())
		{
			final Shape polygon = chest.getConvexHull();
			if (polygon != null)
			{
				OverlayUtil.renderPolygon(graphics, polygon, config.chestHighlightColor());
			}
		}
		return null;
	}
}
