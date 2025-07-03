import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FileContentAnalyzer {

    private final List<String> integers = new ArrayList<>();
    private final List<String> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public StringBuilder executeUtil(Setup setup) {
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(setup.filename()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append('\n');

                if (isInteger(line) && isInLongRange(line)) {
                    integers.add(line);
                } else if (isDecimal(line) && isInDoubleRange(line) && hasCorrectPrecision(line)) {
                    floats.add(line);
                } else {
                    strings.add(line);
                }
            }

            writeFile("integers.txt", integers, setup);
            writeFile("floats.txt", floats, setup);
            writeFile("strings.txt", strings, setup);

            // Вывод статистики
            printStatistics(setup);

        } catch (FileNotFoundException e) {
            System.err.println("Ошибка: Файл не найден.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода: " + e.getMessage());
            System.exit(1);
        }

        return fileContent;
    }

    private void printStatistics(Setup setup) {
        System.out.println("" + (setup.isShort() ? "Краткая статистика" : "Полная статистика"));
        System.out.println("Файл: " + setup.filename());
        System.out.println();

        if (setup.isShort()) {
            System.out.printf("Целые числа: %d шт.%n", integers.size());
            System.out.printf("Дробные числа: %d шт.%n", floats.size());
            System.out.printf("Строки: %d шт.%n", strings.size());
            return;
        }

        printIntegerStats();
        printFloatStats();
        printStringStats();
    }

    private void printIntegerStats() {
        if (integers.isEmpty()) return;

        List<BigDecimal> values = integers.stream()
                .map(BigDecimal::new)
                .toList();

        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(null);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(null);
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        double avg = sum.divide(BigDecimal.valueOf(values.size()), BigDecimal.ROUND_HALF_UP).doubleValue();

        System.out.printf("Целые числа: %d шт.%n", integers.size());
        System.out.printf("Минимум: %s%n", min.toPlainString());
        System.out.printf("Максимум: %s%n", max.toPlainString());
        System.out.printf("Сумма: %s%n", sum.toPlainString());
        System.out.printf("Среднее: %.2f%n", avg);
        System.out.println();
    }

    private void printFloatStats() {
        if (floats.isEmpty()) return;

        List<BigDecimal> values = floats.stream()
                .map(BigDecimal::new)
                .toList();

        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(null);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(null);
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        double avg = sum.divide(BigDecimal.valueOf(values.size()), BigDecimal.ROUND_HALF_UP).doubleValue();

        System.out.printf("Дробные числа: %d шт.%n", floats.size());
        System.out.printf("Минимум: %s%n", min.toPlainString());
        System.out.printf("Максимум: %s%n", max.toPlainString());
        System.out.printf("Сумма: %s%n", sum.toPlainString());
        System.out.printf("Среднее: %.4f%n", avg);
        System.out.println();
    }

    private void printStringStats() {
        if (strings.isEmpty()) return;

        String shortest = strings.stream()
                .min((a, b) -> Integer.compare(a.length(), b.length()))
                .orElseThrow();
        String longest = strings.stream()
                .max((a, b) -> Integer.compare(a.length(), b.length()))
                .orElseThrow();

        System.out.printf("Строки: %d шт.%n", strings.size());
        System.out.printf("Минимальная длина: %d (\"%s\")%n", shortest.length(), shortest);
        System.out.printf("Максимальная длина: %d (\"%s\")%n", longest.length(), longest);
        System.out.println();
    }


    private boolean isInteger(String s) {
        return s.matches("^-?\\d+$");
    }

    private boolean isDecimal(String s) {
        return s.matches("^-?(?:\\d+\\.\\d+|\\.\\d+)(?:[eE][+-]?\\d+)?$");
    }

    private boolean isInLongRange(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInDoubleRange(String s) {
        try {
            BigDecimal value = new BigDecimal(s);
            return value.compareTo(BigDecimal.valueOf(Double.MIN_VALUE)) >= 0 ||
                    value.compareTo(BigDecimal.ZERO) == 0 ||
                    value.compareTo(BigDecimal.valueOf(-Double.MIN_VALUE)) <= 0 ||
                    value.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) <= 0 ||
                    value.compareTo(BigDecimal.valueOf(-Double.MAX_VALUE)) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean hasCorrectPrecision(String s){
        return s.chars().filter(c -> c >= '0' && c <= '9').count() <= 16;
    }

    private void writeFile(String fileName, List<String> fileContent, Setup setup) throws IOException {
        if (fileContent.isEmpty()) {
            return;
        }

        String outputFileName = generateOutputPath(fileName, setup);

        File outputFile = new File(outputFileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        boolean append = setup.appendable();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, append))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private String generateOutputPath(String fileName, Setup setup) throws IOException {
        String outputPath = setup.outputDirectory().isPresent()? setup.outputDirectory().get() : "";
        outputPath += setup.prefix().isEmpty() ? "/" + fileName : "/" + setup.prefix().get() + fileName;
        return outputPath;
    }
}