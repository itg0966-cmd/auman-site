package com.ayman.waterbills.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TenantDao_Impl implements TenantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Tenant> __insertionAdapterOfTenant;

  public TenantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTenant = new EntityInsertionAdapter<Tenant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `Tenant` (`buildingId`,`aptNo`,`name`,`phone`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tenant entity) {
        if (entity.getBuildingId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBuildingId());
        }
        statement.bindLong(2, entity.getAptNo());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
      }
    };
  }

  @Override
  public Object upsert(final Tenant t, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTenant.insert(t);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object get(final String b, final int apt, final Continuation<? super Tenant> $completion) {
    final String _sql = "SELECT * FROM Tenant WHERE buildingId=? AND aptNo=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (b == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, b);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, apt);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Tenant>() {
      @Override
      @Nullable
      public Tenant call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBuildingId = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingId");
          final int _cursorIndexOfAptNo = CursorUtil.getColumnIndexOrThrow(_cursor, "aptNo");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final Tenant _result;
          if (_cursor.moveToFirst()) {
            final String _tmpBuildingId;
            if (_cursor.isNull(_cursorIndexOfBuildingId)) {
              _tmpBuildingId = null;
            } else {
              _tmpBuildingId = _cursor.getString(_cursorIndexOfBuildingId);
            }
            final int _tmpAptNo;
            _tmpAptNo = _cursor.getInt(_cursorIndexOfAptNo);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            _result = new Tenant(_tmpBuildingId,_tmpAptNo,_tmpName,_tmpPhone);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
