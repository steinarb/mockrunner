package com.mockrunner.mock.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mock implementation of <code>DatabaseMetaData</code>.
 */
public class MockDatabaseMetaData implements DatabaseMetaData
{
    private int databaseMajorVersion = 1;
    private int databaseMinorVersion;
    private int defaultTransactionLevel = Connection.TRANSACTION_READ_COMMITTED;
    private int driverMajorVersion = 1;
    private int driverMinorVersion;
    private int jdbcMajorVersion = 3;
    private int jdbcMinorVersion;
    private int maxBinaryLiteralLength;
    private int maxCatalogNameLength;
    private int maxCharLiteralLength;
    private int maxColumnNameLength;
    private int maxColumnsInGroupBy;
    private int maxColumnsInIndex;
    private int maxColumnsInOrderBy;
    private int maxColumnsInSelect;
    private int maxColumnsInTable;
    private int maxConnections;
    private int maxCursorNameLength;
    private int maxIndexLength;
    private int maxProcedureNameLength;
    private int maxRowSize;
    private int maxSchemaNameLength;
    private int maxStatementLength;
    private int maxStatements;
    private int maxTableNameLength;
    private int maxTablesInSelect;
    private int maxUserNameLength;
    private int resultSetHoldability = ResultSet.CONCUR_READ_ONLY;
    private int sqlStateType = sqlStateSQL99;
    private boolean allProceduresAreCallable = true;
    private boolean allTablesAreSelectable = true;
    private boolean dataDefinitionCausesTransactionCommit = false;
    private boolean dataDefinitionIgnoredInTransactions = false;        
    private boolean doesMaxRowSizeIncludeBlobs = false;
    private boolean isCatalogAtStart = false;
    private boolean isReadOnly = false;
    private boolean locatorsUpdateCopy = false;
    private boolean nullPlusNonNullIsNull = false;
    private boolean nullsAreSortedAtEnd = false;
    private boolean nullsAreSortedAtStart = false;
    private boolean nullsAreSortedHigh = false;
    private boolean nullsAreSortedLow = false;
    private boolean storesLowerCaseIdentifiers = true;
    private boolean storesLowerCaseQuotedIdentifiers = true;
    private boolean storesMixedCaseIdentifiers = true;
    private boolean storesMixedCaseQuotedIdentifiers = true;
    private boolean storesUpperCaseIdentifiers = true;
    private boolean storesUpperCaseQuotedIdentifiers = true;
    private boolean supportsANSI92EntryLevelSQL = true;
    private boolean supportsANSI92FullSQL = true;    
    private boolean supportsANSI92IntermediateSQL = true;
    private boolean supportsAlterTableWithAddColumn = true;
    private boolean supportsAlterTableWithDropColumn = true;
    private boolean supportsBatchUpdates = true;
    private boolean supportsCatalogsInDataManipulation = true;
    private boolean supportsCatalogsInIndexDefinitions = true;
    private boolean supportsCatalogsInPrivilegeDefinitions = true;
    private boolean supportsCatalogsInProcedureCalls = true;
    private boolean supportsCatalogsInTableDefinitions = true;
    private boolean supportsColumnAliasing = true;
    private boolean supportsConvert = true;
    private boolean supportsCoreSQLGrammar = true;
    private boolean supportsCorrelatedSubqueries = true;
    private boolean supportsDataDefinitionAndDataManipulationTransactions = true;
    private boolean supportsDataManipulationTransactionsOnly = false;
    private boolean supportsDifferentTableCorrelationNames;
    private boolean supportsExpressionsInOrderBy = true;
    private boolean supportsExtendedSQLGrammar = true;
    private boolean supportsFullOuterJoins = true;
    private boolean supportsGetGeneratedKeys = true;
    private boolean supportsGroupBy = true;
    private boolean supportsGroupByBeyondSelect = true;
    private boolean supportsGroupByUnrelated = true;  
    private boolean supportsIntegrityEnhancementFacility = true;
    private boolean supportsLikeEscapeClause = true;
    private boolean supportsLimitedOuterJoins = true;   
    private boolean supportsMinimumSQLGrammar = true;;
    private boolean supportsMixedCaseIdentifiers = true;
    private boolean supportsMixedCaseQuotedIdentifiers = true;
    private boolean supportsMultipleOpenResults = true;
    private boolean supportsMultipleResultSets = true;
    private boolean supportsMultipleTransactions = true;
    private boolean supportsNamedParameters = true;
    private boolean supportsNonNullableColumns = true;
    private boolean supportsOpenCursorsAcrossCommit = true;
    private boolean supportsOpenCursorsAcrossRollback = true;
    private boolean supportsOpenStatementsAcrossCommit = true;
    private boolean supportsOpenStatementsAcrossRollback = true;
    private boolean supportsOrderByUnrelated = true;
    private boolean supportsOuterJoins = true;
    private boolean supportsPositionedDelete = true;
    private boolean supportsPositionedUpdate = true;
    private boolean supportsSavepoints = true;
    private boolean supportsSchemasInDataManipulation = true;
    private boolean supportsSchemasInIndexDefinitions = true;
    private boolean supportsSchemasInPrivilegeDefinitions = true;
    private boolean supportsSchemasInProcedureCalls = true;
    private boolean supportsSchemasInTableDefinitions = true;
    private boolean supportsSelectForUpdate = true;
    private boolean supportsStatementPooling = true;
    private boolean supportsStoredProcedures = true;
    private boolean supportsSubqueriesInComparisons = true;
    private boolean supportsSubqueriesInExists = true;
    private boolean supportsSubqueriesInIns = true;
    private boolean supportsSubqueriesInQuantifieds = true;
    private boolean supportsTableCorrelationNames = true;
    private boolean supportsTransactions = true;;
    private boolean supportsUnion = true;
    private boolean supportsUnionAll = true;
    private boolean usesLocalFilePerTable = false;
    private boolean usesLocalFiles = true;
    private boolean deletesAreDetected = true;
    private boolean insertsAreDetected = true;
    private boolean othersDeletesAreVisible = true;
    private boolean othersInsertsAreVisible = true;
    private boolean othersUpdatesAreVisible = true;
    private boolean ownDeletesAreVisible = true;
    private boolean ownInsertsAreVisible = true;
    private boolean ownUpdatesAreVisible = true;
    private boolean supportsResultSetHoldability = true;
    private boolean supportsResultSetType = true;
    private boolean supportsTransactionIsolationLevel = true;
    private boolean updatesAreDetected = true;
    private boolean supportsResultSetConcurrency = true;
    private String catalogSeparator = ".";
    private String catalogTerm = "database";
    private String databaseProductName = "MockDatabase";
    private String databaseProductVersion = "1.0";
    private String driverName = MockDriver.class.getName();
    private String driverVersion = "1.0";
    private String extraNameCharacters = "";
    private String identifierQuoteString = " ";
    private String numericFunctions = "";
    private String procedureTerm = "";
    private String sqlKeywords = "";
    private String schemaTerm = "";
    private String searchStringEscape = "\\";
    private String stringFunctions = "";
    private String systemFunctions = "";
    private String timeDateFunctions = "";
    private String url;
    private String userName;
    private Connection connection;
    private ResultSet catalogs;
    private ResultSet schemas;
    private ResultSet tableTypes;
    private ResultSet typeInfo;
    private ResultSet exportedKeys;
    private ResultSet importedKeys;
    private ResultSet primaryKeys;
    private ResultSet procedures;
    private ResultSet superTables;
    private ResultSet superTypes;
    private ResultSet tablePrivileges;
    private ResultSet versionColumns;
    private ResultSet bestRowIdentifier;
    private ResultSet indexInfo;
    private ResultSet udts;
    private ResultSet attributes;
    private ResultSet columnPrivileges;
    private ResultSet columns;
    private ResultSet procedureColumns;
    private ResultSet tables;
    private ResultSet crossReference;
    
