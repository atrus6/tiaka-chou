package org.worldsproject.games.taikachou;

import com.cubes.BlockTerrainControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.cubes.test.CubesTestAssets;
import com.cubes.test.blocks.Block_Grass;
import com.cubes.test.blocks.Block_Stone;
import com.cubes.test.blocks.Block_Wood;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.worldsproject.games.taikachou.pathing.Pathing;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static final int SIZE = 5;
    public static final int CELL = 10;
    private BlockTerrainControl blockTerrain;
    private Pathing paths;
    private float totalTime = 0;
    Spatial spider;
    ArrayList<Cell> spiderPath = new ArrayList<Cell>();

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main() {
        Logger.getLogger( "" ).setLevel( Level.WARNING );
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - Tutorial");
        this.setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        CubesTestAssets.registerBlocks();
        CubesSettings cs = new CubesSettings(this);
        cs.setBlockSize(1);
        cs.setBlockMaterial(CubesTestAssets.getSettings(this).getBlockMaterial());

        //This is your terrain, it contains the whole
        //block world and offers methods to modify it
        blockTerrain = new BlockTerrainControl(cs, new Vector3Int(15, 2, 15));

        for (int i = 0; i < SIZE + 1; i++) {
            blockTerrain.setBlockArea(new Vector3Int(0, 0, i * CELL), new Vector3Int(CELL * SIZE, 8, 1), Block_Stone.class);
            blockTerrain.setBlockArea(new Vector3Int(i * CELL, 0, 0), new Vector3Int(1, 8, CELL * SIZE), Block_Stone.class);
        }

        blockTerrain.setBlockArea(new Vector3Int(0, 0, 0), new Vector3Int(CELL * SIZE, 1, CELL * SIZE), Block_Wood.class);
        blockTerrain.setBlockArea(new Vector3Int(0, 15, 0), new Vector3Int(5, 1, 1), Block_Wood.class);
        blockTerrain.setBlockArea(new Vector3Int(0, 15, 0), new Vector3Int(1, 5, 1), Block_Stone.class);
        blockTerrain.setBlockArea(new Vector3Int(0, 15, 0), new Vector3Int(1, 1, 5), Block_Grass.class);
        Maze m = new Maze();
        ArrayList<Edge> list = m.generate(SIZE, SIZE);
        paths = new Pathing(list);

        for (Edge e : list) {
            //Determining the direction of the hallway.
            int x = (e.getStartCell().x - e.getEndCell().x);
            int y = (e.getStartCell().y - e.getEndCell().y);

            //Since we can't "negative" volumes we have to pick the best place to start.
            Vector3Int start = null;
            Vector3Int volume = null;
            if (x == -1 && y == 0) {
                start = new Vector3Int(e.getEndCell().x * CELL, 1, e.getStartCell().y * CELL + 1);
                volume = new Vector3Int(1, 7, CELL - 1);
            } else if (x == 1 && y == 0) {
                start = new Vector3Int(e.getStartCell().x * CELL, 1, e.getStartCell().y * CELL + 1);
                volume = new Vector3Int(1, 7, CELL - 1);
            } else if (x == 0 && y == -1) {
                start = new Vector3Int(e.getStartCell().x * CELL + 1, 1, e.getEndCell().y * CELL);
                volume = new Vector3Int(CELL - 1, 7, 1);
            } else if (x == 0 && y == 1) {
                start = new Vector3Int(e.getStartCell().x * CELL + 1, 1, e.getStartCell().y * CELL);
                volume = new Vector3Int(CELL - 1, 7, 1);
            }

            blockTerrain.removeBlockArea(start, volume);
        }

        //The terrain is a jME-Control, you can add it
        //to a node of the scenegraph to display it
        Node terrainNode = new Node();
        terrainNode.addControl(blockTerrain);
        rootNode.attachChild(terrainNode);

        addSpider();
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        cam.setLocation(new Vector3f(-10, 80, 16));
        cam.lookAtDirection(new Vector3f(1, -0.56f, -1), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(100);
        
        createHUD();
    }

    private void addSpider() {
        spider = assetManager.loadModel("Models/Spider/spider/Spider.j3o");
        spider.setLocalTranslation(0, 10, 0);
        centerInCell(spider, 0, 0);
        spider.setLocalScale(3);
        rootNode.attachChild(spider);
    }

    private void moveSpider() {
        int x = ((int) spider.getLocalTranslation().x) / CELL;
        int y = ((int) spider.getLocalTranslation().z) / CELL;

        Cell next = paths.getRandomDirection(new Cell(x, y));
        centerInCell(spider, next.x, next.y);
    }
    
    private void directedMove() {
        Random r = new Random();
        int x = r.nextInt(SIZE);
        int y = r.nextInt(SIZE);
        
        int curX = ((int) spider.getLocalTranslation().x) / CELL;
        int curY = ((int) spider.getLocalTranslation().z) / CELL;
        spiderPath = paths.getPath(new Cell(curX, curY), new Cell(x, y));
    }

    private void centerInCell(Spatial s, int x, int y) {
        BoundingBox b = (BoundingBox) s.getWorldBound();
        x = x * CELL;
        y = y * CELL;
        x += CELL/2;
        y += CELL/2;
        s.setLocalTranslation(x, 2, y);
    }
    
    private void createHUD() {
        Picture drawingLayer = new Picture("Magic Area");
        
        drawingLayer.setImage(assetManager, "Textures/cyan.png", false);
        drawingLayer.setWidth(settings.getWidth());
        drawingLayer.setHeight(settings.getHeight());
        
        guiNode.attachChild(drawingLayer);
    }

    @Override
    public void simpleUpdate(float tpf) {
        totalTime += tpf;

        if (totalTime >= 1) {
            totalTime = 0;
            if(spiderPath.isEmpty()) {
                directedMove();
            } else {
                Cell next = spiderPath.remove(0);
                centerInCell(spider, next.x, next.y);
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
