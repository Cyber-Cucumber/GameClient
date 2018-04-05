package ru.torment.client;

import ru.torment.client.map.MapParser;
import ru.torment.client.tileset.TilesetParser;

public class Main
{
	public static void main( String[] args )
	{
//		StartWindow swingGUI = new StartWindow();
//		swingGUI.setVisible(true);

//		SwingUtilities.invokeLater(
//				new Runnable()
//				{
//					public void run()
//					{
//					}
//				});
		TilesetParser.parseTileset("sc_jungle_world.tsx");
		TilesetParser.parseTileset("sc_jungle_world_water_mud.tsx");
		MapParser.parseMap("sc_map_test.tmx");
		new GameFrame();
	}
}
