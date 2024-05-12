import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit Tests for ImageManipulator
 */
public class ImageManipulatorTest {
    @Test
    public void loadImage() throws Exception {
        // arrange
        Img expected = LoadImage("testresources/testImage.jpg");

        // act
        Img actual = ImageManipulator.LoadImage("testresources/testImage.jpg");

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void saveImage() throws Exception {
        File currentDirectory = new File(".");
        System.out.println(currentDirectory.getAbsolutePath());

        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/testImage.png");
        String savePath = "testresources/savedImage.png";

        // act
        ImageManipulator.SaveImage(expected, savePath);
        Img actual = LoadImage(savePath);

        // assert
        assertTrue(CompareImages(expected, actual));

        File f = new File(savePath);
        f.delete();
    }

    @Test
    public void convertToGrayScale() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/grayscale.png");

        // act
        Img actual = ImageManipulator.ConvertToGrayScale(start);
        actual.Save("png", "testresources/grayscale-actual.png");

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void invertImage() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/invert.png");

        // act
        Img actual = ImageManipulator.InvertImage(start);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void convertToSepia() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/sepia.png");

        // act
        Img actual = ImageManipulator.ConvertToSepia(start);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void convertToBW() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/bw.png");

        // act
        Img actual = ImageManipulator.ConvertToBW(start);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void rotateImage() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/rotate.png");

        // act
        Img actual = ImageManipulator.RotateImage(start);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void instagramFilter() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/instagram.png");

        // act
        Img actual = ImageManipulator.InstagramFilter(start);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void setHue() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/hue.png");

        // act
        Img actual = ImageManipulator.SetHue(start, 200);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void setSaturation() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/saturation.png");

        // act
        Img actual = ImageManipulator.SetSaturation(start, .2);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    @Test
    public void setLightness() throws Exception {
        // arrange
        Img start = LoadImage("testresources/testImage.jpg");
        Img expected = LoadImage("testresources/lightness.png");

        // act
        Img actual = ImageManipulator.SetLightness(start, .5);

        // assert
        assertTrue(CompareImages(expected, actual));
    }

    private Img LoadImage(String path) throws IOException {
        return new Img(path);
    }

    /**
     * Compares actual and expected images by comparing each individual pixel
     * in the actual image to the corresponding pixel in the expected image
     * @param expected expected image
     * @param actual actual image
     * @return true if the images are the same, false if they are different
     */
    private boolean CompareImages(Img expected, Img actual) {
        if (actual.GetHeight() != expected.GetHeight()
            || actual.GetWidth() != expected.GetWidth()) {
            return false;
        }

        for (int i = 0; i < actual.GetHeight(); i++) {
            for (int j = 0; j < actual.GetWidth(); j++) {
                if (!ComparePixels(expected.GetRGB(j, i), actual.GetRGB(j, i))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Compare pixels, allow up to a 1 pt difference to account for rounding.
     * @param expected expected rgb values
     * @param actual actual rgb values
     * @return true if they are the same, false if they are different
     */
    private boolean ComparePixels(RGB expected, RGB actual) {
        if (Math.abs(expected.GetRed() - actual.GetRed()) > 1
            || Math.abs(expected.GetGreen() - actual.GetGreen()) > 1
            || Math.abs(expected.GetBlue() - actual.GetBlue()) > 1) {
           return false;
        }

        return true;
    }
}