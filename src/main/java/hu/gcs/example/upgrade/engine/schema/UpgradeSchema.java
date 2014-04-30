package hu.gcs.example.upgrade.engine.schema;

import java.util.Collections;
import java.util.Set;

public class UpgradeSchema extends Schema {

    public UpgradeSchema(final int version, final Set<String> statements) {
        this.version = version;
        this.statements.addAll(statements);
        this.statements.add(schemaVersionUpdateStatement());
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public Set<String> getStatements() {
        return Collections.unmodifiableSet(statements);
    }
}
