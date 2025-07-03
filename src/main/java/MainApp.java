
public class MainApp {
    public static void main(String[] args) {

        FileContentAnalyzer fp = new FileContentAnalyzer();
        System.out.println(fp.executeUtil(OptionHandler.setup(args)));
    }
}
