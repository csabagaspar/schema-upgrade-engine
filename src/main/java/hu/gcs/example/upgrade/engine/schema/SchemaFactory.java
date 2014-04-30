package hu.gcs.example.upgrade.engine.schema;

import java.util.Set;

public class SchemaFactory {
    public Schema createUpgradeSchema(final int version, final Set<String> statements) {
        return new UpgradeSchema(version, statements);
    }

    public Schema createInitSchema() {
        return new InitSchema();
    }
}
