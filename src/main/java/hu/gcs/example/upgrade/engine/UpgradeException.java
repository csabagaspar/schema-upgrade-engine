package hu.gcs.example.upgrade.engine;

public class UpgradeException extends RuntimeException {
    public UpgradeException(final RuntimeException e) {
        super(e);
    }

    private static final long serialVersionUID = -1525862425591224470L;
}
