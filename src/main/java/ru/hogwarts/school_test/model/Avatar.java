package ru.hogwarts.school_test.model;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

import java.util.Arrays;
import java.util.Objects;

public class Avatar {
    @Id
    private long avatarId;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;
    @OneToOne
    private Student student;

    public Avatar(long avatarId, String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    private long getId() {
        return avatarId;
    }

    private void setId(long id) {
        this.avatarId = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setMediaType(String contentType) {
        this.mediaType = mediaType;
    }
    public void setFileSize(long size){
        this.fileSize = fileSize;
    }

    public void setAvatar(Student student) {
    }

    public void setPreview(byte[] bytes) {
        this.data = data;
    }

    public String getMediaType() {
        return this.mediaType = mediaType;
    }

    public byte[] getPreview() {
        return data = data;
    }

    public long getFileSize() {
        return this.fileSize = fileSize;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    @Override
    public int hashCode() {
        int result = Objects.hash(avatarId, filePath, mediaType, fileSize, student);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + avatarId +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", fileSize=" + fileSize +
                ", data=" + Arrays.toString(data) +
                ", student=" + student +
                '}';
    }
}
