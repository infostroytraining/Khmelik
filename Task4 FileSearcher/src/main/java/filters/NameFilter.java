package filters;

import com.google.common.io.Files;

import java.io.File;

public class NameFilter extends Filter {

    private String name;

    public NameFilter(Filter nextFilter, String name) {
        super(nextFilter);
        this.name = name;
    }

    @Override
    public boolean currentFilter(File file) {
        String fileName = Files.getNameWithoutExtension(file.getName());
        return fileName.contains(name);
    }
}