    public int getDatabaseMajorVersion() throws SQLException
    {
        return databaseMajorVersion;
    }
    
    public void setDatabaseMajorVersion(int version)
    {
        databaseMajorVersion = version;
    }

    public int getDatabaseMinorVersion() throws SQLException
    {
        return databaseMinorVersion;
    }
    
    public void setDatabaseMinorVersion(int version)
    {
        databaseMinorVersion = version;
    }
    
    public int getDefaultTransactionIsolation() throws SQLException
    {
        return defaultTransactionLevel;
    }
    
    public void setDefaultTransactionIsolation(int defaultTransactionLevel)
    {
        this.defaultTransactionLevel = defaultTransactionLevel;
    }

    public int getDriverMajorVersion()
    {
        return driverMajorVersion;
    }
    
    public void setDriverMajorVersion(int driverMajorVersion)
    {
        this.driverMajorVersion = driverMajorVersion;
    }

    public int getDriverMinorVersion()
    {
        return driverMinorVersion;
    }
    
    public void setDriverMinorVersion(int driverMinorVersion)
    {
        this.driverMinorVersion = driverMinorVersion;
    }

    public int getJDBCMajorVersion() throws SQLException
    {
        return jdbcMajorVersion;
    }
    
    public void setJDBCMajorVersion(int jdbcMajorVersion)
    {
        this.jdbcMajorVersion = jdbcMajorVersion;
    }

    public int getJDBCMinorVersion() throws SQLException
    {
        return jdbcMinorVersion;
    }
    
    public void setJDBCMinorVersion(int jdbcMinorVersion)
    {
        this.jdbcMinorVersion = jdbcMinorVersion;
    }

    public int getMaxBinaryLiteralLength() throws SQLException
    {
        return maxBinaryLiteralLength;
    }
    
    public void setMaxBinaryLiteralLength(int maxBinaryLiteralLength)
    {
        this.maxBinaryLiteralLength = maxBinaryLiteralLength;
    }

    public int getMaxCatalogNameLength() throws SQLException
    {
        return maxCatalogNameLength;
    }
    
    public void setetMaxCatalogNameLength(int maxCatalogNameLength)
    {
        this.maxCatalogNameLength = maxCatalogNameLength;
    }

    public int getMaxCharLiteralLength() throws SQLException
    {
        return maxCharLiteralLength;
    }
    
    public void setMaxCharLiteralLength(int maxCharLiteralLength)
    {
        this.maxCharLiteralLength = maxCharLiteralLength;
    }

    public int getMaxColumnNameLength() throws SQLException
    {
        return maxColumnNameLength;
    }
    
    public void setMaxColumnNameLength(int maxColumnNameLength)
    {
        this.maxColumnNameLength = maxColumnNameLength;
    }

    public int getMaxColumnsInGroupBy() throws SQLException
    {
        return maxColumnsInGroupBy;
    }
    
    public void setMaxColumnsInGroupBy(int maxColumnsInGroupBy)
    {
        this.maxColumnsInGroupBy = maxColumnsInGroupBy;
    }

    public int getMaxColumnsInIndex() throws SQLException
    {
        return maxColumnsInIndex;
    }
    
    public void setMaxColumnsInIndex(int maxColumnsInIndex)
    {
        this.maxColumnsInIndex = maxColumnsInIndex;
    }

    public int getMaxColumnsInOrderBy() throws SQLException
    {
        return maxColumnsInOrderBy;
    }
    
    public void setMaxColumnsInOrderBy(int maxColumnsInOrderBy)
    {
        this.maxColumnsInOrderBy = maxColumnsInOrderBy;
    }

    public int getMaxColumnsInSelect() throws SQLException
    {
        return maxColumnsInSelect;
    }
    
    public void setMaxColumnsInSelect(int maxColumnsInSelect)
    {
        this.maxColumnsInSelect = maxColumnsInSelect;
    }

    public int getMaxColumnsInTable() throws SQLException
    {
        return maxColumnsInTable;
    }
    
    public void setMaxColumnsInTable(int maxColumnsInTable)
    {
        this.maxColumnsInTable = maxColumnsInTable;
    }

    public int getMaxConnections() throws SQLException
    {
        return maxConnections;
    }
    
    public void setMaxConnections(int maxConnections)
    {
        this.maxConnections = maxConnections;
    }

    public int getMaxCursorNameLength() throws SQLException
    {
        return maxCursorNameLength;
    }
    
    public void setMaxCursorNameLength(int maxCursorNameLength)
    {
        this.maxCursorNameLength = maxCursorNameLength;
    }

    public int getMaxIndexLength() throws SQLException
    {
        return maxIndexLength;
    }
    
    public void setMaxIndexLength(int maxIndexLength)
    {
        this.maxIndexLength = maxIndexLength;
    }

