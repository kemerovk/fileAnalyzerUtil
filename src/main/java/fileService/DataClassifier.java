package fileService;

import data.ClassifiedData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataClassifier {
    private final List<String> integers = new ArrayList<>();
    private final List<String> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public void classify(List<String> lines) {
        for (String line : lines) {
            if (isInteger(line) && isInLongRange(line)) {
                integers.add(line);
            } else if (isDecimal(line) && isInDoubleRange(line) && hasCorrectPrecision(line)) {
                floats.add(line);
            } else {
                strings.add(line);
            }
        }
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
    public ClassifiedData getResult() {
        return new ClassifiedData(integers, floats, strings);
    }
}
