import fileService.FileContentAnalyzer;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {

        FileContentAnalyzer fp = new FileContentAnalyzer();
        fp.executeUtil(OptionHandler.setup(args));

    }
}
