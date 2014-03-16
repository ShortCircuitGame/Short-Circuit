package shortcircui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class ShortCircuit implements ApplicationListener {
	TiledMap map;
	OrthographicCamera camera;
	OrthoCamController cameraController;
	HexagonalTiledMapRenderer renderer;
	Texture hexture;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 480, 480);
		camera.update();

		cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(cameraController);

		hexture = new Texture(Gdx.files.internal("data/hexes.png"));
		TextureRegion[][] hexes = TextureRegion.split(hexture, 112, 97);
		map = new TiledMap();
		MapLayers layers = map.getLayers();
		TiledMapTile[] tiles = new TiledMapTile[3];
		tiles[0] = new StaticTiledMapTile(new TextureRegion(hexes[0][0]));
		tiles[1] = new StaticTiledMapTile(new TextureRegion(hexes[0][1]));
		tiles[2] = new StaticTiledMapTile(new TextureRegion(hexes[1][0]));

		int width = 15;
		int height = 10;
		for (int l = 0; l < 1; l++) {
			TiledMapTileLayer layer = new TiledMapTileLayer(width, height, 112, 97);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int id = (int) (Math.random() * 3);
					Cell cell = new Cell();
					cell.setTile(tiles[id]);
					layer.setCell(x, y, cell);
				}
			}
			layers.add(layer);
		}

		renderer = new HexagonalTiledMapRenderer(map);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		hexture.dispose();
		map.dispose();
	}

	@Override
	public void resize(int width, int height) {
		this.camera.viewportWidth = width;
		this.camera.viewportHeight = height;
		this.camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
