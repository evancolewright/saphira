# saphira
> **Saphira** (pronounced "suh-FEAR-uh") is a minimal JDBC abstraction wrapped around [HikariCP](https://github.com/brettwooldridge/HikariCP).

## 🛠️ Installation (Maven)
1. Clone the repository from GitHub.
```
git clone https://github.com/evancolewright/saphira.git
```
2. Install to your local .m2.
```
mvn clean install
```
3. Add the following dependency to your project.
```
<dependency>
    <groupId>pro.evanwright</groupId>
    <artifactId>saphira</artifactId>
    <version>VERSION</version>
</dependency>
```

## ⌨️ Usage
### Creating an Instance

```java
// MySQL
MySQLClient mySQLClient = new MySQLClient(new DatabaseCredentials("host", "database", "root", "password"));

// SQLite
File databaseFile = ...;  // Make sure this file is already created
SQLiteClient sqliteClient = new SQLiteClient(databaseFile);
```

All examples will be using the MySQLClient, but they all will work with the SQLiteClient too.

### Querying the Database

Instead of using the traditional ResultSet that throws a checked SQLException and has to be cleaned up after use (which also throws an exception...like.. why?), I opted to create a wrapper, **QueryResults**, that you can use without having to worry about try/catch boilerplate.  It is, quite literally, a ResultSet without checked exceptions.

```java
final String statement = "SELECT * FROM PlayerData;";
        
// Sync
QueryResults queryResults = mysqlClient.query(statement, null);
while (queryResults.next()) {
    // do something with the results
}
	
// Async (recommended)
mySQLClient.queryAsync(statement, null).whenComplete(((queryResults, throwable) -> {
    if (throwable == null) {
        // do something with the results
    }
}));
```

### Updating the Database

```java
final String statement = "INSERT INTO PlayerData (uuid, coins) VALUES (?, ?) ON DUPLICATE KEY UPDATE coins = coins + ?;";

// Sync
int rowAltered = mysqlClient.update(statement, (ps) -> {
    ps.setString(1, uuid);
    preparedStatement.setInt(2, 100);
    preparedStatement.setInt(3, 100);
});

// Async (recommended)
databaseClient.updateAsync(statement, (ps) -> {
    ps.setString(1, uuid);
    ps.setInt(2, 100);
    ps.setInt(3, 100);
}).whenComplete(((rowsAltered, throwable) -> {
    if (throwable == null) {
        // do something with rowsAltered
    }
}));
```

## 🐛 Exceptions

All SQL Exceptions are wrapped with an **UncheckedSQLException** class. This essentially means that you aren't forced to wrap every database call with a try/catch block, but you most certainly can.
```java
try
{
    // Some library call here
} catch (UncheckedSQLException exception)
{
    // SQLException was thrown, handle it
}
```
## 🗒️ License

MIT



