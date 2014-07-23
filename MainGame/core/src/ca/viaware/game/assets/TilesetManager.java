package ca.viaware.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

public class TilesetManager {

    private static HashMap<String, Tileset> tilesets;

    public static Tileset getTileset(String name) {
        return tilesets.get(name);
    }

    public static void loadTileset(String name, FileHandle imageFile) {
        if (tilesets == null) tilesets = new HashMap<String, Tileset>();

        System.out.println("Loading tileset " + name + " at " + imageFile.path());

        try {
            Texture texture = new Texture(imageFile);
            Tileset tileset = new Tileset(texture);
            DataInputStream input = new DataInputStream(Gdx.files.internal(imageFile.pathWithoutExtension() + ".regions").read());

            while (true) {
                int[] read = new int[4];
                for (int i = 0; i < read.length; i++) {
                    int r = input.readShort();
                    if (r == -100) { //TODO This is stupid
                        input.close();
                        tilesets.put(name, tileset);
                        System.out.println("Done loading tileset.");
                        return;
                    }
                    read[i] = r;
                }
                String tileName = input.readUTF();
                System.out.println("Adding region " + tileName + "(" + read[0] + "," + read[1] + ")(" + read[2] + "," + read[3] + ")");
                tileset.addRegion(tileName, read[0], read[1], read[2], read[3]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
