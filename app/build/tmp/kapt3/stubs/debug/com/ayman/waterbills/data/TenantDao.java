package com.ayman.waterbills.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/ayman/waterbills/data/TenantDao;", "", "get", "Lcom/ayman/waterbills/data/Tenant;", "b", "", "apt", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsert", "", "t", "(Lcom/ayman/waterbills/data/Tenant;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface TenantDao {
    
    @androidx.room.Query(value = "SELECT * FROM Tenant WHERE buildingId=:b AND aptNo=:apt")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object get(@org.jetbrains.annotations.NotNull()
    java.lang.String b, int apt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.ayman.waterbills.data.Tenant> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.Tenant t, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}