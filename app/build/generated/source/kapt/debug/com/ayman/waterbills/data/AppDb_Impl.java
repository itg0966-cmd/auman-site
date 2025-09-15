package com.ayman.waterbills.data;

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
public final class AppDb_Impl extends AppDb {
  private volatile BuildingDao _buildingDao;

  private volatile TenantDao _tenantDao;

  private volatile MeterDao _meterDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Building` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `apts` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Tenant` (`buildingId` TEXT NOT NULL, `aptNo` INTEGER NOT NULL, `name` TEXT NOT NULL, `phone` TEXT NOT NULL, PRIMARY KEY(`buildingId`, `aptNo`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `MeterRecord` (`buildingId` TEXT NOT NULL, `aptNo` INTEGER NOT NULL, `year` INTEGER NOT NULL, `month` INTEGER NOT NULL, `prevReading` INTEGER NOT NULL, `currReading` INTEGER NOT NULL, `unitPrice` INTEGER NOT NULL, `periodValue` INTEGER NOT NULL, `arrears` INTEGER NOT NULL, `dueAmount` INTEGER NOT NULL, `paid` INTEGER NOT NULL, `fullPaid` INTEGER NOT NULL, `note` TEXT NOT NULL, `payDate` TEXT NOT NULL, `toDad` INTEGER NOT NULL, `dadDate` TEXT NOT NULL, PRIMARY KEY(`buildingId`, `aptNo`, `year`, `month`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8cea6b4541fd9ef453d5d49b91ddfb1b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Building`");
        db.execSQL("DROP TABLE IF EXISTS `Tenant`");
        db.execSQL("DROP TABLE IF EXISTS `MeterRecord`");
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
        final HashMap<String, TableInfo.Column> _columnsBuilding = new HashMap<String, TableInfo.Column>(3);
        _columnsBuilding.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuilding.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuilding.put("apts", new TableInfo.Column("apts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBuilding = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBuilding = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBuilding = new TableInfo("Building", _columnsBuilding, _foreignKeysBuilding, _indicesBuilding);
        final TableInfo _existingBuilding = TableInfo.read(db, "Building");
        if (!_infoBuilding.equals(_existingBuilding)) {
          return new RoomOpenHelper.ValidationResult(false, "Building(com.ayman.waterbills.data.Building).\n"
                  + " Expected:\n" + _infoBuilding + "\n"
                  + " Found:\n" + _existingBuilding);
        }
        final HashMap<String, TableInfo.Column> _columnsTenant = new HashMap<String, TableInfo.Column>(4);
        _columnsTenant.put("buildingId", new TableInfo.Column("buildingId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTenant.put("aptNo", new TableInfo.Column("aptNo", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTenant.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTenant.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTenant = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTenant = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTenant = new TableInfo("Tenant", _columnsTenant, _foreignKeysTenant, _indicesTenant);
        final TableInfo _existingTenant = TableInfo.read(db, "Tenant");
        if (!_infoTenant.equals(_existingTenant)) {
          return new RoomOpenHelper.ValidationResult(false, "Tenant(com.ayman.waterbills.data.Tenant).\n"
                  + " Expected:\n" + _infoTenant + "\n"
                  + " Found:\n" + _existingTenant);
        }
        final HashMap<String, TableInfo.Column> _columnsMeterRecord = new HashMap<String, TableInfo.Column>(16);
        _columnsMeterRecord.put("buildingId", new TableInfo.Column("buildingId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("aptNo", new TableInfo.Column("aptNo", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("year", new TableInfo.Column("year", "INTEGER", true, 3, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("month", new TableInfo.Column("month", "INTEGER", true, 4, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("prevReading", new TableInfo.Column("prevReading", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("currReading", new TableInfo.Column("currReading", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("unitPrice", new TableInfo.Column("unitPrice", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("periodValue", new TableInfo.Column("periodValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("arrears", new TableInfo.Column("arrears", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("dueAmount", new TableInfo.Column("dueAmount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("paid", new TableInfo.Column("paid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("fullPaid", new TableInfo.Column("fullPaid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("payDate", new TableInfo.Column("payDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("toDad", new TableInfo.Column("toDad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeterRecord.put("dadDate", new TableInfo.Column("dadDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeterRecord = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeterRecord = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeterRecord = new TableInfo("MeterRecord", _columnsMeterRecord, _foreignKeysMeterRecord, _indicesMeterRecord);
        final TableInfo _existingMeterRecord = TableInfo.read(db, "MeterRecord");
        if (!_infoMeterRecord.equals(_existingMeterRecord)) {
          return new RoomOpenHelper.ValidationResult(false, "MeterRecord(com.ayman.waterbills.data.MeterRecord).\n"
                  + " Expected:\n" + _infoMeterRecord + "\n"
                  + " Found:\n" + _existingMeterRecord);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8cea6b4541fd9ef453d5d49b91ddfb1b", "91c39ce82ad6ac8111fdc8ad3ce4843e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Building","Tenant","MeterRecord");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Building`");
      _db.execSQL("DELETE FROM `Tenant`");
      _db.execSQL("DELETE FROM `MeterRecord`");
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
    _typeConvertersMap.put(BuildingDao.class, BuildingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TenantDao.class, TenantDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MeterDao.class, MeterDao_Impl.getRequiredConverters());
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
  public BuildingDao buildingDao() {
    if (_buildingDao != null) {
      return _buildingDao;
    } else {
      synchronized(this) {
        if(_buildingDao == null) {
          _buildingDao = new BuildingDao_Impl(this);
        }
        return _buildingDao;
      }
    }
  }

  @Override
  public TenantDao tenantDao() {
    if (_tenantDao != null) {
      return _tenantDao;
    } else {
      synchronized(this) {
        if(_tenantDao == null) {
          _tenantDao = new TenantDao_Impl(this);
        }
        return _tenantDao;
      }
    }
  }

  @Override
  public MeterDao meterDao() {
    if (_meterDao != null) {
      return _meterDao;
    } else {
      synchronized(this) {
        if(_meterDao == null) {
          _meterDao = new MeterDao_Impl(this);
        }
        return _meterDao;
      }
    }
  }
}
