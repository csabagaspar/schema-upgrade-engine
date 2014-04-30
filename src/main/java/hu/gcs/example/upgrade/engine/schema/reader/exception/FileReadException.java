package hu.gcs.example.upgrade.engine.schema.reader.exception;

public class FileReadException extends RuntimeException {

    public FileReadException(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 7745926156270286707L;

}
