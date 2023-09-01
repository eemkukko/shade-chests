package shadechests;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;

import static net.runelite.api.ObjectID.BRONZE_CHEST;       // Red
import static net.runelite.api.ObjectID.BRONZE_CHEST_4112;  // Brown
import static net.runelite.api.ObjectID.BRONZE_CHEST_4113;  // Crimson
import static net.runelite.api.ObjectID.BRONZE_CHEST_4114;  // Black
import static net.runelite.api.ObjectID.BRONZE_CHEST_4115;  // Purple

import static net.runelite.api.ObjectID.STEEL_CHEST;        // Red
import static net.runelite.api.ObjectID.STEEL_CHEST_4117;   // Brown
import static net.runelite.api.ObjectID.STEEL_CHEST_4118;   // Crimson
import static net.runelite.api.ObjectID.STEEL_CHEST_4119;   // Black
import static net.runelite.api.ObjectID.STEEL_CHEST_4120;   // Purple

import static net.runelite.api.ObjectID.BLACK_CHEST;        // Red
import static net.runelite.api.ObjectID.BLACK_CHEST_4122;   // Brown
import static net.runelite.api.ObjectID.BLACK_CHEST_4123;   // Crimson
import static net.runelite.api.ObjectID.BLACK_CHEST_4124;   // Black
import static net.runelite.api.ObjectID.BLACK_CHEST_4125;   // Purple

import static net.runelite.api.ObjectID.SILVER_CHEST;       // Red
import static net.runelite.api.ObjectID.SILVER_CHEST_4127;  // Brown
import static net.runelite.api.ObjectID.SILVER_CHEST_4128;  // Crimson
import static net.runelite.api.ObjectID.SILVER_CHEST_4129;  // Black
import static net.runelite.api.ObjectID.SILVER_CHEST_4130;  // Purple

import static net.runelite.api.ObjectID.GOLD_CHEST;         // Red
import static net.runelite.api.ObjectID.GOLD_CHEST_41213;   // Brown
import static net.runelite.api.ObjectID.GOLD_CHEST_41214;   // Crimson
import static net.runelite.api.ObjectID.GOLD_CHEST_41215;   // Black
import static net.runelite.api.ObjectID.GOLD_CHEST_41216;   // Purple

import static net.runelite.api.ItemID.BRONZE_KEY_RED;
import static net.runelite.api.ItemID.BRONZE_KEY_BROWN;
import static net.runelite.api.ItemID.BRONZE_KEY_CRIMSON;
import static net.runelite.api.ItemID.BRONZE_KEY_BLACK;
import static net.runelite.api.ItemID.BRONZE_KEY_PURPLE;

import static net.runelite.api.ItemID.STEEL_KEY_RED;
import static net.runelite.api.ItemID.STEEL_KEY_BROWN;
import static net.runelite.api.ItemID.STEEL_KEY_CRIMSON;
import static net.runelite.api.ItemID.STEEL_KEY_BLACK;
import static net.runelite.api.ItemID.STEEL_KEY_PURPLE;

import static net.runelite.api.ItemID.BLACK_KEY_RED;
import static net.runelite.api.ItemID.BLACK_KEY_BROWN;
import static net.runelite.api.ItemID.BLACK_KEY_CRIMSON;
import static net.runelite.api.ItemID.BLACK_KEY_BLACK;
import static net.runelite.api.ItemID.BLACK_KEY_PURPLE;

import static net.runelite.api.ItemID.SILVER_KEY_RED;
import static net.runelite.api.ItemID.SILVER_KEY_BROWN;
import static net.runelite.api.ItemID.SILVER_KEY_CRIMSON;
import static net.runelite.api.ItemID.SILVER_KEY_BLACK;
import static net.runelite.api.ItemID.SILVER_KEY_PURPLE;

import static net.runelite.api.ItemID.GOLD_KEY_RED;
import static net.runelite.api.ItemID.GOLD_KEY_BROWN;
import static net.runelite.api.ItemID.GOLD_KEY_CRIMSON;
import static net.runelite.api.ItemID.GOLD_KEY_BLACK;
import static net.runelite.api.ItemID.GOLD_KEY_PURPLE;

