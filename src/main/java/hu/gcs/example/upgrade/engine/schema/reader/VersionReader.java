package hu.gcs.example.upgrade.engine.schema.reader;

import hu.gcs.example.upgrade.engine.schema.reader.exception.VersionNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionReader {

    private static final String REGEX = "\\d+";
    private final Pattern numberPattern = Pattern.compile(REGEX);

    public int read(final String filename) {
        final Matcher m = numberPattern.matcher(filename);
        while (m.find()) {
            return Integer.parseInt(m.group());
        }
        throw new VersionNotFoundException();
    }

}
