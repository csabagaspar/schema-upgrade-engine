package hu.gcs.example.upgrade.datasource;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@DataSourceDefinition(name = "java:jboss/datasources/TestDS", className = "org.h2.jdbcx.JdbcDataSource", url = "jdbc:h2:mem:test")
@Singleton
@Startup
public class H2SqlDataSource {

}
