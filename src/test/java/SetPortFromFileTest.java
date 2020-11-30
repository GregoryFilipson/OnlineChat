import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static client.Main.setPortFromFile;

public class SetPortFromFileTest {

    @Test
    public void correctPortShouldBeRead() {
        File file = new File("settings.txt");
        int result = setPortFromFile(file);
        Assert.assertEquals(4444, result);
    }

    @Test(expected = NumberFormatException.class)
    public void incorrectFileShouldThrowNumberFormatExeption() {
        File file = new File("settingsTest.txt");
        int result = setPortFromFile(file);
        Assert.assertEquals(NumberFormatException.class, result);
    }
}
