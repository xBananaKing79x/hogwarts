package ru.hogwarts.school_test.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school_test.model.Avatar;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.repositories.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("${student.avatars.dir.path}")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student id: {}", studentId);
        try {
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                logger.warn("Student with id {} not found for avatar upload", studentId);
                throw new IllegalArgumentException("Student with id " + studentId + " not found");
            }

            Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (InputStream is = file.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024);) {
                bis.transferTo(bos);
            }
            Avatar avatar = findAvatar(studentId);
            avatar.setAvatar(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(file.getSize());
            avatar.setMediaType(file.getContentType());
            avatar.setPreview(generateAvatarPreview(filePath));
            avatarRepository.save(avatar);
            logger.debug("Successfully uploaded avatar for student id: {}", studentId);
        } catch (IOException e) {
            logger.error("IO error occurred while uploading avatar for student id {}: {}", studentId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while uploading avatar for student id {}: {}", studentId, e.getMessage());
            throw e;
        }
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    private String getExtension(String fileName) {
        logger.debug("Getting file extension for file name: {}", fileName);
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            logger.warn("File name {} has no extension, returning empty string", fileName);
            return "";
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("File extension for {} is: {}", fileName, extension);
        return extension;
    }

    private byte[] generateAvatarPreview(Path filePath) throws IOException {
        logger.info("Was invoked method for generate avatar preview from file: {}", filePath);
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                logger.warn("Could not read image from file: {}", filePath);
            }
            int heigth = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, heigth, image.getType());
            Graphics2D graphics = image.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            logger.debug("Successfully generated avatar preview of size {} bytes", baos.size());
            return baos.toByteArray();
        }
        catch (IOException e) {
            logger.error("IO error occurred while generating avatar preview from file {}: {}", filePath, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while generating avatar preview from file {}: {}", filePath, e.getMessage());
            throw new IOException("Error generating avatar preview", e);
        }
    }
}
