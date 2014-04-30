package hu.gcs.example.upgrade.engine.schema;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Schema {

    protected int version;
    protected Set<String> statements = new LinkedHashSet<>();

    public abstract int getVersion();

    public abstract Set<String> getStatements();

    protected String schemaVersionUpdateStatement() {
        return String.format("INSERT INTO schema_version (version) VALUES (%d)", getVersion());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((statements == null) ? 0 : statements.hashCode());
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Schema && ((Schema) obj).getVersion() == version;
    };

}
