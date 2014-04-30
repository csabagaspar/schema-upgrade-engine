package hu.gcs.example.upgrade.engine.schema.reader;

public class ReaderFactory {

    public SqlFileReader createFileReader(final String path) {
        return new SqlFileReader(path);
    }

    public PropertyArrayReader createPropertyArrayReader(final String file, final String propertyName) {
        return new PropertyArrayReader(file, propertyName);
    }

    public VersionReader createVersionReader() {
        return new VersionReader();
    }

}
