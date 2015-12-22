package filters;

import java.io.File;

public abstract class Filter {

    private Filter nextFilter;

    protected Filter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public boolean doChain(File file) {
        boolean result = currentFilter(file);
        if (nextFilter != null && result) {
            return nextFilter.doChain(file);
        }
        return result;
    }

    public abstract boolean currentFilter(File file);
}