    public int getMaxProcedureNameLength() throws SQLException
    {
        return maxProcedureNameLength;
    }
    
    public void setMaxProcedureNameLength(int maxProcedureNameLength)
    {
        this.maxProcedureNameLength = maxProcedureNameLength;
    }

    public int getMaxRowSize() throws SQLException
    {
        return maxRowSize;
    }
    
    public void setMaxRowSize(int maxRowSize)
    {
        this.maxRowSize = maxRowSize;
    }

    public int getMaxSchemaNameLength() throws SQLException
    {
        return maxSchemaNameLength;
    }
    
    public void setMaxSchemaNameLength(int maxSchemaNameLength)
    {
        this.maxSchemaNameLength = maxSchemaNameLength;
    }

    public int getMaxStatementLength() throws SQLException
    {
        return maxStatementLength;
    }
    
    public void setMaxStatementLength(int maxStatementLength)
    {
        this.maxStatementLength = maxStatementLength;
    }

    public int getMaxStatements() throws SQLException
    {
        return maxStatements;
    }
    
    public void setMaxStatements(int maxStatements)
    {
        this.maxStatements = maxStatements;
    }

    public int getMaxTableNameLength() throws SQLException
    {
        return maxTableNameLength;
    }
    
    public void setMaxTableNameLength(int maxTableNameLength)
    {
        this.maxTableNameLength = maxTableNameLength;
    }

    public int getMaxTablesInSelect() throws SQLException
    {
        return maxTablesInSelect;
    }
    
    public void setMaxTablesInSelect(int maxTablesInSelect)
    {
        this.maxTablesInSelect = maxTablesInSelect;
    }

    public int getMaxUserNameLength() throws SQLException
    {
        return maxUserNameLength;
    }
    
    public void setMaxUserNameLength(int maxUserNameLength)
    {
        this.maxUserNameLength = maxUserNameLength;
    }

    public int getResultSetHoldability() throws SQLException
    {
        return resultSetHoldability;
    }
    
    public void setResultSetHoldability(int resultSetHoldability)
    {
        this.resultSetHoldability = resultSetHoldability;
    }

    public int getSQLStateType() throws SQLException
    {
        return sqlStateType;
    }
    
    public void setSQLStateType(int sqlStateType)
    {
        this.sqlStateType = sqlStateType;
    }

    public boolean allProceduresAreCallable() throws SQLException
    {
        return allProceduresAreCallable;
    }
    
    public void setAllProceduresAreCallable(boolean callable)
    {
        allProceduresAreCallable = callable;
    }

    public boolean allTablesAreSelectable() throws SQLException
    {
        return allTablesAreSelectable;
    }
    
    public void setAllTablesAreSelectable(boolean selectable)
    {
        allTablesAreSelectable = selectable;
    }

    public boolean dataDefinitionCausesTransactionCommit() throws SQLException
    {
        return dataDefinitionCausesTransactionCommit;
    }
    
    public void setDataDefinitionCausesTransactionCommit(boolean causesCommit)
    {
        dataDefinitionCausesTransactionCommit = causesCommit;
    }

    public boolean dataDefinitionIgnoredInTransactions() throws SQLException
    {
        return dataDefinitionIgnoredInTransactions;
    }
    
    public void setDataDefinitionIgnoredInTransactions(boolean ignored)
    {
        dataDefinitionIgnoredInTransactions = ignored;
    }

    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException
    {
        return doesMaxRowSizeIncludeBlobs;
    }
    
    public void setDoesMaxRowSizeIncludeBlobs(boolean includeBlobs)
    {
        doesMaxRowSizeIncludeBlobs = includeBlobs;
    }

    public boolean isCatalogAtStart() throws SQLException
    {
        return isCatalogAtStart;
    }
    
    public void setIsCatalogAtStart(boolean isCatalogAtStart)
    {
        this.isCatalogAtStart = isCatalogAtStart;
    }

    public boolean isReadOnly() throws SQLException
    {
        return isReadOnly;
    }
    
    public void setIsReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    public boolean locatorsUpdateCopy() throws SQLException
    {
        return locatorsUpdateCopy;
    }
    
    public void setLocatorsUpdateCopy(boolean locatorsUpdateCopy)
    {
        this.locatorsUpdateCopy = locatorsUpdateCopy;
    }

    public boolean nullPlusNonNullIsNull() throws SQLException
    {
        return nullPlusNonNullIsNull;
    }
    
    public void setNullPlusNonNullIsNull(boolean nullPlusNonNullIsNull)
    {
        this.nullPlusNonNullIsNull = nullPlusNonNullIsNull;
    }

    public boolean nullsAreSortedAtEnd() throws SQLException
    {
        return nullsAreSortedAtEnd;
    }
    
    public void setNullsAreSortedAtEnd(boolean nullsAreSortedAtEnd)
    {
        this.nullsAreSortedAtEnd = nullsAreSortedAtEnd;
    }

    public boolean nullsAreSortedAtStart() throws SQLException
    {
        return nullsAreSortedAtStart;
    }
    
    public void setNullsAreSortedAtStart(boolean nullsAreSortedAtStart)
    {
        this.nullsAreSortedAtStart = nullsAreSortedAtStart;
    }

    public boolean nullsAreSortedHigh() throws SQLException
    {
        return nullsAreSortedHigh;
    }
    
    public void setNullsAreSortedHigh(boolean nullsAreSortedHigh)
    {
        this.nullsAreSortedHigh = nullsAreSortedHigh;
    }

    public boolean nullsAreSortedLow() throws SQLException
    {
        return nullsAreSortedLow;
    }
    
    public void setNullsAreSortedLow(boolean nullsAreSortedLow)
    {
        this.nullsAreSortedLow = nullsAreSortedLow;
    }

    public boolean storesLowerCaseIdentifiers() throws SQLException
    {
        return storesLowerCaseIdentifiers;
    }
    
