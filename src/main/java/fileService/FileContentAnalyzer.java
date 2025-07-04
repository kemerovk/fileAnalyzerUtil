package fileService;

import data.ClassifiedData;
import data.Setup;
import statistic.Statistic;
import java.util.List;

public class FileContentAnalyzer {

    private final FileReaderService fileReader = new FileReaderService();
    private final DataClassifier dataClassifier = new DataClassifier();
    private final DataWriter dataWriter = new DataWriter();
    private final Statistic statsPrinter = new Statistic();

    public void executeUtil(Setup setup){
        StringBuilder result = new StringBuilder();

        List<String> lines = fileReader.readLines(setup.filenames());
        lines.forEach(line -> result.append(line).append('\n'));

        dataClassifier.classify(lines);
        ClassifiedData classifiedData = dataClassifier.getResult();
        dataWriter.write(classifiedData, setup);

        statsPrinter.print(classifiedData, setup);
    }
}