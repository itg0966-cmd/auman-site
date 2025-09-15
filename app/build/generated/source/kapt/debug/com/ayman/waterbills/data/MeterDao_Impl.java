package com.ayman.waterbills.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MeterDao_Impl implements MeterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MeterRecord> __insertionAdapterOfMeterRecord;

  public MeterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeterRecord = new EntityInsertionAdapter<MeterRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `MeterRecord` (`buildingId`,`aptNo`,`year`,`month`,`prevReading`,`currReading`,`unitPrice`,`periodValue`,`arrears`,`dueAmount`,`paid`,`fullPaid`,`note`,`payDate`,`toDad`,`dadDate`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MeterRecord entity) {
        if (entity.getBuildingId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBuildingId());
        }
        statement.bindLong(2, entity.getAptNo());
        statement.bindLong(3, entity.getYear());
        statement.bindLong(4, entity.getMonth());
        statement.bindLong(5, entity.getPrevReading());
        statement.bindLong(6, entity.getCurrReading());
        statement.bindLong(7, entity.getUnitPrice());
        statement.bindLong(8, entity.getPeriodValue());
        statement.bindLong(9, entity.getArrears());
        statement.bindLong(10, entity.getDueAmount());
        statement.bindLong(11, entity.getPaid());
        final int _tmp = entity.getFullPaid() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getNote() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNote());
        }
        if (entity.getPayDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPayDate());
        }
        final int _tmp_1 = entity.getToDad() ? 1 : 0;
        statement.bindLong(15, _tmp_1);
        if (entity.getDadDate() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getDadDate());
        }
      }
    };
  }

  @Override
  public Object upsert(final MeterRecord r, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMeterRecord.insert(r);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object monthOf(final String b, final int y, final int m,
      final Continuation<? super List<MeterRecord>> $completion) {
    final String _sql = "SELECT * FROM MeterRecord WHERE buildingId=? AND year=? AND month=? ORDER BY aptNo";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (b == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, b);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, y);
    _argIndex = 3;
    _statement.bindLong(_argIndex, m);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MeterRecord>>() {
      @Override
      @NonNull
      public List<MeterRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBuildingId = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingId");
          final int _cursorIndexOfAptNo = CursorUtil.getColumnIndexOrThrow(_cursor, "aptNo");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfPrevReading = CursorUtil.getColumnIndexOrThrow(_cursor, "prevReading");
          final int _cursorIndexOfCurrReading = CursorUtil.getColumnIndexOrThrow(_cursor, "currReading");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfPeriodValue = CursorUtil.getColumnIndexOrThrow(_cursor, "periodValue");
          final int _cursorIndexOfArrears = CursorUtil.getColumnIndexOrThrow(_cursor, "arrears");
          final int _cursorIndexOfDueAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAmount");
          final int _cursorIndexOfPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "paid");
          final int _cursorIndexOfFullPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "fullPaid");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfPayDate = CursorUtil.getColumnIndexOrThrow(_cursor, "payDate");
          final int _cursorIndexOfToDad = CursorUtil.getColumnIndexOrThrow(_cursor, "toDad");
          final int _cursorIndexOfDadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dadDate");
          final List<MeterRecord> _result = new ArrayList<MeterRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MeterRecord _item;
            final String _tmpBuildingId;
            if (_cursor.isNull(_cursorIndexOfBuildingId)) {
              _tmpBuildingId = null;
            } else {
              _tmpBuildingId = _cursor.getString(_cursorIndexOfBuildingId);
            }
            final int _tmpAptNo;
            _tmpAptNo = _cursor.getInt(_cursorIndexOfAptNo);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpMonth;
            _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
            final int _tmpPrevReading;
            _tmpPrevReading = _cursor.getInt(_cursorIndexOfPrevReading);
            final int _tmpCurrReading;
            _tmpCurrReading = _cursor.getInt(_cursorIndexOfCurrReading);
            final int _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
            final int _tmpPeriodValue;
            _tmpPeriodValue = _cursor.getInt(_cursorIndexOfPeriodValue);
            final int _tmpArrears;
            _tmpArrears = _cursor.getInt(_cursorIndexOfArrears);
            final int _tmpDueAmount;
            _tmpDueAmount = _cursor.getInt(_cursorIndexOfDueAmount);
            final int _tmpPaid;
            _tmpPaid = _cursor.getInt(_cursorIndexOfPaid);
            final boolean _tmpFullPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFullPaid);
            _tmpFullPaid = _tmp != 0;
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpPayDate;
            if (_cursor.isNull(_cursorIndexOfPayDate)) {
              _tmpPayDate = null;
            } else {
              _tmpPayDate = _cursor.getString(_cursorIndexOfPayDate);
            }
            final boolean _tmpToDad;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfToDad);
            _tmpToDad = _tmp_1 != 0;
            final String _tmpDadDate;
            if (_cursor.isNull(_cursorIndexOfDadDate)) {
              _tmpDadDate = null;
            } else {
              _tmpDadDate = _cursor.getString(_cursorIndexOfDadDate);
            }
            _item = new MeterRecord(_tmpBuildingId,_tmpAptNo,_tmpYear,_tmpMonth,_tmpPrevReading,_tmpCurrReading,_tmpUnitPrice,_tmpPeriodValue,_tmpArrears,_tmpDueAmount,_tmpPaid,_tmpFullPaid,_tmpNote,_tmpPayDate,_tmpToDad,_tmpDadDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object sumDue(final String b, final int y, final int m,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(dueAmount),0) FROM MeterRecord WHERE buildingId=? AND year=? AND month=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (b == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, b);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, y);
    _argIndex = 3;
    _statement.bindLong(_argIndex, m);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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

  @Override
  public Object sumPaid(final String b, final int y, final int m,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(paid),0) FROM MeterRecord WHERE buildingId=? AND year=? AND month=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (b == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, b);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, y);
    _argIndex = 3;
    _statement.bindLong(_argIndex, m);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