    public void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers)
    {
        this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
    }

    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException
    {
        return storesLowerCaseQuotedIdentifiers;
    }
    
    public void setStoresLowerCaseQuotedIdentifiers(boolean storesLowerCaseQuotedIdentifiers)
    {
        this.storesLowerCaseQuotedIdentifiers = storesLowerCaseQuotedIdentifiers;
    }

    public boolean storesMixedCaseIdentifiers() throws SQLException
    {
        return storesMixedCaseIdentifiers;
    }
    
    public void setStoresMixedCaseIdentifiers(boolean storesMixedCaseIdentifiers)
    {
        this.storesMixedCaseIdentifiers = storesMixedCaseIdentifiers;
    }

    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException
    {
        return storesMixedCaseQuotedIdentifiers;
    }
    
    public void setStoresMixedCaseQuotedIdentifiers(boolean storesMixedCaseQuotedIdentifiers)
    {
        this.storesMixedCaseQuotedIdentifiers = storesMixedCaseQuotedIdentifiers;
    }

    public boolean storesUpperCaseIdentifiers() throws SQLException
    {
        return storesUpperCaseIdentifiers;
    }
    
    public void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers)
    {
        this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
    }

    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException
    {
        return storesUpperCaseQuotedIdentifiers;
    }
    
    public void setStoresUpperCaseQuotedIdentifiers(boolean storesUpperCaseQuotedIdentifiers)
    {
        this.storesUpperCaseQuotedIdentifiers = storesUpperCaseQuotedIdentifiers;
    }

    public boolean supportsANSI92EntryLevelSQL() throws SQLException
    {
        return supportsANSI92EntryLevelSQL;
    }
    
    public void setSupportsANSI92EntryLevelSQL(boolean supportsANSI92EntryLevelSQL)
    {
        this.supportsANSI92EntryLevelSQL = supportsANSI92EntryLevelSQL;
    }

    public boolean supportsANSI92FullSQL() throws SQLException
    {
        return supportsANSI92FullSQL;
    }
    
    public void setSupportsANSI92FullSQL(boolean supportsANSI92FullSQL)
    {
        this.supportsANSI92FullSQL = supportsANSI92FullSQL;
    }

    public boolean supportsANSI92IntermediateSQL() throws SQLException
    {
        return supportsANSI92IntermediateSQL;
    }
    
    public void setSupportsANSI92IntermediateSQL(boolean supportsANSI92IntermediateSQL)
    {
        this.supportsANSI92IntermediateSQL = supportsANSI92IntermediateSQL;
    }

    public boolean supportsAlterTableWithAddColumn() throws SQLException
    {
        return supportsAlterTableWithAddColumn;
    }
    
    public void setSupportsAlterTableWithAddColumn(boolean supportsAlterTableWithAddColumn)
    {
        this.supportsAlterTableWithAddColumn = supportsAlterTableWithAddColumn;
    }

    public boolean supportsAlterTableWithDropColumn() throws SQLException
    {
        return supportsAlterTableWithDropColumn;
    }
    
    public void setSupportsAlterTableWithDropColumn(boolean supportsAlterTableWithDropColumn)
    {
        this.supportsAlterTableWithDropColumn = supportsAlterTableWithDropColumn;
    }

    public boolean supportsBatchUpdates() throws SQLException
    {
        return supportsBatchUpdates;
    }
    
    public void setSupportsBatchUpdates(boolean supportsBatchUpdates)
    {
        this.supportsBatchUpdates = supportsBatchUpdates;
    }

    public boolean supportsCatalogsInDataManipulation() throws SQLException
    {
        return supportsCatalogsInDataManipulation;
    }
    
    public void setSupportsCatalogsInDataManipulation(boolean supportsCatalogsInDataManipulation)
    {
        this.supportsCatalogsInDataManipulation = supportsCatalogsInDataManipulation;
    }

    public boolean supportsCatalogsInIndexDefinitions() throws SQLException
    {
        return supportsCatalogsInIndexDefinitions;
    }
    
    public void setSupportsCatalogsInIndexDefinitions(boolean supportsCatalogsInIndexDefinitions)
    {
        this.supportsCatalogsInIndexDefinitions = supportsCatalogsInIndexDefinitions;
    }

    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException
    {
        return supportsCatalogsInPrivilegeDefinitions;
    }
    
    public void setSupportsCatalogsInPrivilegeDefinitions(boolean supportsCatalogsInPrivilegeDefinitions)
    {
        this.supportsCatalogsInPrivilegeDefinitions = supportsCatalogsInPrivilegeDefinitions;
    }

    public boolean supportsCatalogsInProcedureCalls() throws SQLException
    {
        return supportsCatalogsInProcedureCalls;
    }
    
    public void setSupportsCatalogsInProcedureCalls(boolean supportsCatalogsInProcedureCalls)
    {
        this.supportsCatalogsInProcedureCalls = supportsCatalogsInProcedureCalls;
    }

    public boolean supportsCatalogsInTableDefinitions() throws SQLException
    {
        return supportsCatalogsInTableDefinitions;
    }
    
    public void setSupportsCatalogsInTableDefinitions(boolean supportsCatalogsInTableDefinitions)
    {
        this.supportsCatalogsInTableDefinitions = supportsCatalogsInTableDefinitions;
    }

    public boolean supportsColumnAliasing() throws SQLException
    {
        return supportsColumnAliasing;
    }
    
    public void setSupportsColumnAliasing(boolean supportsColumnAliasing)
    {
        this.supportsColumnAliasing = supportsColumnAliasing;
    }

    public boolean supportsConvert() throws SQLException
    {
        return supportsConvert;
    }
    
    public void setSupportsConvert(boolean supportsConvert)
    {
        this.supportsConvert = supportsConvert;
    }

    public boolean supportsCoreSQLGrammar() throws SQLException
    {
        return supportsCoreSQLGrammar;
    }
    
    public void setSupportsCoreSQLGrammar(boolean supportsCoreSQLGrammar)
    {
        this.supportsCoreSQLGrammar = supportsCoreSQLGrammar;
    }

    public boolean supportsCorrelatedSubqueries() throws SQLException
    {
        return supportsCorrelatedSubqueries;
    }
    
    public void setSupportsCorrelatedSubqueries(boolean supportsCorrelatedSubqueries)
    {
        this.supportsCorrelatedSubqueries = supportsCorrelatedSubqueries;
    }

    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException
    {
        return supportsDataDefinitionAndDataManipulationTransactions;
    }
    
    public void setSupportsDataDefinitionAndDataManipulationTransactions(boolean supportsDataDefinitionAndDataManipulationTransactions)
    {
        this.supportsDataDefinitionAndDataManipulationTransactions = supportsDataDefinitionAndDataManipulationTransactions;
    }

    public boolean supportsDataManipulationTransactionsOnly() throws SQLException
    {
        return supportsDataManipulationTransactionsOnly;
    }
    
    public void setSupportsDataManipulationTransactionsOnly(boolean supportsDataManipulationTransactionsOnly)
    {
        this.supportsDataManipulationTransactionsOnly = supportsDataManipulationTransactionsOnly;
    }

    public boolean supportsDifferentTableCorrelationNames() throws SQLException
    {
        return supportsDifferentTableCorrelationNames;
    }
    
    public void setSupportsDifferentTableCorrelationNames(boolean supportsDifferentTableCorrelationNames)
    {
        this.supportsDifferentTableCorrelationNames = supportsDifferentTableCorrelationNames;
    }

    public boolean supportsExpressionsInOrderBy() throws SQLException
    {
        return supportsExpressionsInOrderBy;
    }
    
    public void setSupportsExpressionsInOrderBy(boolean supportsExpressionsInOrderBy)
    {
        this.supportsExpressionsInOrderBy = supportsExpressionsInOrderBy;
    }

    public boolean supportsExtendedSQLGrammar() throws SQLException
    {
        return supportsExtendedSQLGrammar;
    }
    
    public void setSupportsExtendedSQLGrammar(boolean supportsExtendedSQLGrammar)
    {
        this.supportsExtendedSQLGrammar = supportsExtendedSQLGrammar;
    }

    public boolean supportsFullOuterJoins() throws SQLException
    {
        return supportsFullOuterJoins;
    }
    
    public void setSupportsFullOuterJoins(boolean supportsFullOuterJoins)
    {
        this.supportsFullOuterJoins = supportsFullOuterJoins;
    }

    public boolean supportsGetGeneratedKeys() throws SQLException
    {
        return supportsGetGeneratedKeys;
    }
    
    public void setSupportsGetGeneratedKeys(boolean supportsGetGeneratedKeys)
    {
        this.supportsGetGeneratedKeys = supportsGetGeneratedKeys;
    }

    public boolean supportsGroupBy() throws SQLException
    {
        return supportsGroupBy;
    }
    
    public void setSupportsGroupBy(boolean supportsGroupBy)
    {
        this.supportsGroupBy = supportsGroupBy;
    }

    public boolean supportsGroupByBeyondSelect() throws SQLException
    {
        return supportsGroupByBeyondSelect;
    }
    
    public void setSupportsGroupByBeyondSelect(boolean supportsGroupByBeyondSelect)
    {
        this.supportsGroupByBeyondSelect = supportsGroupByBeyondSelect;
    }

    public boolean supportsGroupByUnrelated() throws SQLException
    {
        return supportsGroupByUnrelated;
    }
    
    public void setSupportsGroupByUnrelated(boolean supportsGroupByUnrelated)
    {
        this.supportsGroupByUnrelated = supportsGroupByUnrelated;
    }

    public boolean supportsIntegrityEnhancementFacility() throws SQLException
    {
        return supportsIntegrityEnhancementFacility;
    }
    
    public void setSupportsIntegrityEnhancementFacility(boolean supportsIntegrityEnhancementFacility)
    {
        this.supportsIntegrityEnhancementFacility = supportsIntegrityEnhancementFacility;
    }

    public boolean supportsLikeEscapeClause() throws SQLException
    {
        return supportsLikeEscapeClause;
    }
    
    public void setSupportsLikeEscapeClause(boolean supportsLikeEscapeClause)
    {
        this.supportsLikeEscapeClause = supportsLikeEscapeClause;
    }

    public boolean supportsLimitedOuterJoins() throws SQLException
    {
        return supportsLimitedOuterJoins;
    }
    
    public void setSupportsLimitedOuterJoins(boolean supportsLimitedOuterJoins)
    {
        this.supportsLimitedOuterJoins = supportsLimitedOuterJoins;
    }

    public boolean supportsMinimumSQLGrammar() throws SQLException
    {
        return supportsMinimumSQLGrammar;
    }
    
    public void setSupportsMinimumSQLGrammar(boolean supportsMinimumSQLGrammar)
    {
        this.supportsMinimumSQLGrammar = supportsMinimumSQLGrammar;
    }

    public boolean supportsMixedCaseIdentifiers() throws SQLException
    {
        return supportsMixedCaseIdentifiers;
    }
    
    public void setSupportsMixedCaseIdentifiers(boolean supportsMixedCaseIdentifiers)
    {
        this.supportsMixedCaseIdentifiers = supportsMixedCaseIdentifiers;
    }

    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException
    {
        return supportsMixedCaseQuotedIdentifiers;
    }
    
    public void setSupportsMixedCaseQuotedIdentifiers(boolean supportsMixedCaseQuotedIdentifiers)
    {
        this.supportsMixedCaseQuotedIdentifiers = supportsMixedCaseQuotedIdentifiers;
    }

    public boolean supportsMultipleOpenResults() throws SQLException
    {
        return supportsMultipleOpenResults;
    }
    
    public void setSupportsMultipleOpenResults(boolean supportsMultipleOpenResults)
    {
        this.supportsMultipleOpenResults = supportsMultipleOpenResults;
    }

    public boolean supportsMultipleResultSets() throws SQLException
    {
        return supportsMultipleResultSets;
    }
    
    public void setSupportsMultipleResultSets(boolean supportsMultipleResultSets)
    {
        this.supportsMultipleResultSets = supportsMultipleResultSets;
    }

    public boolean supportsMultipleTransactions() throws SQLException
    {
        return supportsMultipleTransactions;
    }
    
    public void setSupportsMultipleTransactions(boolean supportsMultipleTransactions)
    {
        this.supportsMultipleTransactions = supportsMultipleTransactions;
    }

    public boolean supportsNamedParameters() throws SQLException
    {
        return supportsNamedParameters;
    }
    
    public void setSupportsNamedParameters(boolean supportsNamedParameters)
    {
        this.supportsNamedParameters = supportsNamedParameters;
    }

    public boolean supportsNonNullableColumns() throws SQLException
    {
        return supportsNonNullableColumns;
    }
    
    public void setSupportsNonNullableColumns(boolean supportsNonNullableColumns)
    {
        this.supportsNonNullableColumns = supportsNonNullableColumns;
    }

    public boolean supportsOpenCursorsAcrossCommit() throws SQLException
    {
        return supportsOpenCursorsAcrossCommit;
    }
    
    public void setSupportsOpenCursorsAcrossCommit(boolean supportsOpenCursorsAcrossCommit)
    {
        this.supportsOpenCursorsAcrossCommit = supportsOpenCursorsAcrossCommit;
    }

    public boolean supportsOpenCursorsAcrossRollback() throws SQLException
    {
        return supportsOpenCursorsAcrossRollback;
    }
    
    public void setSupportsOpenCursorsAcrossRollback(boolean supportsOpenCursorsAcrossRollback)
    {
        this.supportsOpenCursorsAcrossRollback = supportsOpenCursorsAcrossRollback;
    }

    public boolean supportsOpenStatementsAcrossCommit() throws SQLException
    {
        return supportsOpenStatementsAcrossCommit;
    }
    
    public void setSupportsOpenStatementsAcrossCommit(boolean supportsOpenStatementsAcrossCommit)
    {
        this.supportsOpenStatementsAcrossCommit = supportsOpenStatementsAcrossCommit;
    }

    public boolean supportsOpenStatementsAcrossRollback() throws SQLException
    {
        return supportsOpenStatementsAcrossRollback;
    }
    
    public void setSupportsOpenStatementsAcrossRollback(boolean supportsOpenStatementsAcrossRollback)
    {
        this.supportsOpenStatementsAcrossRollback = supportsOpenStatementsAcrossRollback;
    }

    public boolean supportsOrderByUnrelated() throws SQLException
    {
        return supportsOrderByUnrelated;
    }
    
    public void setSupportsOrderByUnrelated(boolean supportsOrderByUnrelated)
    {
        this.supportsOrderByUnrelated = supportsOrderByUnrelated;
    }

    public boolean supportsOuterJoins() throws SQLException
    {
        return supportsOuterJoins;
    }
    
    public void setSupportsOuterJoins(boolean supportsOuterJoins)
    {
        this.supportsOuterJoins = supportsOuterJoins;
    }

    public boolean supportsPositionedDelete() throws SQLException
    {
        return supportsPositionedDelete;
    }
    
    public void setSupportsPositionedDelete(boolean supportsPositionedDelete)
    {
        this.supportsPositionedDelete = supportsPositionedDelete;
    }

    public boolean supportsPositionedUpdate() throws SQLException
    {
        return supportsPositionedUpdate;
    }
    
    public void setSupportsPositionedUpdate(boolean supportsPositionedUpdate)
    {
        this.supportsPositionedUpdate = supportsPositionedUpdate;
    }

    public boolean supportsSavepoints() throws SQLException
    {
        return supportsSavepoints;
    }
    
    public void setSupportsSavepoints(boolean supportsSavepoints)
    {
        this.supportsSavepoints = supportsSavepoints;
    }

    public boolean supportsSchemasInDataManipulation() throws SQLException
    {
        return supportsSchemasInDataManipulation;
    }
    
    public void setSupportsSchemasInDataManipulation(boolean supportsSchemasInDataManipulation)
    {
        this.supportsSchemasInDataManipulation = supportsSchemasInDataManipulation;
    }

    public boolean supportsSchemasInIndexDefinitions() throws SQLException
    {
        return supportsSchemasInIndexDefinitions;
    }
    
    public void setSupportsSchemasInIndexDefinitions(boolean supportsSchemasInIndexDefinitions)
    {
        this.supportsSchemasInIndexDefinitions = supportsSchemasInIndexDefinitions;
    }

    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException
    {
        return supportsSchemasInPrivilegeDefinitions;
    }
    
    public void setSupportsSchemasInPrivilegeDefinitions(boolean supportsSchemasInPrivilegeDefinitions)
    {
        this.supportsSchemasInPrivilegeDefinitions = supportsSchemasInPrivilegeDefinitions;
    }

    public boolean supportsSchemasInProcedureCalls() throws SQLException
    {
        return supportsSchemasInProcedureCalls;
    }
    
    public void setSupportsSchemasInProcedureCalls(boolean supportsSchemasInProcedureCalls)
    {
        this.supportsSchemasInProcedureCalls = supportsSchemasInProcedureCalls;
    }

    public boolean supportsSchemasInTableDefinitions() throws SQLException
    {
        return supportsSchemasInTableDefinitions;
    }
    
    public void setSupportsSchemasInTableDefinitions(boolean supportsSchemasInTableDefinitions)
    {
        this.supportsSchemasInTableDefinitions = supportsSchemasInTableDefinitions;
    }

    public boolean supportsSelectForUpdate() throws SQLException
    {
        return supportsSelectForUpdate;
    }
    
    public void setSupportsSelectForUpdate(boolean supportsSelectForUpdate)
    {
        this.supportsSelectForUpdate = supportsSelectForUpdate;
    }

    public boolean supportsStatementPooling() throws SQLException
    {
        return supportsStatementPooling;
    }
    
    public void setSupportsStatementPooling(boolean supportsStatementPooling)
    {
        this.supportsStatementPooling = supportsStatementPooling;
    }

    public boolean supportsStoredProcedures() throws SQLException
    {
        return supportsStoredProcedures;
    }
    
    public void setSupportsStoredProcedures(boolean supportsStoredProcedures)
    {
        this.supportsStoredProcedures = supportsStoredProcedures;
    }

    public boolean supportsSubqueriesInComparisons() throws SQLException
    {
        return supportsSubqueriesInComparisons;
    }
    
    public void setSupportsSubqueriesInComparisons(boolean supportsSubqueriesInComparisons)
    {
        this.supportsSubqueriesInComparisons = supportsSubqueriesInComparisons;
    }

    public boolean supportsSubqueriesInExists() throws SQLException
    {
        return supportsSubqueriesInExists;
    }
    
    public void setSupportsSubqueriesInExists(boolean supportsSubqueriesInExists)
    {
        this.supportsSubqueriesInExists = supportsSubqueriesInExists;
    }

    public boolean supportsSubqueriesInIns() throws SQLException
    {
        return supportsSubqueriesInIns;
    }
    
    public void setSupportsSubqueriesInIns(boolean supportsSubqueriesInIns)
    {
        this.supportsSubqueriesInIns = supportsSubqueriesInIns;
    }

    public boolean supportsSubqueriesInQuantifieds() throws SQLException
    {
        return supportsSubqueriesInQuantifieds;
    }
    
    public void setSupportsSubqueriesInQuantifieds(boolean supportsSubqueriesInQuantifieds)
    {
        this.supportsSubqueriesInQuantifieds = supportsSubqueriesInQuantifieds;
    }

    public boolean supportsTableCorrelationNames() throws SQLException
    {
        return supportsTableCorrelationNames;
    }
    
    public void setSupportsTableCorrelationNames(boolean supportsTableCorrelationNames)
    {
        this.supportsTableCorrelationNames = supportsTableCorrelationNames;
    }

    public boolean supportsTransactions() throws SQLException
    {
        return supportsTransactions;
    }
    
    public void setSupportsTransactions(boolean supportsTransactions)
    {
        this.supportsTransactions = supportsTransactions;
    }

    public boolean supportsUnion() throws SQLException
    {
        return supportsUnion;
    }
    
    public void setSupportsUnion(boolean supportsUnion)
    {
        this.supportsUnion = supportsUnion;
    }

    public boolean supportsUnionAll() throws SQLException
    {
        return supportsUnionAll;
    }
    
    public void setSupportsUnionAll(boolean supportsUnionAll)
    {
        this.supportsUnionAll = supportsUnionAll;
    }

    public boolean usesLocalFilePerTable() throws SQLException
    {
        return usesLocalFilePerTable;
    }
    
    public void setUsesLocalFilePerTable(boolean usesLocalFilePerTable)
    {
        this.usesLocalFilePerTable = usesLocalFilePerTable;
    }

    public boolean usesLocalFiles() throws SQLException
    {
        return usesLocalFiles;
    }
    
    public void setUsesLocalFiles(boolean usesLocalFiles)
    {
        this.usesLocalFiles = usesLocalFiles;
    }

    public boolean deletesAreDetected(int type) throws SQLException
    {
        return deletesAreDetected;
    }
    
    public void setDeletesAreDetected(boolean deletesAreDetected)
    {
        this.deletesAreDetected = deletesAreDetected;
    }

    public boolean insertsAreDetected(int type) throws SQLException
    {
        return insertsAreDetected;
    }
    
    public void setInsertsAreDetected(boolean insertsAreDetected)
    {
        this.insertsAreDetected = insertsAreDetected;
    }

    public boolean othersDeletesAreVisible(int type) throws SQLException
    {
        return othersDeletesAreVisible;
    }
    
    public void setOthersDeletesAreVisible(boolean othersDeletesAreVisible)
    {
        this.othersDeletesAreVisible = othersDeletesAreVisible;
    }

    public boolean othersInsertsAreVisible(int type) throws SQLException
    {
        return othersInsertsAreVisible;
    }
    
    public void setOthersInsertsAreVisible(boolean othersInsertsAreVisible)
    {
        this.othersInsertsAreVisible = othersInsertsAreVisible;
    }

    public boolean othersUpdatesAreVisible(int type) throws SQLException
    {
        return othersUpdatesAreVisible;
    }
    
    public void setOthersUpdatesAreVisible(boolean othersUpdatesAreVisible)
    {
        this.othersUpdatesAreVisible = othersUpdatesAreVisible;
    }

    public boolean ownDeletesAreVisible(int type) throws SQLException
    {
        return ownDeletesAreVisible;
    }
    
    public void setOwnDeletesAreVisible(boolean ownDeletesAreVisible)
    {
        this.ownDeletesAreVisible = ownDeletesAreVisible;
    }

    public boolean ownInsertsAreVisible(int type) throws SQLException
    {
        return ownInsertsAreVisible;
    }
    
    public void setOwnInsertsAreVisible(boolean ownInsertsAreVisible)
    {
        this.ownInsertsAreVisible = ownInsertsAreVisible;
    }

    public boolean ownUpdatesAreVisible(int type) throws SQLException
    {
        return ownUpdatesAreVisible;
    }
    
    public void setOwnUpdatesAreVisible(boolean ownUpdatesAreVisible)
    {
        this.ownUpdatesAreVisible = ownUpdatesAreVisible;
    }

    public boolean supportsResultSetHoldability(int holdability) throws SQLException
    {
        return supportsResultSetHoldability;
    }
    
    public void setSupportsResultSetHoldability(boolean supportsResultSetHoldability)
    {
        this.supportsResultSetHoldability = supportsResultSetHoldability;
    }

    public boolean supportsResultSetType(int type) throws SQLException
    {
        return supportsResultSetType;
    }
    
    public void setSupportsResultSetType(boolean supportsResultSetType)
    {
        this.supportsResultSetType = supportsResultSetType;
    }

    public boolean supportsTransactionIsolationLevel(int level) throws SQLException
    {
        return supportsTransactionIsolationLevel;
    }
    
    public void setSupportsTransactionIsolationLevel(boolean supportsTransactionIsolationLevel)
    {
        this.supportsTransactionIsolationLevel = supportsTransactionIsolationLevel;
    }

    public boolean updatesAreDetected(int type) throws SQLException
    {
        return updatesAreDetected;
    }
    
    public void setUpdatesAreDetected(boolean updatesAreDetected)
    {
        this.updatesAreDetected = updatesAreDetected;
    }

    public boolean supportsConvert(int fromType, int toType) throws SQLException
    {
        return supportsConvert;
    }
  
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException
    {
        return supportsResultSetConcurrency;
    }
    
    public void setSupportsResultSetConcurrency(boolean supportsResultSetConcurrency)
    {
        this.supportsResultSetConcurrency = supportsResultSetConcurrency;
    }

    public String getCatalogSeparator() throws SQLException
    {
        return catalogSeparator;
    }
    
    public void setCatalogSeparator(String catalogSeparator)
    {
        this.catalogSeparator = catalogSeparator;
    }

    public String getCatalogTerm() throws SQLException
    {
        return catalogTerm;
    }
    
    public void setCatalogTerm(String catalogTerm)
    {
        this.catalogTerm = catalogTerm;
    }

    public String getDatabaseProductName() throws SQLException
    {
        return databaseProductName;
    }
    
    public void setDatabaseProductName(String databaseProductName)
    {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseProductVersion() throws SQLException
    {
        return databaseProductVersion;
    }
    
    public void setDatabaseProductVersion(String databaseProductVersion)
    {
        this.databaseProductVersion = databaseProductVersion;
    }

    public String getDriverName() throws SQLException
    {
        return driverName;
    }
    
    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getDriverVersion() throws SQLException
    {
        return driverVersion;
    }
    
    public void setDriverVersion(String driverVersion)
    {
        this.driverVersion = driverVersion;
    }

    public String getExtraNameCharacters() throws SQLException
    {
        return extraNameCharacters;
    }
    
    public void setExtraNameCharacters(String extraNameCharacters)
    {
        this.extraNameCharacters = extraNameCharacters;
    }

    public String getIdentifierQuoteString() throws SQLException
    {
        return identifierQuoteString;
    }
    
    public void setIdentifierQuoteString(String identifierQuoteString)
    {
        this.identifierQuoteString = identifierQuoteString;
    }

    public String getNumericFunctions() throws SQLException
    {
        return numericFunctions;
    }
    
    public void setNumericFunctions(String numericFunctions)
    {
        this.numericFunctions = numericFunctions;
    }

    public String getProcedureTerm() throws SQLException
    {
        return procedureTerm;
    }
    
    public void setProcedureTerm(String procedureTerm)
    {
        this.procedureTerm = procedureTerm;
    }

    public String getSQLKeywords() throws SQLException
    {
        return sqlKeywords;
    }
    
    public void setSQLKeywords(String sqlKeywords)
    {
        this.sqlKeywords = sqlKeywords;
    }

    public String getSchemaTerm() throws SQLException
    {
        return schemaTerm;
    }
    
    public void setSchemaTerm(String schemaTerm)
    {
        this.schemaTerm = schemaTerm;
    }

    public String getSearchStringEscape() throws SQLException
    {
        return searchStringEscape;
    }
    
    public void setSearchStringEscape(String searchStringEscape)
    {
        this.searchStringEscape = searchStringEscape;
    }

    public String getStringFunctions() throws SQLException
    {
        return stringFunctions;
    }
    
    public void setStringFunctions(String stringFunctions)
    {
        this.stringFunctions = stringFunctions;
    }

    public String getSystemFunctions() throws SQLException
    {
        return systemFunctions;
    }
    
    public void setSystemFunctions(String systemFunctions)
    {
        this.systemFunctions = systemFunctions;
    }

    public String getTimeDateFunctions() throws SQLException
    {
        return timeDateFunctions;
    }
    
    public void setTimeDateFunctions(String timeDateFunctions)
    {
        this.timeDateFunctions = timeDateFunctions;
    }

    public String getURL() throws SQLException
    {
        return url;
    }
    
    public void setURL(String url)
    {
        this.url = url;
    }

    public String getUserName() throws SQLException
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Connection getConnection() throws SQLException
    {
        return connection;
    }
    
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    public ResultSet getCatalogs() throws SQLException
    {
        return catalogs;
    }
    
    public void setCatalogs(ResultSet catalogs)
    {
        this.catalogs = catalogs;
    }

    public ResultSet getSchemas() throws SQLException
    {
        return schemas;
    }
    
    public void setSchemas(ResultSet schemas)
    {
        this.schemas = schemas;
    }

    public ResultSet getTableTypes() throws SQLException
    {
        return tableTypes;
    }
    
    public void setTableTypes(ResultSet tableTypes)
    {
        this.tableTypes = tableTypes;
    }

    public ResultSet getTypeInfo() throws SQLException
    {
        return typeInfo;
    }
    
    public void setTypeInfo(ResultSet typeInfo)
    {
        this.typeInfo = typeInfo;
    }

    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException
    {
        return exportedKeys;
    }
    
    public void setExportedKeys(ResultSet exportedKeys)
    {
        this.exportedKeys = exportedKeys;
    }

    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException
    {
        return importedKeys;
    }
    
    public void setImportedKeys(ResultSet importedKeys)
    {
        this.importedKeys = importedKeys;
    }

    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException
    {
        return primaryKeys;
    }
    
    public void setPrimaryKeys(ResultSet primaryKeys)
    {
        this.primaryKeys = primaryKeys;
    }

    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException
    {
        return procedures;
    }
    
    public void setProcedures(ResultSet procedures)
    {
        this.procedures = procedures;
    }

    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException
    {
        return superTables;
    }
    
    public void setSuperTables(ResultSet superTables)
    {
        this.superTables = superTables;
    }

    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException
    {
        return superTypes;
    }
    
    public void setSuperTypes(ResultSet superTypes)
    {
        this.superTypes = superTypes;
    }

    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException
    {
        return tablePrivileges;
    }
    
    public void setTablePrivileges(ResultSet tablePrivileges)
    {
        this.tablePrivileges = tablePrivileges;
    }

    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException
    {
        return versionColumns;
    }
    
    public void setVersionColumns(ResultSet versionColumns)
    {
        this.versionColumns = versionColumns;
    }

    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException
    {
        return bestRowIdentifier;
    }
    
    public void setBestRowIdentifier(ResultSet bestRowIdentifier)
    {
        this.bestRowIdentifier = bestRowIdentifier;
    }

    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException
    {
        return indexInfo;
    }
    
    public void setIndexInfo(ResultSet indexInfo)
    {
        this.indexInfo = indexInfo;
    }

    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException
    {
        return udts;
    }
    
    public void setUDTs(ResultSet udts)
    {
        this.udts = udts;
    }

    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException
    {
        return attributes;
    }
    
    public void setAttributes(ResultSet attributes)
    {
        this.attributes = attributes;
    }

    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException
    {
        return columnPrivileges;
    }
    
    public void setColumnPrivileges(ResultSet columnPrivileges)
    {
        this.columnPrivileges = columnPrivileges;
    }

    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException
    {
        return columns;
    }
    
    public void setColumns(ResultSet columns)
    {
        this.columns = columns;
    }

    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException
    {
        return procedureColumns;
    }
    
    public void setProcedureColumns(ResultSet procedureColumns)
    {
        this.procedureColumns = procedureColumns;
    }

    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException
    {
        return tables;
    }
    
    public void setTables(ResultSet tables)
    {
        this.tables = tables;
    }

    public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException
    {
        return crossReference;
    }

    public void setCrossReference(ResultSet crossReference)
    {
        this.crossReference = crossReference;
    }
}
