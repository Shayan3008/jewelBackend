package com.jewelbackend.backend.setup.jobs;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.setup.services.BaseService;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@EnableScheduling
public class ImageMigrateJob extends BaseService {
    @Value("${image.storage.directory}")
    private String imageDirPath;
    public ImageMigrateJob(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

//    @Scheduled(cron = "*/10 * * * * *")
    public void uploadItemImageToDir() throws IOException {
        HelperUtils.logMessage(Level.INFO, "Running Migration Job");
        var id = 12525;
        var items = getDaoFactory().getItemDao().getAllItemWhoHaveImages();
        for (var item : items) {
            if (item.getItemImage() == null)
                continue;
            String fileName = UUID.randomUUID() + "_" + item.getDesignNo() + ".jpg";
            // Create the directory if it does not exist
            Path uploadPath = Paths.get(imageDirPath);
            if (!Files.exists(uploadPath)) {
                HelperUtils.logMessage(Level.ERROR, "DIRECTORY NOT FOUND");
                return;
            }

            // Generate a unique filename

            Path filePath = Paths.get(imageDirPath, fileName);


            // Convert byte array to BufferedImage
            ByteArrayInputStream bais = new ByteArrayInputStream(item.getItemImage());
            BufferedImage bufferedImage = ImageIO.read(bais);

            // Save the BufferedImage to the specified path
            File outputFile = new File(filePath.toString());
            ImageIO.write(bufferedImage, "jpg", outputFile);
            item.setItemImagePath(filePath.toString());
            item.setItemImage(null);
            getDaoFactory().getItemDao().save(item);
        }
    }
}
