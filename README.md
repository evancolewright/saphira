# saphira
A lightweight JDBC abstraction wrapped around HikariCP.

### Supported Databases
- MySQL
- SQLite

### Installing
1. Clone the repository from GitHub.
```
git clone https://github.com/evancolewright/saphira.git
```
2. Install to your local .m2 (Maven).
```
mvn clean install
```
3. Add the following dependency to your project (Maven).
```
<dependency>
    <groupId>io.github.evancolewright</groupId>
    <artifactId>saphira</artifactId>
    <version>VERSION</version>
</dependency>
```

### Usage
#### Creating an Instance

```java
// MySQL
MySQLClient mySQLClient = new MySQLClient(new DatabaseCredentials("host", "database", "root", "password"));

// SQLite
File databaseFile = ...;  // Make sure this file is already created
SQLiteClient sqliteClient = new SQLiteClient(databaseFile);
```

All examples will be using the MySQLClient, but they all will work with the SQLiteClient too.

#### Querying the Database

Instead of using the traditional ResultSet that throws all of those icky exceptions and has to be cleaned up after use (which also throws an  exception...like what?), I opted to create a wrapper, QueryResults, that you can use without having to worry about try/catch boilerplate.  It  has most of the same functions as a ResultSet too.

```java
// Sync
QueryResults resultSet = mysqlClient.query("SELECT * FROM PlayerData;", null);
while (resultSet.next())
    // do something
	
// Async
databaseClient.queryAsync("SELECT * FROM Animals;", null).whenComplete(((queryResults, throwable) -> {
    if (throwable == null)
        // Do something with results...
}));
```

#### Updating the Database

```java
// Sync
int rowAltered = mysqlClient.update("INSERT INTO PlayerData (uuid, coins) VALUES (?, ?) ON DUPLICATE KEY UPDATE coins = coins + ?;", (statement) -> {
    preparedStatement.setString(1, String.valueOf(player.getUniqueId()));
    preparedStatement.setInt(2, 100);
    preparedStatement.setInt(3, 100);	
});

//Async
databaseClient.updateAsync("INSERT INTO PlayerData (uuid, coins) VALUES (?, ?) ON DUPLICATE KEY UPDATE coins = coins + ?;", (statement) -> {
    statement.setString(1, String.valueOf("");
    statement.setInt(2, 100);
    statement.setInt(3, 100);
}
).whenComplete(((rowsAltered, throwable) -> {
    if (throwable == null)
        // do something
}));
```

#### Submitting Command Batches to the Database

```java
// Sync
List<Player> players = ...;
int[] batchRowsAltered = mysqlClient.executeBatch("INSERT INTO PlayerData (uuid, coins) (?, ?);", (statement) -> {
    for (Player player : player)
    {
	statement.setString(1, player.getUniqueID());
	statement.setInt(2, 100);
	statement.addBatch();
    }
});

// Async
mysqlClient.executeBatchAsync("INSERT INTO PlayerData (uuid, coins) (?, ?);", (statement) -> {
    for (Player player : player)
    {
	statement.setString(1, player.getUniqueID());
	statement.setInt(2, 100);
	statement.addBatch();
    }
}).whenComplete((array, throwable) -> { 
    if (throwable == null)
        // do something
}));
```

### Exceptions

All SQL Exceptions are wrapped with an **UncheckedSQLException** class. Unlike some SQL API's, you are not forced to catch a SQLException every 5 seconds, but it is  thrown if you did want to in certain situations.  You can choose to handle these situations by wrapping a call that may potentially throw it.

```java
try
{
    // Some library call here
} catch (UncheckedSQLException exception)
{
    // SQLException was thrown, handle it
}
```

### License

MIT



