package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageSavingUtils {

    private static final Logger logger = LogManager.getLogger();

    public static void saveImage(Part imagePart, String imagesFolderRelativePath, String filePath) {
        try (InputStream imageInputStream = imagePart.getInputStream()) {
            File imageFile = new File(imagesFolderRelativePath + filePath);
            if (!imageFile.exists()) {
                if (imageFile.getParentFile().mkdirs()) logger.debug("Image storage directory created.");
                if (imageFile.createNewFile()) logger.debug("Image file created on path " + filePath);
            }
            Files.copy(imageInputStream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Thread.sleep(500);  //Time for image to copy (otherwise it won't be displayed when redirecting).
        } catch (IOException e) {
            logger.error("Can not create/write into image file. Image saving exception."
                    + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            logger.error("Interrupted exception on waiting until file will load on server.");
        }
    }

}
