package dslToGame;

import graphic.Animation;
import graphic.textures.TextureHandler;
import semanticAnalysis.types.DSLTypeAdapter;

public class AnimationBuilder {
    public static int frameTime = 5;

    @DSLTypeAdapter(t = Animation.class)
    public static Animation buildAnimation(String path) {
        return new Animation(TextureHandler.getInstance().getTexturePaths(path), frameTime);
    }

    @DSLTypeAdapter(t = Animation.class)
    public static Animation buildAnimation(String path, int frameTime) {
        return new Animation(TextureHandler.getInstance().getTexturePaths(path), frameTime);
    }

    @DSLTypeAdapter(t = Animation.class)
    public static Animation buildAnimation(String path, int frameTime, boolean looping) {
        return new Animation(
                TextureHandler.getInstance().getTexturePaths(path), frameTime, looping);
    }
}
