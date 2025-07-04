package data;

import java.util.List;

public record ClassifiedData(
        List<String> integers,
        List<String> floats,
        List<String> strings
) {}