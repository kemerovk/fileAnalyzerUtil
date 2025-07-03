import org.apache.commons.cli.*;

import java.util.Optional;

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

            if (cmd.getArgs().length == 0) {
                System.err.println("Ошибка: Не указан входной файл.");
                System.exit(1);
            }

            boolean showShortStat = cmd.hasOption('s');
            boolean appendable = cmd.hasOption('a');
            Optional<String> prefix = Optional.ofNullable(cmd.getOptionValue('p'));
            Optional<String> outputDirectory = Optional.ofNullable(cmd.getOptionValue('o'));
            String filename = cmd.getArgs()[cmd.getArgs().length - 1];

            return new Setup(showShortStat, appendable, prefix, outputDirectory, filename);

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