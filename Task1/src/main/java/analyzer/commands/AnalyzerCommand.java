package analyzer.commands;

import java.io.IOException;

public interface AnalyzerCommand {

    public static final String WORD_DIVIDER_REGEXP = "[\\s,.!?\"\'“”’—\\(\\)…:‘0-9;/\\*]+";

    public String execute(String filePath) throws IOException;
}
