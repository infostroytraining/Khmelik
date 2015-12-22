package filters;

import java.io.File;

public class SizeFilter extends Filter {

    private long fileMinSize;
    private long fileMaxSize;

    public SizeFilter(Filter nextFilter, long fileMinSize, long fileMaxSize) {
        super(nextFilter);
        this.fileMinSize = fileMinSize;
        this.fileMaxSize = fileMaxSize;
    }

    @Override
    public boolean currentFilter(File file) {
        return file.length() > fileMinSize && file.length() < fileMaxSize;
    }
}
