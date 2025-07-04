package fileService;

import data.ClassifiedData;
import data.Setup;

import java.io.*;
import java.util.List;

public class DataWriter {

    public void write(ClassifiedData data, Setup setup) {
        writeToFile("integers.txt", data.integers(), setup);
        writeToFile("floats.txt", data.floats(), setup);
        writeToFile("strings.txt", data.strings(), setup);
    }

    private void writeToFile(String fileName, List<String> content, Setup setup) {
        if (content.isEmpty()) return;

        String outputPath = generateOutputPath(fileName, setup);

        File file = new File(outputPath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            boolean append = setup.appendable();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
                for (String line : content) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи файла — " + fileName + ": " + e.getMessage());
        }
    }

    private String generateOutputPath(String fileName, Setup setup) {
        String path = setup.outputDirectory().orElse("");
        String prefix = setup.prefix().orElse("");
        return path + (path.endsWith("/") || path.isEmpty() ? "" : "/") + prefix + fileName;
    }
}