package com.example.omnimind.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OmniMindDatabase_Impl extends OmniMindDatabase {
  private volatile OmniMindDao _omniMindDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `agent_tasks` (`id` TEXT NOT NULL, `projectId` TEXT NOT NULL, `title` TEXT NOT NULL, `prompt` TEXT NOT NULL, `status` TEXT NOT NULL, `progress` INTEGER NOT NULL, `costEstimateCents` INTEGER NOT NULL, `timeEstimateSeconds` INTEGER NOT NULL, `isApprovedByEstimator` INTEGER NOT NULL, `filesModifiedCount` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `agent_messages` (`id` TEXT NOT NULL, `taskId` TEXT NOT NULL, `agentId` TEXT NOT NULL, `agentName` TEXT NOT NULL, `messageText` TEXT NOT NULL, `thinking` TEXT, `actions` TEXT, `output` TEXT, `verdictType` TEXT NOT NULL, `verdictReason` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `api_keys` (`id` TEXT NOT NULL, `providerName` TEXT NOT NULL, `encryptedKey` TEXT NOT NULL, `baseUrl` TEXT, `priorityWeight` INTEGER NOT NULL, `monthlyBudgetCents` INTEGER, `currentSpentCents` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `lastUsedTimestamp` INTEGER NOT NULL, `lastError` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `projects` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `localPath` TEXT NOT NULL, `status` TEXT NOT NULL, `progress` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL, `filesJson` TEXT NOT NULL, `apiSpentCents` INTEGER NOT NULL, `knowledgeBaseJson` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sandbox_runs` (`id` TEXT NOT NULL, `taskId` TEXT NOT NULL, `buildSuccess` INTEGER NOT NULL, `testsSuccess` INTEGER NOT NULL, `compileLog` TEXT NOT NULL, `testLog` TEXT NOT NULL, `diffSummary` TEXT NOT NULL, `isApplied` INTEGER NOT NULL, `isIgnored` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2aa63b510823381281816fd84d98a222')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `agent_tasks`");
        db.execSQL("DROP TABLE IF EXISTS `agent_messages`");
        db.execSQL("DROP TABLE IF EXISTS `api_keys`");
        db.execSQL("DROP TABLE IF EXISTS `projects`");
        db.execSQL("DROP TABLE IF EXISTS `sandbox_runs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAgentTasks = new HashMap<String, TableInfo.Column>(11);
        _columnsAgentTasks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("projectId", new TableInfo.Column("projectId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("prompt", new TableInfo.Column("prompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("costEstimateCents", new TableInfo.Column("costEstimateCents", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("timeEstimateSeconds", new TableInfo.Column("timeEstimateSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("isApprovedByEstimator", new TableInfo.Column("isApprovedByEstimator", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("filesModifiedCount", new TableInfo.Column("filesModifiedCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAgentTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAgentTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAgentTasks = new TableInfo("agent_tasks", _columnsAgentTasks, _foreignKeysAgentTasks, _indicesAgentTasks);
        final TableInfo _existingAgentTasks = TableInfo.read(db, "agent_tasks");
        if (!_infoAgentTasks.equals(_existingAgentTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "agent_tasks(com.example.omnimind.data.model.AgentTask).\n"
                  + " Expected:\n" + _infoAgentTasks + "\n"
                  + " Found:\n" + _existingAgentTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsAgentMessages = new HashMap<String, TableInfo.Column>(11);
        _columnsAgentMessages.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("taskId", new TableInfo.Column("taskId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("agentId", new TableInfo.Column("agentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("agentName", new TableInfo.Column("agentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("messageText", new TableInfo.Column("messageText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("thinking", new TableInfo.Column("thinking", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("actions", new TableInfo.Column("actions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("output", new TableInfo.Column("output", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("verdictType", new TableInfo.Column("verdictType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("verdictReason", new TableInfo.Column("verdictReason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAgentMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAgentMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAgentMessages = new TableInfo("agent_messages", _columnsAgentMessages, _foreignKeysAgentMessages, _indicesAgentMessages);
        final TableInfo _existingAgentMessages = TableInfo.read(db, "agent_messages");
        if (!_infoAgentMessages.equals(_existingAgentMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "agent_messages(com.example.omnimind.data.model.AgentMessage).\n"
                  + " Expected:\n" + _infoAgentMessages + "\n"
                  + " Found:\n" + _existingAgentMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsApiKeys = new HashMap<String, TableInfo.Column>(10);
        _columnsApiKeys.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("providerName", new TableInfo.Column("providerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("encryptedKey", new TableInfo.Column("encryptedKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("baseUrl", new TableInfo.Column("baseUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("priorityWeight", new TableInfo.Column("priorityWeight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("monthlyBudgetCents", new TableInfo.Column("monthlyBudgetCents", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("currentSpentCents", new TableInfo.Column("currentSpentCents", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("lastUsedTimestamp", new TableInfo.Column("lastUsedTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApiKeys.put("lastError", new TableInfo.Column("lastError", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysApiKeys = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesApiKeys = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoApiKeys = new TableInfo("api_keys", _columnsApiKeys, _foreignKeysApiKeys, _indicesApiKeys);
        final TableInfo _existingApiKeys = TableInfo.read(db, "api_keys");
        if (!_infoApiKeys.equals(_existingApiKeys)) {
          return new RoomOpenHelper.ValidationResult(false, "api_keys(com.example.omnimind.data.model.ApiKeyConfig).\n"
                  + " Expected:\n" + _infoApiKeys + "\n"
                  + " Found:\n" + _existingApiKeys);
        }
        final HashMap<String, TableInfo.Column> _columnsProjects = new HashMap<String, TableInfo.Column>(9);
        _columnsProjects.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("localPath", new TableInfo.Column("localPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("filesJson", new TableInfo.Column("filesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("apiSpentCents", new TableInfo.Column("apiSpentCents", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProjects.put("knowledgeBaseJson", new TableInfo.Column("knowledgeBaseJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProjects = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProjects = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProjects = new TableInfo("projects", _columnsProjects, _foreignKeysProjects, _indicesProjects);
        final TableInfo _existingProjects = TableInfo.read(db, "projects");
        if (!_infoProjects.equals(_existingProjects)) {
          return new RoomOpenHelper.ValidationResult(false, "projects(com.example.omnimind.data.model.Project).\n"
                  + " Expected:\n" + _infoProjects + "\n"
                  + " Found:\n" + _existingProjects);
        }
        final HashMap<String, TableInfo.Column> _columnsSandboxRuns = new HashMap<String, TableInfo.Column>(10);
        _columnsSandboxRuns.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("taskId", new TableInfo.Column("taskId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("buildSuccess", new TableInfo.Column("buildSuccess", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("testsSuccess", new TableInfo.Column("testsSuccess", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("compileLog", new TableInfo.Column("compileLog", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("testLog", new TableInfo.Column("testLog", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("diffSummary", new TableInfo.Column("diffSummary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("isApplied", new TableInfo.Column("isApplied", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("isIgnored", new TableInfo.Column("isIgnored", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSandboxRuns.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSandboxRuns = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSandboxRuns = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSandboxRuns = new TableInfo("sandbox_runs", _columnsSandboxRuns, _foreignKeysSandboxRuns, _indicesSandboxRuns);
        final TableInfo _existingSandboxRuns = TableInfo.read(db, "sandbox_runs");
        if (!_infoSandboxRuns.equals(_existingSandboxRuns)) {
          return new RoomOpenHelper.ValidationResult(false, "sandbox_runs(com.example.omnimind.data.model.SandboxRun).\n"
                  + " Expected:\n" + _infoSandboxRuns + "\n"
                  + " Found:\n" + _existingSandboxRuns);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2aa63b510823381281816fd84d98a222", "fcb8807fcb667b25bf063a8831af1f21");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "agent_tasks","agent_messages","api_keys","projects","sandbox_runs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `agent_tasks`");
      _db.execSQL("DELETE FROM `agent_messages`");
      _db.execSQL("DELETE FROM `api_keys`");
      _db.execSQL("DELETE FROM `projects`");
      _db.execSQL("DELETE FROM `sandbox_runs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(OmniMindDao.class, OmniMindDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public OmniMindDao omniMindDao() {
    if (_omniMindDao != null) {
      return _omniMindDao;
    } else {
      synchronized(this) {
        if(_omniMindDao == null) {
          _omniMindDao = new OmniMindDao_Impl(this);
        }
        return _omniMindDao;
      }
    }
  }
}
