package apidemo.demo.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {

    private final Path storageFolder = Paths.get("src/main/webapp/WEB-INF/uploads");

    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("Hhdhd");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
//            file must be <= 5Mb
            float fileSizeMb = file.getSize() / 1_000_000;
            if (fileSizeMb > 0.5f) {
                throw new RuntimeException("File must b <= 5 Mb");
            }
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generateFileName = UUID.randomUUID().toString().replace("-", "");
            generateFileName = generateFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generateFileName)).normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generateFileName;
        } catch (
                Exception e) {
            throw new RuntimeException("Failed to store file ", e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public byte[] readFileContent(String fileName) {
        return new byte[0];
    }

    @Override
    public void deleteAllFiles() {

    }
}