@Slf4j
@PluginDescriptor(
	name = "Shade Chests Highlighter"
)
public class ShadeChestsPlugin extends Plugin
{
	@Inject
	private ShadeChestsOverlay shadeChestsOverlay;
	@Getter
	private final HashSet<GameObject> chestsToHighlight = new HashSet<>();
	private final HashSet<Integer> chestIdsToHighlight = new HashSet<>();
	private final HashMap<Integer, Integer> keyChestsMap = new HashMap<>();
	@Inject
	private Client client;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ShadeChestsConfig config;

	@Override
	protected void startUp() throws Exception
	{
		keyChestsMap.put(BRONZE_KEY_RED, BRONZE_CHEST);
		keyChestsMap.put(BRONZE_KEY_BROWN, BRONZE_CHEST_4112);
		keyChestsMap.put(BRONZE_KEY_CRIMSON, BRONZE_CHEST_4113);
		keyChestsMap.put(BRONZE_KEY_BLACK, BRONZE_CHEST_4114);
		keyChestsMap.put(BRONZE_KEY_PURPLE, BRONZE_CHEST_4115);

		keyChestsMap.put(STEEL_KEY_RED, STEEL_CHEST);
		keyChestsMap.put(STEEL_KEY_BROWN, STEEL_CHEST_4117);
		keyChestsMap.put(STEEL_KEY_CRIMSON, STEEL_CHEST_4118);
		keyChestsMap.put(STEEL_KEY_BLACK, STEEL_CHEST_4119);
		keyChestsMap.put(STEEL_KEY_PURPLE, STEEL_CHEST_4120);

		keyChestsMap.put(BLACK_KEY_RED, BLACK_CHEST);
		keyChestsMap.put(BLACK_KEY_BROWN, BLACK_CHEST_4122);
		keyChestsMap.put(BLACK_KEY_CRIMSON, BLACK_CHEST_4123);
		keyChestsMap.put(BLACK_KEY_BLACK, BLACK_CHEST_4124);
		keyChestsMap.put(BLACK_KEY_PURPLE, BLACK_CHEST_4125);

		keyChestsMap.put(SILVER_KEY_RED, SILVER_CHEST);
		keyChestsMap.put(SILVER_KEY_BROWN, SILVER_CHEST_4127);
		keyChestsMap.put(SILVER_KEY_CRIMSON, SILVER_CHEST_4128);
		keyChestsMap.put(SILVER_KEY_BLACK, SILVER_CHEST_4129);
		keyChestsMap.put(SILVER_KEY_PURPLE, SILVER_CHEST_4130);

		keyChestsMap.put(GOLD_KEY_RED, GOLD_CHEST);
		keyChestsMap.put(GOLD_KEY_BROWN, GOLD_CHEST_41213);
		keyChestsMap.put(GOLD_KEY_CRIMSON, GOLD_CHEST_41214);
		keyChestsMap.put(GOLD_KEY_BLACK, GOLD_CHEST_41215);
		keyChestsMap.put(GOLD_KEY_PURPLE, GOLD_CHEST_41216);

		overlayManager.add(shadeChestsOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		chestsToHighlight.clear();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOADING)
		{
			chestsToHighlight.clear();
		}
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event)
	{
		final ItemContainer itemContainer = event.getItemContainer();
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}
		chestIdsToHighlight.clear();
		for (Map.Entry<Integer, Integer> keyChest : keyChestsMap.entrySet())
		{
			if (itemContainer.contains(keyChest.getKey()))
			{
				chestIdsToHighlight.add(keyChest.getValue());
			}
		}
		Iterator<GameObject> chestIterator = chestsToHighlight.iterator();
		while (chestIterator.hasNext())
		{
			Integer chestId = chestIterator.next().getId();
			if (!chestIdsToHighlight.contains(chestId))
			{
				chestIterator.remove();
			}
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject gameObject = event.getGameObject();
		if (chestIdsToHighlight.contains(gameObject.getId()))
		{
			chestsToHighlight.add(gameObject);
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		chestsToHighlight.remove(event.getGameObject());
	}

	@Provides
	ShadeChestsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ShadeChestsConfig.class);
	}
}
