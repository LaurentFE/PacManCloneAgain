package fr.LaurentFE.pacManCloneAgain.model;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameMapTests {

    @Test
    public void GameMap_loadMap_mapIsIdenticalToFile() {
        final GameMap gameMap = new GameMap(GameConfig.DEFAULT_MAP_TILE_HEIGHT, GameConfig.DEFAULT_MAP_TILE_WIDTH);
        gameMap.loadMap(GameConfig.DEFAULT_MAP_PATH);
        final Path tileMapFilePath = Path.of("./src/test/resources/levelTest");
        try {
            final List<String> tileMap = Files.readAllLines(tileMapFilePath);
            StringBuilder map = new StringBuilder();
            for (String tile : tileMap) {
                map.append(tile);
                map.append('\n');
            }
            assert(map.toString().equals(gameMap.toString()));
        } catch (IOException e) {
            System.err.println("Failed to load levelTest file");
            assert(false);
        }
    }
}
