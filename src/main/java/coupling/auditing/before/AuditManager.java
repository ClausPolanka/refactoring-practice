package coupling.auditing.before;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class AuditManager {

    private int maxEntriesPerFile;
    private String directoryName;

    public AuditManager(int maxEntriesPerFile, String directoryName) {
        this.maxEntriesPerFile = maxEntriesPerFile;
        this.directoryName = directoryName;
    }

    public void addRecord(String visitorName, LocalDateTime timeOfVisit) throws IOException {
        try (Stream<Path> filePaths = Files.list(Paths.get(directoryName))) {
            List<File> files = sortByIndex(filePaths);

            String newRecord = visitorName + ";" + timeOfVisit;

            if (files.isEmpty()) {
                Path newFilePath = Paths.get(directoryName, "audit_1.txt");
                Files.write(newFilePath, newRecord.getBytes());
                return;
            }
            File file = files.get(files.size() - 1);
            List<String> lines = Files.readAllLines(Paths.get(file.getFilePath()));
            System.out.println(lines);

            if (lines.size() < maxEntriesPerFile) {
                lines.add(newRecord);
                Files.write(
                        Paths.get(file.getFilePath()),
                        String.join(System.lineSeparator(), lines).getBytes(),
                        StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                String newIndex = String.valueOf(parseInt(file.getFileIndex()) + 1);
                String newName = "audit_" + newIndex + ".txt";
                Path newFilePath = Paths.get(directoryName, newName);
                Files.write(newFilePath, newRecord.getBytes());
            }
        }
    }

    private List<File> sortByIndex(Stream<Path> filePaths) {
        return filePaths
                .map(path -> new File(
                        path.getFileName()
                                .toString()
                                .split("_")[1]
                                .replaceAll(".txt", ""),
                        path.toString()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
        int maxEntriesPerFile = 3;
        String directoryName = "audits";
        AuditManager am = new AuditManager(maxEntriesPerFile, directoryName);
        am.addRecord("Claus", LocalDateTime.now());
    }

}
