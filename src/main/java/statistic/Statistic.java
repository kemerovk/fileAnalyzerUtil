package statistic;

import data.ClassifiedData;
import data.Setup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Statistic {
    public void print(ClassifiedData data, Setup setup) {
        System.out.println(setup.isShort() ? "Краткая статистика" : "Полная статистика");
        System.out.println("Файлы " );
        setup.filenames().forEach(System.out::println);
        System.out.println();

        if (setup.isShort()) {
            System.out.printf("Целые числа: %d шт.%n", data.integers().size());
            System.out.printf("Дробные числа: %d шт.%n", data.floats().size());
            System.out.printf("Строки: %d шт.%n", data.strings().size());
            return;
        }

        printIntegerStats(data.integers());
        printFloatStats(data.floats());
        printStringStats(data.strings());
    }
    private void printIntegerStats(List<String> integers) {
        if (integers.isEmpty()) return;

        List<BigDecimal> values = integers.stream()
                .map(BigDecimal::new)
                .toList();

        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(null);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(null);
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        double avg = sum.divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP).doubleValue();

        System.out.printf("Целые числа: %d шт.%n", integers.size());
        System.out.printf("Минимум: %s%n", Objects.requireNonNull(min).toPlainString());
        System.out.printf("Максимум: %s%n", max.toPlainString());
        System.out.printf("Сумма: %s%n", sum.toPlainString());
        System.out.printf("Среднее: %.2f%n", avg);
        System.out.println();
    }

    private void printFloatStats(List<String> floats) {
        if (floats.isEmpty()) return;

        List<BigDecimal> values = floats.stream()
                .map(BigDecimal::new)
                .toList();

        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(null);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(null);
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        double avg = sum.divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP).doubleValue();

        System.out.printf("Дробные числа: %d шт.%n", floats.size());
        System.out.printf("Минимум: %s%n", min != null ? min.toPlainString() : null);
        System.out.printf("Максимум: %s%n", Objects.requireNonNull(max).toPlainString());
        System.out.printf("Сумма: %s%n", sum.toPlainString());
        System.out.printf("Среднее: %.4f%n", avg);
        System.out.println();
    }

    private void printStringStats(List<String> strings) {
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
}
