import filters.*;

import java.io.File;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        FileFilterChain chain = new FileFilterChain();
        chain.getParametersFromConsole();
        List<File> result = chain.getResultFiles();
        result.stream().forEach(System.out::println);
    }
}
