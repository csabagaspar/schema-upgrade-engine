package hu.gcs.example.upgrade.engine.schema;

import java.util.Comparator;

public class SchemaComparator implements Comparator<Schema> {

    @Override
    public int compare(final Schema s1, final Schema s2) {
        return s1.getVersion() > s2.getVersion() ? 1 : -1;
    }
}
