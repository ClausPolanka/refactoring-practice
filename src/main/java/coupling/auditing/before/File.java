package coupling.auditing.before;

public class File {
    private String fileIndex;
    private String filePath;

    public File(String fileIndex, String filePath) {
        this.fileIndex = fileIndex;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileIndex() {
        return fileIndex;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileIndex='" + fileIndex + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
