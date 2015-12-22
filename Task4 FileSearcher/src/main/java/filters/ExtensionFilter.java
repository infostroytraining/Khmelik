package filters;

import com.google.common.io.Files;

import java.io.File;

public class ExtensionFilter extends Filter {

    private String extension;

    public ExtensionFilter(Filter nextFilter, String extension) {
        super(nextFilter);
        this.extension = extension;
    }

    @Override
    public boolean currentFilter(File file) {
        String fileExtension = Files.getFileExtension(file.getName());
        return fileExtension.equals(extension);
    }
}
