import java.util.Optional;

public record Setup(boolean isShort,
                    boolean appendable,
                    Optional<String> prefix,
                    Optional<String> outputDirectory,
                    String filename) {
}
