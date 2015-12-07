package analyzer;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class TasksConverter implements IStringConverter<Task> {

    @Override
    public Task convert(String s) {
        Task convertedValue = Task.fromString(s.toUpperCase());
        if (convertedValue == null) {
            throw new ParameterException("Value " + s + "can not be converted to task enum. " +
                    "Available values are: frequency, length, duplicates.");
        }
        return convertedValue;
    }
}
