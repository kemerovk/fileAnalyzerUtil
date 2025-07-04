import data.Setup;
import exception.OppositeOptionsException;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class OptionHandler {
    public static Setup setup(String[] args) {
        Options options = new Options();

        options.addOption("o", true, "this directory will contain all the output files");
        options.addOption("p", true, "prefix for output files");

        options.addOption("s", false, "show short statistics");
        options.addOption("f", false, "show full statistics");
        options.addOption("a", false, "make files appendable");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Ошибка набора параметров: " + e.getMessage());
            System.exit(1);
            return null;
        }

        try {
            if (cmd.hasOption("f") && cmd.hasOption("s")) {
                throw new OppositeOptionsException("Вы выбрали взаимоисключающие опции: -s и -f");
            }

            boolean showShortStat = cmd.hasOption('s');
            boolean appendable = cmd.hasOption('a');
            Optional<String> prefix = Optional.ofNullable(cmd.getOptionValue('p'));
            Optional<String> outputDirectory = Optional.ofNullable(cmd.getOptionValue('o'));

            String[] rawArgs = cmd.getArgs();
            if (rawArgs.length == 0) {
                System.err.println("Ошибка: Не указаны входные файлы.");
                System.exit(1);
            }

            List<String> validFiles = new ArrayList<>();
            List<String> invalidFiles = new ArrayList<>();

            for (String filename : rawArgs) {
                File file = new File(filename);
                if (file.exists() && file.isFile()) {
                    validFiles.add(filename);
                } else {
                    invalidFiles.add(filename);
                }
            }

            if (validFiles.isEmpty()) {
                System.err.println("Ошибка: Ни один из указанных файлов не существует.");
                System.exit(1);
            }

            if (!invalidFiles.isEmpty()) {
                System.err.println("Предупреждение: Следующие файлы не найдены и будут проигнорированы:");
                invalidFiles.forEach(System.err::println);
            }


            return new Setup(showShortStat, appendable, prefix, outputDirectory, new ArrayList<>(validFiles));

        } catch (OppositeOptionsException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
            return null;
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}