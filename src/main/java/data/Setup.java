package data;

import java.util.List;
import java.util.Optional;

public record Setup(boolean isShort,
                    boolean appendable,
                    Optional<String> prefix,
                    Optional<String> outputDirectory,
                    List<String> filenames) {
}
