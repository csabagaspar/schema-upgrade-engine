package hu.gcs.example.upgrade.engine.schema;

import java.util.LinkedHashSet;
import java.util.Set;

public class InitSchema extends Schema {

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Set<String> getStatements() {
        final Set<String> statements = new LinkedHashSet<>();
        statements.add("CREATE TABLE schema_version (version INT NOT NULL, PRIMARY KEY (version))");
        statements.add(schemaVersionUpdateStatement());
        return statements;
    }
}
