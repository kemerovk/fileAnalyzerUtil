package fileService;

import java.io.*;
import java.util.*;

public class FileReaderService {

    public List<String> readLines(List<String> filenames) {
        List<String> allLines = new ArrayList<>();
        for (String filename : filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    allLines.add(line);
                }
            } catch (FileNotFoundException e) {
                System.err.println("Ошибка: Файл не найден — " + filename);
            } catch (IOException e) {
                System.err.println("Ошибка ввода/вывода при чтении файла — " + filename + ": " + e.getMessage());
            }
        }
        return allLines;
    }
}