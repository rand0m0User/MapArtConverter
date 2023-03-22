package ditherer;

import java.awt.image.BufferedImage;

public class Ditherer {

    private ColorPalette palette;

    public Ditherer(ColorPalette palette) {
        this.setPalette(palette);
    }

    public ColorPalette getPalette() {
        return palette;
    }

    public void setPalette(ColorPalette palette) {
        this.palette = palette;
    }

    public void dither(BufferedImage img) {
        int h = img.getHeight();
        int w = img.getWidth();

        //dividing by two constants elimination
        float diva = (float) 7 / 16;
        float divb = (float) 1 / 16;
        float divc = (float) 3 / 16;
        float divd = 5 / 16;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                VectorRGB current_color = new VectorRGB(img.getRGB(x, y));
                VectorRGB closest_match = palette.getClosestMatch(current_color);
                VectorRGB error = current_color.subtract(closest_match);

                img.setRGB(x, y, closest_match.toRGB());

                if (!(x == w - 1)) {
                    img.setRGB(x + 1, y,
                            ((new VectorRGB(img.getRGB(x + 1, y)).add(error.scalarMultiply(diva)))
                            .clip(0, 255).toRGB()));

                    if (!(y == h - 1)) {
                        img.setRGB(x + 1, y + 1,
                                ((new VectorRGB(img.getRGB(x + 1, y + 1)).add(error.scalarMultiply(divb)))
                                .clip(0, 255).toRGB()));
                    }
                }

                if (!(y == h - 1)) {

                    img.setRGB(x, y + 1,
                            ((new VectorRGB(img.getRGB(x, y + 1)).add(error.scalarMultiply(divc)))
                            .clip(0, 255).toRGB()));

                    if (!(x == 0)) {
                        img.setRGB(x - 1, y + 1, ((new VectorRGB(img.getRGB(x - 1, y + 1))
                                .add(error.scalarMultiply(divd)).clip(0, 255).toRGB())));

                    }
                }
            }
        }
    }
}
