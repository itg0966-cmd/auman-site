package com.ayman.waterbills.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\n"}, d2 = {"Lcom/ayman/waterbills/data/AppDb;", "Landroidx/room/RoomDatabase;", "()V", "buildingDao", "Lcom/ayman/waterbills/data/BuildingDao;", "meterDao", "Lcom/ayman/waterbills/data/MeterDao;", "tenantDao", "Lcom/ayman/waterbills/data/TenantDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.ayman.waterbills.data.Building.class, com.ayman.waterbills.data.Tenant.class, com.ayman.waterbills.data.MeterRecord.class}, version = 1, exportSchema = false)
public abstract class AppDb extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final com.ayman.waterbills.data.AppDb.Companion Companion = null;
    
    public AppDb() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ayman.waterbills.data.BuildingDao buildingDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ayman.waterbills.data.TenantDao tenantDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ayman.waterbills.data.MeterDao meterDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ayman/waterbills/data/AppDb$Companion;", "", "()V", "create", "Lcom/ayman/waterbills/data/AppDb;", "ctx", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ayman.waterbills.data.AppDb create(@org.jetbrains.annotations.NotNull()
        android.content.Context ctx) {
            return null;
        }
    }
}