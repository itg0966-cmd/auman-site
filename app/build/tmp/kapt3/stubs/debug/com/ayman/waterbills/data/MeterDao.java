package com.ayman.waterbills.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\nJ&\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\nJ&\u0010\f\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/ayman/waterbills/data/MeterDao;", "", "monthOf", "", "Lcom/ayman/waterbills/data/MeterRecord;", "b", "", "y", "", "m", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sumDue", "sumPaid", "upsert", "", "r", "(Lcom/ayman/waterbills/data/MeterRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface MeterDao {
    
    @androidx.room.Query(value = "SELECT * FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m ORDER BY aptNo")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object monthOf(@org.jetbrains.annotations.NotNull()
    java.lang.String b, int y, int m, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.ayman.waterbills.data.MeterRecord>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.MeterRecord r, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(dueAmount),0) FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sumDue(@org.jetbrains.annotations.NotNull()
    java.lang.String b, int y, int m, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(paid),0) FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sumPaid(@org.jetbrains.annotations.NotNull()
    java.lang.String b, int y, int m, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}