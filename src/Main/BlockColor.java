package Main;

import java.awt.Color;

public class BlockColor {

    private int block; //waste of 3 bytes
    private int meta = 0; //waste of 3 and a half bytes
    private int r; //waste of 3 bytes
    private int g; //waste of 3 bytes
    private int b; //waste of 3 bytes
    private boolean extraMeta = false;

    public BlockColor(int tmpBlock, int tmpR, int tmpG, int tmpB) {
        this.block = tmpBlock;
        this.r = tmpR;
        this.g = tmpG;
        this.b = tmpB;
    }

    public BlockColor(int tmpBlock, int tmpMeta, int tmpR, int tmpG, int tmpB) {
        this.block = tmpBlock;
        this.meta = tmpMeta;
        this.r = tmpR;
        this.g = tmpG;
        this.b = tmpB;
    }

    public int getDif(Color color) {
        int difR = color.getRed() - this.r;
        int difG = color.getGreen() - this.g;
        int difB = color.getBlue() - this.b;
        if (difR < 0) {
            difR *= -1;
        }

        if (difG < 0) {
            difG *= -1;
        }

        if (difB < 0) {
            difB *= -1;
        }

        return difR + difG + difB;
    }

    public int getBlock() {
        return this.block;
    }

    public int getMeta() {
        return this.meta;
    }

    public Color Color() {
        return new Color(r, g, b);
    }
}
