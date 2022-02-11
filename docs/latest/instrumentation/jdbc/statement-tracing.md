---
title: 'JDBC Operations Tracing | Kamon Documentation'
layout: docs
redirect_from:
  - /documentation/0.6.x/kamon-jdbc/overview/
---

{% include toc.html %}

JDBC Statement Instrumentation
==============================

The JDBC instrumentation will automatically create Spans for Statements, Prepared Statements and Callable Statements
created with the official drivers for these databases (please, drop a line if your database driver is missing!):
  - H2
  - SQLite
  - Postgres
  - MySQL / MariaDB
  - Oracle
  - Microsoft SQL Server


Statement Tracing
-----------------

All Spans created by the instrumentation will at least have the following tags:

  - **db.url**: full JDBC URL used to locate the database.
  - **db.vendor**: name of the JDBC driver vendor.
  - **db.statement**: the full executed statement text.

Additionally, if a supported connection pool is used the following tags will be included as well:
  - **jdbc.pool.vendor**: name of the connection pool vendor, currently we only support HikariCP.
  - **jdbc.pool.name**: name of the connection pool.


### Controlling the db.statement tag <small>Since 2.4.7</small>

You can use the `kamon.instrumentation.jdbc.add-db-statement-as-span-tag` setting to control the `db.statement` tag in JDBC calls. This is 
especially important when your JDBC calls might contain sensitive information that you don't want to share with third parties, like your tracing 
vendor. The possible setting values are:
  - **always**: (default) will always add the `db.statement` tag to spans
  - **prepared**: will only add the the `db.statement` tag when the traced execution was made with a PreparedStatement, which should ensure no placeholder values are leaked to the tracing backend
  - **never**: completely disables adding the `db.statement` tag to spans

  

Slow Statements Processor
-------------------------

Besides creating Spans, the JDBC instrumentation can also capture statement executions that take longer than a specified
threshold and run them through a `SlowStatementProcessor` implementation. This module ships with a warning logger that
will automatically log a warning message when any statement execution takes longer than 2 seconds. You can tweak the
threshold or change your processor implementation with these settings:

{% code_block typesafeconfig %}
kamon.instrumentation.jdbc.statements {
  slow {

    # Minimum execution time threshold to consider a statement execution
    # as slow. When a statement execution takes longer than this threshold,
    # it will be passed to all all configured processors.
    threshold = 2 seconds

    # Implementations of SlowStatementProcessor that will handle all
    # slow statement executions.
    processors = [
      "your.custom.Implementation"
    ]
  }
}
{% endcode_block %}



Failed Statements Processor
---------------------------

Finally, Kamon can also capture failed statement executions and run them through a `FailedStatementProcessor`
implementation. This module ships with a failed statements processor that logs error messages when a Statement fails.
You can change your processor implementation with these settings:

{% code_block typesafeconfig %}
kamon.instrumentation.jdbc.statements {
  failed {

    # Implementations of FailedStatementProcessor that will handle all
    # failed statement executions.
    processors = [
      "your.custom.Implementation"
    ]
  }
}
{% endcode_block %}


Manual Installation
-------------------

In case you are not using the Kamon Bundle, add the dependency below to your build:

{% include dependency-info.html module="kamon-jdbc" version=site.data.versions.latest.jdbc %}
{% include instrumentation-agent-notice.html %}
