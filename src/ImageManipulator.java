import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Static utility class that is responsible for transforming the images.
 * Each function (or at least most functions) take in an Image and return
 * a transformed image.
 */
public class ImageManipulator {
    /**
     * Loads the image at the given path
     *
     * @param path path to image to load
     * @return an Img object that has the given image loaded
     * @throws IOException
     */
    public static Img LoadImage(String path) throws IOException {
        Img img = new Img(path);
        return img;
    }

    /**
     * Saves the image to the given file location
     *
     * @param image image to save
     * @param path  location in file system to save the image
     * @throws IOException
     */
    public static void SaveImage(Img image, String path) throws IOException {
        String extension = path.substring(path.lastIndexOf(".") + 1);
        image.Save(extension, path);
    }

    /**
     * Converts the given image to grayscale (black, white, and gray). This is done
     * by finding the average of the RGB channel values of each pixel and setting
     * each channel to the average value.
     *
     * @param image image to transform
     * @return the image transformed to grayscale
     */
    public static Img ConvertToGrayScale(Img image) {
        for (int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x, y).GetRed();
                int green = image.GetRGB(x, y).GetGreen();
                int blue = image.GetRGB(x, y).GetBlue();
                int avg = (red + green + blue) / 3;
                RGB rgb = new RGB(avg, avg, avg);
                image.SetRGB(x, y, rgb);
            }
        }

        return image;
    }

    /**
     * Inverts the image. To invert the image, for each channel of each pixel, we get
     * its new value by subtracting its current value from 255. (r = 255 - r)
     *
     * @param image image to transform
     * @return image transformed to inverted image
     */
    public static Img InvertImage(Img image) {
        for (int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x, y).GetRed();
                int green = image.GetRGB(x, y).GetGreen();
                int blue = image.GetRGB(x, y).GetBlue();
                int nRed = 255 - red;
                int nGreen = 255 - green;
                int nBlue = 255 - blue;
                RGB rgb = new RGB(nRed, nGreen, nBlue);
                image.SetRGB(x, y, rgb);
            }
        }
        return image;
    }

    /**
     * Converts the image to sepia. To do so, for each pixel, we use the following equations
     * to get the new channel values:
     * r = .393r + .769g + .189b
     * g = .349r + .686g + .168b
     * b = 272r + .534g + .131b
     *
     * @param image image to transform
     * @return image transformed to sepia
     */
    public static Img ConvertToSepia(Img image) {
        for (int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x, y).GetRed();
                int green = image.GetRGB(x, y).GetGreen();
                int blue = image.GetRGB(x, y).GetBlue();
                int nRed = (int) ((.393 * red) + (.769 * green) + (.189 * blue));
                int nGreen = (int) ((.349 * red) + (.686 * green) + (.168 * blue));
                int nBlue = (int) ((.272 * red) + (.534 * green) + (.131 * blue));
                RGB rgb = new RGB(nRed, nGreen, nBlue);
                image.SetRGB(x, y, rgb);
            }
        }
        return image;
    }

    /**
     * Creates a stylized Black/White image (no gray) from the given image. To do so:
     * 1) calculate the luminance for each pixel. Luminance = (.299 r^2 + .587 g^2 + .114 b^2)^(1/2)
     * 2) find the median luminance
     * 3) each pixel that has luminance >= median_luminance will be white changed to white and each pixel
     * that has luminance < median_luminance will be changed to black
     *
     * @param image image to transform
     * @return black/white stylized form of image
     */
    public static Img ConvertToBW(Img image) {
        ArrayList<Double> medianish = new ArrayList<Double>();
        for (int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x, y).GetRed();
                int green = image.GetRGB(x, y).GetGreen();
                int blue = image.GetRGB(x, y).GetBlue();
                double luminance = Math.sqrt(.299 * red * red + .587 * green * green + .114 * blue * blue);
                medianish.add(luminance);
            }
        }
        medianish.sort(Comparator.naturalOrder());
        double middle = medianish.get(medianish.size() / 2);
        for (int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x, y).GetRed();
                int green = image.GetRGB(x, y).GetGreen();
                int blue = image.GetRGB(x, y).GetBlue();
                double luminance = Math.sqrt(.299 * red * red + .587 * green * green + .114 * blue * blue);
                if (luminance >= middle) {
                    RGB rgb = new RGB(255, 255, 255);
                    image.SetRGB(x, y, rgb);
                } else {
                    RGB rgb = new RGB(0, 0, 0);
                    image.SetRGB(x, y, rgb);
                }
            }
        }
        return image;
    }

    /**
     * Rotates the image 90 degrees clockwise.
     *
     * @param image image to transform
     * @return image rotated 90 degrees clockwise
     */
    public static Img RotateImage(Img image) {
        Img rotation = new Img(image.GetHeight(), image.GetWidth());
        for (int y = 0; y < image.GetHeight(); y++) {
            for (int x = 0; x < image.GetWidth(); x++) {
                RGB rgb = image.GetRGB(x, y);
                rotation.SetRGB(image.GetHeight() - 1 - y, x, rgb);
            }
        }
        return rotation;
    }

        /**
         * Applies an Instagram-like filter to the image. To do so, we apply the following transformations:
         * 1) We apply a "warm" filter. We can produce warm colors by reducing the amount of blue in the image
         *      and increasing the amount of red. For each pixel, apply the following transformation:
         *          r = r * 1.2
         *          g = g
         *          b = b / 1.5
         * 2) We add a vignette (a black gradient around the border) by combining our image with an
         *      an image of a halo (you can see the image at resources/halo.png). We take 65% of our
         *      image and 35% of the halo image. For example:
         *          r = .65 * r_image + .35 * r_halo
         * 3) We add decorative grain by combining our image with a decorative grain image
         *      (resources/decorative_grain.png). We will do this at a .95 / .5 ratio.
         * @param image image to transform
         * @return image with a filter
         * @throws IOException
         */
    public static Img InstagramFilter(Img image) throws IOException {
        Img halo = new Img("C:\\Users\\lucym\\IdeaProjects\\project-5-image-manipulation-LucyMartenstein\\resources\\halo.png");
        Img grain = new Img("C:\\Users\\lucym\\IdeaProjects\\project-5-image-manipulation-LucyMartenstein\\resources\\decorative_grain.png");
        double haloHeight = (double)(halo.GetHeight())/ image.GetHeight();
        double haloWidth = (double)(halo.GetWidth())/image.GetWidth();
        double grainHeight = (double)(grain.GetHeight())/ image.GetHeight();
        double grainWidth = (double)(grain.GetWidth())/ image.GetWidth();
        for(int x = 0; x < image.GetWidth(); x++){
            for(int y = 0; y < image.GetHeight(); y++){
                int red = image.GetRGB(x,y).GetRed();
                int green = image.GetRGB(x,y).GetGreen();
                int blue = image.GetRGB(x,y).GetBlue();
                int nRed = (int) (red * 1.2);
                int nGreen =  (int) (green);
                int nBlue =  (int) (blue/1.5);
                RGB rgb = new RGB(nRed, nGreen, nBlue);
                image.SetRGB(x, y, rgb);

                int haloHeight2 = (int)(y*haloHeight);
                int haloWidth2 = (int)(x*haloWidth);
                RGB haloPixel = halo.GetRGB(haloWidth2, haloHeight2);
                int hRed = haloPixel.GetRed();
                int hGreen = haloPixel.GetGreen();
                int hBlue = haloPixel.GetBlue();
                int red2 = (int)(.65 * image.GetRGB(x,y).GetRed() + .35 * hRed);
                int green2 = (int)(.65 * image.GetRGB(x,y).GetGreen() + .35 * hGreen);
                int blue2 = (int)(.65 * image.GetRGB(x,y).GetBlue() + .35 * hBlue);
                RGB withHalo = new RGB(red2, green2, blue2);
                image.SetRGB(x, y, withHalo);

                int grainHeight2 = (int)(y*grainHeight);
                int grainWidth2 = (int)(x*grainWidth);
                RGB grainPixel = grain.GetRGB(grainWidth2, grainHeight2);
                int gRed = grainPixel.GetRed();
                int gGreen = grainPixel.GetGreen();
                int gBlue = grainPixel.GetBlue();
                int red3 = (int)(.95 * image.GetRGB(x,y).GetRed() + .05 * gRed);
                int green3 = (int)(.95 * image.GetRGB(x,y).GetGreen() + .05 * gGreen);
                int blue3 = (int)(.95 * image.GetRGB(x,y).GetBlue() + .05 * gBlue);
                RGB withGrain = new RGB(red3, green3, blue3);
                image.SetRGB(x, y, withGrain);
            }
        }
        return image;
    }

    /**
     * Sets the given hue to each pixel image. Hue can range from 0 to 360. We do this
     * by converting each RGB pixel to an HSL pixel, Setting the new hue, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param hue amount of hue to add
     * @return image with added hue */

    public static Img SetHue(Img image, int hue) {
        for(int x = 0; x < image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                int red = image.GetRGB(x,y).GetRed();
                int green = image.GetRGB(x,y).GetGreen();
                int blue = image.GetRGB(x,y).GetBlue();
                HSL hsl = new RGB(red, green, blue).ConvertToHSL();
                hsl.SetHue(hue);
                RGB nRGB = hsl.GetRGB();
                image.SetRGB(x,y, nRGB);
            }
        }
        return image;
    }

        /**
         * Sets the given saturation to the image. Saturation can range from 0 to 1. We do this
         * by converting each RGB pixel to an HSL pixel, setting the new saturation, and then
         * converting each pixel back to an RGB pixel.
         * @param image image to transform
         * @param saturation amount of saturation to add
         * @return image with added hue */

        public static Img SetSaturation(Img image, double saturation) {
            for(int x = 0; x < image.GetWidth(); x++) {
                for (int y = 0; y < image.GetHeight(); y++) {
                    int red = image.GetRGB(x,y).GetRed();
                    int green = image.GetRGB(x,y).GetGreen();
                    int blue = image.GetRGB(x,y).GetBlue();
                    HSL hsl = new RGB(red, green, blue).ConvertToHSL();
                    hsl.SetSaturation(saturation);
                    RGB nRGB = hsl.GetRGB();
                    image.SetRGB(x,y, nRGB);
                }
            }
            return image;
        }

        /**
         * Sets the lightness to the image. Lightness can range from 0 to 1. We do this
         * by converting each RGB pixel to an HSL pixel, setting the new lightness, and then
         * converting each pixel back to an RGB pixel.
         * @param image image to transform
         * @param lightness amount of hue to add
         * @return image with added hue */

        public static Img SetLightness(Img image, double lightness) {
            for(int x = 0; x < image.GetWidth(); x++) {
                for (int y = 0; y < image.GetHeight(); y++) {
                    int red = image.GetRGB(x,y).GetRed();
                    int green = image.GetRGB(x,y).GetGreen();
                    int blue = image.GetRGB(x,y).GetBlue();
                    HSL hsl = new RGB(red, green, blue).ConvertToHSL();
                    hsl.SetLightness(lightness);
                    RGB nRGB = hsl.GetRGB();
                    image.SetRGB(x,y, nRGB);
                }
            }
            return image;
        }
    }
