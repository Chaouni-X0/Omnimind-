package com.example.omnimind.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.omnimind.data.model.AgentMessage;
import com.example.omnimind.data.model.AgentTask;
import com.example.omnimind.data.model.ApiKeyConfig;
import com.example.omnimind.data.model.Project;
import com.example.omnimind.data.model.SandboxRun;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OmniMindDao_Impl implements OmniMindDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AgentTask> __insertionAdapterOfAgentTask;

  private final EntityInsertionAdapter<AgentMessage> __insertionAdapterOfAgentMessage;

  private final EntityInsertionAdapter<Project> __insertionAdapterOfProject;

  private final EntityInsertionAdapter<ApiKeyConfig> __insertionAdapterOfApiKeyConfig;

  private final EntityInsertionAdapter<SandboxRun> __insertionAdapterOfSandboxRun;

  private final EntityDeletionOrUpdateAdapter<AgentTask> __updateAdapterOfAgentTask;

  private final EntityDeletionOrUpdateAdapter<Project> __updateAdapterOfProject;

  private final EntityDeletionOrUpdateAdapter<ApiKeyConfig> __updateAdapterOfApiKeyConfig;

  public OmniMindDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAgentTask = new EntityInsertionAdapter<AgentTask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `agent_tasks` (`id`,`projectId`,`title`,`prompt`,`status`,`progress`,`costEstimateCents`,`timeEstimateSeconds`,`isApprovedByEstimator`,`filesModifiedCount`,`lastUpdated`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AgentTask entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getProjectId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getPrompt());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getProgress());
        statement.bindLong(7, entity.getCostEstimateCents());
        statement.bindLong(8, entity.getTimeEstimateSeconds());
        final int _tmp = entity.isApprovedByEstimator() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getFilesModifiedCount());
        statement.bindLong(11, entity.getLastUpdated());
      }
    };
    this.__insertionAdapterOfAgentMessage = new EntityInsertionAdapter<AgentMessage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `agent_messages` (`id`,`taskId`,`agentId`,`agentName`,`messageText`,`thinking`,`actions`,`output`,`verdictType`,`verdictReason`,`timestamp`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AgentMessage entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTaskId());
        statement.bindString(3, entity.getAgentId());
        statement.bindString(4, entity.getAgentName());
        statement.bindString(5, entity.getMessageText());
        if (entity.getThinking() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getThinking());
        }
        if (entity.getActions() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getActions());
        }
        if (entity.getOutput() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOutput());
        }
        statement.bindString(9, entity.getVerdictType());
        if (entity.getVerdictReason() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getVerdictReason());
        }
        statement.bindLong(11, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfProject = new EntityInsertionAdapter<Project>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `projects` (`id`,`name`,`localPath`,`status`,`progress`,`lastUpdated`,`filesJson`,`apiSpentCents`,`knowledgeBaseJson`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Project entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLocalPath());
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getProgress());
        statement.bindLong(6, entity.getLastUpdated());
        statement.bindString(7, entity.getFilesJson());
        statement.bindLong(8, entity.getApiSpentCents());
        statement.bindString(9, entity.getKnowledgeBaseJson());
      }
    };
    this.__insertionAdapterOfApiKeyConfig = new EntityInsertionAdapter<ApiKeyConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `api_keys` (`id`,`providerName`,`encryptedKey`,`baseUrl`,`priorityWeight`,`monthlyBudgetCents`,`currentSpentCents`,`isEnabled`,`lastUsedTimestamp`,`lastError`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ApiKeyConfig entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getProviderName());
        statement.bindString(3, entity.getEncryptedKey());
        if (entity.getBaseUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBaseUrl());
        }
        statement.bindLong(5, entity.getPriorityWeight());
        if (entity.getMonthlyBudgetCents() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getMonthlyBudgetCents());
        }
        statement.bindLong(7, entity.getCurrentSpentCents());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getLastUsedTimestamp());
        if (entity.getLastError() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastError());
        }
      }
    };
    this.__insertionAdapterOfSandboxRun = new EntityInsertionAdapter<SandboxRun>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sandbox_runs` (`id`,`taskId`,`buildSuccess`,`testsSuccess`,`compileLog`,`testLog`,`diffSummary`,`isApplied`,`isIgnored`,`timestamp`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SandboxRun entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTaskId());
        final int _tmp = entity.getBuildSuccess() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.getTestsSuccess() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindString(5, entity.getCompileLog());
        statement.bindString(6, entity.getTestLog());
        statement.bindString(7, entity.getDiffSummary());
        final int _tmp_2 = entity.isApplied() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        final int _tmp_3 = entity.isIgnored() ? 1 : 0;
        statement.bindLong(9, _tmp_3);
        statement.bindLong(10, entity.getTimestamp());
      }
    };
    this.__updateAdapterOfAgentTask = new EntityDeletionOrUpdateAdapter<AgentTask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `agent_tasks` SET `id` = ?,`projectId` = ?,`title` = ?,`prompt` = ?,`status` = ?,`progress` = ?,`costEstimateCents` = ?,`timeEstimateSeconds` = ?,`isApprovedByEstimator` = ?,`filesModifiedCount` = ?,`lastUpdated` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AgentTask entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getProjectId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getPrompt());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getProgress());
        statement.bindLong(7, entity.getCostEstimateCents());
        statement.bindLong(8, entity.getTimeEstimateSeconds());
        final int _tmp = entity.isApprovedByEstimator() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getFilesModifiedCount());
        statement.bindLong(11, entity.getLastUpdated());
        statement.bindString(12, entity.getId());
      }
    };
    this.__updateAdapterOfProject = new EntityDeletionOrUpdateAdapter<Project>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `projects` SET `id` = ?,`name` = ?,`localPath` = ?,`status` = ?,`progress` = ?,`lastUpdated` = ?,`filesJson` = ?,`apiSpentCents` = ?,`knowledgeBaseJson` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Project entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLocalPath());
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getProgress());
        statement.bindLong(6, entity.getLastUpdated());
        statement.bindString(7, entity.getFilesJson());
        statement.bindLong(8, entity.getApiSpentCents());
        statement.bindString(9, entity.getKnowledgeBaseJson());
        statement.bindString(10, entity.getId());
      }
    };
    this.__updateAdapterOfApiKeyConfig = new EntityDeletionOrUpdateAdapter<ApiKeyConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `api_keys` SET `id` = ?,`providerName` = ?,`encryptedKey` = ?,`baseUrl` = ?,`priorityWeight` = ?,`monthlyBudgetCents` = ?,`currentSpentCents` = ?,`isEnabled` = ?,`lastUsedTimestamp` = ?,`lastError` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ApiKeyConfig entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getProviderName());
        statement.bindString(3, entity.getEncryptedKey());
        if (entity.getBaseUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBaseUrl());
        }
        statement.bindLong(5, entity.getPriorityWeight());
        if (entity.getMonthlyBudgetCents() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getMonthlyBudgetCents());
        }
        statement.bindLong(7, entity.getCurrentSpentCents());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getLastUsedTimestamp());
        if (entity.getLastError() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastError());
        }
        statement.bindString(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertTask(final AgentTask task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAgentTask.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMessage(final AgentMessage message,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAgentMessage.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProject(final Project project, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProject.insertAndReturnId(project);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertApiKey(final ApiKeyConfig apiKey,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfApiKeyConfig.insertAndReturnId(apiKey);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSandboxRun(final SandboxRun run,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSandboxRun.insertAndReturnId(run);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTask(final AgentTask task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAgentTask.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProject(final Project project, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProject.handle(project);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateApiKey(final ApiKeyConfig apiKey,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfApiKeyConfig.handle(apiKey);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AgentTask> getTaskById(final String taskId) {
    final String _sql = "SELECT * FROM agent_tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"agent_tasks"}, new Callable<AgentTask>() {
      @Override
      @Nullable
      public AgentTask call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "projectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfCostEstimateCents = CursorUtil.getColumnIndexOrThrow(_cursor, "costEstimateCents");
          final int _cursorIndexOfTimeEstimateSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "timeEstimateSeconds");
          final int _cursorIndexOfIsApprovedByEstimator = CursorUtil.getColumnIndexOrThrow(_cursor, "isApprovedByEstimator");
          final int _cursorIndexOfFilesModifiedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "filesModifiedCount");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final AgentTask _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProjectId;
            _tmpProjectId = _cursor.getString(_cursorIndexOfProjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpCostEstimateCents;
            _tmpCostEstimateCents = _cursor.getLong(_cursorIndexOfCostEstimateCents);
            final long _tmpTimeEstimateSeconds;
            _tmpTimeEstimateSeconds = _cursor.getLong(_cursorIndexOfTimeEstimateSeconds);
            final boolean _tmpIsApprovedByEstimator;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsApprovedByEstimator);
            _tmpIsApprovedByEstimator = _tmp != 0;
            final int _tmpFilesModifiedCount;
            _tmpFilesModifiedCount = _cursor.getInt(_cursorIndexOfFilesModifiedCount);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new AgentTask(_tmpId,_tmpProjectId,_tmpTitle,_tmpPrompt,_tmpStatus,_tmpProgress,_tmpCostEstimateCents,_tmpTimeEstimateSeconds,_tmpIsApprovedByEstimator,_tmpFilesModifiedCount,_tmpLastUpdated);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AgentTask>> getTasksByProject(final String projectId) {
    final String _sql = "SELECT * FROM agent_tasks WHERE projectId = ? ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, projectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"agent_tasks"}, new Callable<List<AgentTask>>() {
      @Override
      @NonNull
      public List<AgentTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "projectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfCostEstimateCents = CursorUtil.getColumnIndexOrThrow(_cursor, "costEstimateCents");
          final int _cursorIndexOfTimeEstimateSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "timeEstimateSeconds");
          final int _cursorIndexOfIsApprovedByEstimator = CursorUtil.getColumnIndexOrThrow(_cursor, "isApprovedByEstimator");
          final int _cursorIndexOfFilesModifiedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "filesModifiedCount");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<AgentTask> _result = new ArrayList<AgentTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AgentTask _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProjectId;
            _tmpProjectId = _cursor.getString(_cursorIndexOfProjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpCostEstimateCents;
            _tmpCostEstimateCents = _cursor.getLong(_cursorIndexOfCostEstimateCents);
            final long _tmpTimeEstimateSeconds;
            _tmpTimeEstimateSeconds = _cursor.getLong(_cursorIndexOfTimeEstimateSeconds);
            final boolean _tmpIsApprovedByEstimator;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsApprovedByEstimator);
            _tmpIsApprovedByEstimator = _tmp != 0;
            final int _tmpFilesModifiedCount;
            _tmpFilesModifiedCount = _cursor.getInt(_cursorIndexOfFilesModifiedCount);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new AgentTask(_tmpId,_tmpProjectId,_tmpTitle,_tmpPrompt,_tmpStatus,_tmpProgress,_tmpCostEstimateCents,_tmpTimeEstimateSeconds,_tmpIsApprovedByEstimator,_tmpFilesModifiedCount,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AgentMessage>> getMessagesByTask(final String taskId) {
    final String _sql = "SELECT * FROM agent_messages WHERE taskId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"agent_messages"}, new Callable<List<AgentMessage>>() {
      @Override
      @NonNull
      public List<AgentMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfAgentId = CursorUtil.getColumnIndexOrThrow(_cursor, "agentId");
          final int _cursorIndexOfAgentName = CursorUtil.getColumnIndexOrThrow(_cursor, "agentName");
          final int _cursorIndexOfMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "messageText");
          final int _cursorIndexOfThinking = CursorUtil.getColumnIndexOrThrow(_cursor, "thinking");
          final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
          final int _cursorIndexOfOutput = CursorUtil.getColumnIndexOrThrow(_cursor, "output");
          final int _cursorIndexOfVerdictType = CursorUtil.getColumnIndexOrThrow(_cursor, "verdictType");
          final int _cursorIndexOfVerdictReason = CursorUtil.getColumnIndexOrThrow(_cursor, "verdictReason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AgentMessage> _result = new ArrayList<AgentMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AgentMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTaskId;
            _tmpTaskId = _cursor.getString(_cursorIndexOfTaskId);
            final String _tmpAgentId;
            _tmpAgentId = _cursor.getString(_cursorIndexOfAgentId);
            final String _tmpAgentName;
            _tmpAgentName = _cursor.getString(_cursorIndexOfAgentName);
            final String _tmpMessageText;
            _tmpMessageText = _cursor.getString(_cursorIndexOfMessageText);
            final String _tmpThinking;
            if (_cursor.isNull(_cursorIndexOfThinking)) {
              _tmpThinking = null;
            } else {
              _tmpThinking = _cursor.getString(_cursorIndexOfThinking);
            }
            final String _tmpActions;
            if (_cursor.isNull(_cursorIndexOfActions)) {
              _tmpActions = null;
            } else {
              _tmpActions = _cursor.getString(_cursorIndexOfActions);
            }
            final String _tmpOutput;
            if (_cursor.isNull(_cursorIndexOfOutput)) {
              _tmpOutput = null;
            } else {
              _tmpOutput = _cursor.getString(_cursorIndexOfOutput);
            }
            final String _tmpVerdictType;
            _tmpVerdictType = _cursor.getString(_cursorIndexOfVerdictType);
            final String _tmpVerdictReason;
            if (_cursor.isNull(_cursorIndexOfVerdictReason)) {
              _tmpVerdictReason = null;
            } else {
              _tmpVerdictReason = _cursor.getString(_cursorIndexOfVerdictReason);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AgentMessage(_tmpId,_tmpTaskId,_tmpAgentId,_tmpAgentName,_tmpMessageText,_tmpThinking,_tmpActions,_tmpOutput,_tmpVerdictType,_tmpVerdictReason,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Project>> getAllProjects() {
    final String _sql = "SELECT * FROM projects ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"projects"}, new Callable<List<Project>>() {
      @Override
      @NonNull
      public List<Project> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfFilesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "filesJson");
          final int _cursorIndexOfApiSpentCents = CursorUtil.getColumnIndexOrThrow(_cursor, "apiSpentCents");
          final int _cursorIndexOfKnowledgeBaseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "knowledgeBaseJson");
          final List<Project> _result = new ArrayList<Project>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Project _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLocalPath;
            _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpFilesJson;
            _tmpFilesJson = _cursor.getString(_cursorIndexOfFilesJson);
            final long _tmpApiSpentCents;
            _tmpApiSpentCents = _cursor.getLong(_cursorIndexOfApiSpentCents);
            final String _tmpKnowledgeBaseJson;
            _tmpKnowledgeBaseJson = _cursor.getString(_cursorIndexOfKnowledgeBaseJson);
            _item = new Project(_tmpId,_tmpName,_tmpLocalPath,_tmpStatus,_tmpProgress,_tmpLastUpdated,_tmpFilesJson,_tmpApiSpentCents,_tmpKnowledgeBaseJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Project> getProjectById(final String projectId) {
    final String _sql = "SELECT * FROM projects WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, projectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"projects"}, new Callable<Project>() {
      @Override
      @Nullable
      public Project call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfFilesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "filesJson");
          final int _cursorIndexOfApiSpentCents = CursorUtil.getColumnIndexOrThrow(_cursor, "apiSpentCents");
          final int _cursorIndexOfKnowledgeBaseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "knowledgeBaseJson");
          final Project _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLocalPath;
            _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpFilesJson;
            _tmpFilesJson = _cursor.getString(_cursorIndexOfFilesJson);
            final long _tmpApiSpentCents;
            _tmpApiSpentCents = _cursor.getLong(_cursorIndexOfApiSpentCents);
            final String _tmpKnowledgeBaseJson;
            _tmpKnowledgeBaseJson = _cursor.getString(_cursorIndexOfKnowledgeBaseJson);
            _result = new Project(_tmpId,_tmpName,_tmpLocalPath,_tmpStatus,_tmpProgress,_tmpLastUpdated,_tmpFilesJson,_tmpApiSpentCents,_tmpKnowledgeBaseJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<ApiKeyConfig> getApiKeyById(final String id) {
    final String _sql = "SELECT * FROM api_keys WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"api_keys"}, new Callable<ApiKeyConfig>() {
      @Override
      @Nullable
      public ApiKeyConfig call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProviderName = CursorUtil.getColumnIndexOrThrow(_cursor, "providerName");
          final int _cursorIndexOfEncryptedKey = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedKey");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfPriorityWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "priorityWeight");
          final int _cursorIndexOfMonthlyBudgetCents = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudgetCents");
          final int _cursorIndexOfCurrentSpentCents = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpentCents");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLastUsedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedTimestamp");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final ApiKeyConfig _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProviderName;
            _tmpProviderName = _cursor.getString(_cursorIndexOfProviderName);
            final String _tmpEncryptedKey;
            _tmpEncryptedKey = _cursor.getString(_cursorIndexOfEncryptedKey);
            final String _tmpBaseUrl;
            if (_cursor.isNull(_cursorIndexOfBaseUrl)) {
              _tmpBaseUrl = null;
            } else {
              _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            }
            final int _tmpPriorityWeight;
            _tmpPriorityWeight = _cursor.getInt(_cursorIndexOfPriorityWeight);
            final Long _tmpMonthlyBudgetCents;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudgetCents)) {
              _tmpMonthlyBudgetCents = null;
            } else {
              _tmpMonthlyBudgetCents = _cursor.getLong(_cursorIndexOfMonthlyBudgetCents);
            }
            final long _tmpCurrentSpentCents;
            _tmpCurrentSpentCents = _cursor.getLong(_cursorIndexOfCurrentSpentCents);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final long _tmpLastUsedTimestamp;
            _tmpLastUsedTimestamp = _cursor.getLong(_cursorIndexOfLastUsedTimestamp);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            _result = new ApiKeyConfig(_tmpId,_tmpProviderName,_tmpEncryptedKey,_tmpBaseUrl,_tmpPriorityWeight,_tmpMonthlyBudgetCents,_tmpCurrentSpentCents,_tmpIsEnabled,_tmpLastUsedTimestamp,_tmpLastError);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ApiKeyConfig>> getEnabledApiKeys() {
    final String _sql = "SELECT * FROM api_keys WHERE isEnabled = 1 ORDER BY priorityWeight DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"api_keys"}, new Callable<List<ApiKeyConfig>>() {
      @Override
      @NonNull
      public List<ApiKeyConfig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProviderName = CursorUtil.getColumnIndexOrThrow(_cursor, "providerName");
          final int _cursorIndexOfEncryptedKey = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedKey");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfPriorityWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "priorityWeight");
          final int _cursorIndexOfMonthlyBudgetCents = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudgetCents");
          final int _cursorIndexOfCurrentSpentCents = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpentCents");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLastUsedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedTimestamp");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final List<ApiKeyConfig> _result = new ArrayList<ApiKeyConfig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ApiKeyConfig _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProviderName;
            _tmpProviderName = _cursor.getString(_cursorIndexOfProviderName);
            final String _tmpEncryptedKey;
            _tmpEncryptedKey = _cursor.getString(_cursorIndexOfEncryptedKey);
            final String _tmpBaseUrl;
            if (_cursor.isNull(_cursorIndexOfBaseUrl)) {
              _tmpBaseUrl = null;
            } else {
              _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            }
            final int _tmpPriorityWeight;
            _tmpPriorityWeight = _cursor.getInt(_cursorIndexOfPriorityWeight);
            final Long _tmpMonthlyBudgetCents;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudgetCents)) {
              _tmpMonthlyBudgetCents = null;
            } else {
              _tmpMonthlyBudgetCents = _cursor.getLong(_cursorIndexOfMonthlyBudgetCents);
            }
            final long _tmpCurrentSpentCents;
            _tmpCurrentSpentCents = _cursor.getLong(_cursorIndexOfCurrentSpentCents);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final long _tmpLastUsedTimestamp;
            _tmpLastUsedTimestamp = _cursor.getLong(_cursorIndexOfLastUsedTimestamp);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            _item = new ApiKeyConfig(_tmpId,_tmpProviderName,_tmpEncryptedKey,_tmpBaseUrl,_tmpPriorityWeight,_tmpMonthlyBudgetCents,_tmpCurrentSpentCents,_tmpIsEnabled,_tmpLastUsedTimestamp,_tmpLastError);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SandboxRun>> getSandboxRunsByTask(final String taskId) {
    final String _sql = "SELECT * FROM sandbox_runs WHERE taskId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sandbox_runs"}, new Callable<List<SandboxRun>>() {
      @Override
      @NonNull
      public List<SandboxRun> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfBuildSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "buildSuccess");
          final int _cursorIndexOfTestsSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "testsSuccess");
          final int _cursorIndexOfCompileLog = CursorUtil.getColumnIndexOrThrow(_cursor, "compileLog");
          final int _cursorIndexOfTestLog = CursorUtil.getColumnIndexOrThrow(_cursor, "testLog");
          final int _cursorIndexOfDiffSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "diffSummary");
          final int _cursorIndexOfIsApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "isApplied");
          final int _cursorIndexOfIsIgnored = CursorUtil.getColumnIndexOrThrow(_cursor, "isIgnored");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<SandboxRun> _result = new ArrayList<SandboxRun>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SandboxRun _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTaskId;
            _tmpTaskId = _cursor.getString(_cursorIndexOfTaskId);
            final boolean _tmpBuildSuccess;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfBuildSuccess);
            _tmpBuildSuccess = _tmp != 0;
            final boolean _tmpTestsSuccess;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfTestsSuccess);
            _tmpTestsSuccess = _tmp_1 != 0;
            final String _tmpCompileLog;
            _tmpCompileLog = _cursor.getString(_cursorIndexOfCompileLog);
            final String _tmpTestLog;
            _tmpTestLog = _cursor.getString(_cursorIndexOfTestLog);
            final String _tmpDiffSummary;
            _tmpDiffSummary = _cursor.getString(_cursorIndexOfDiffSummary);
            final boolean _tmpIsApplied;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsApplied);
            _tmpIsApplied = _tmp_2 != 0;
            final boolean _tmpIsIgnored;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsIgnored);
            _tmpIsIgnored = _tmp_3 != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new SandboxRun(_tmpId,_tmpTaskId,_tmpBuildSuccess,_tmpTestsSuccess,_tmpCompileLog,_tmpTestLog,_tmpDiffSummary,_tmpIsApplied,_tmpIsIgnored,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
