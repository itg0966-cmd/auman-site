package com.ayman.waterbills.repo;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J,\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000e2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u001bJ&\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010!J&\u0010\"\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010!J8\u0010$\u001a\u0014\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00150%2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\'J\u001e\u0010(\u001a\u00020\u001e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010)J\u001e\u0010*\u001a\u00020\u001e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010)J\u0006\u0010+\u001a\u00020\u001eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/ayman/waterbills/repo/Repo;", "", "db", "Lcom/ayman/waterbills/data/AppDb;", "(Lcom/ayman/waterbills/data/AppDb;)V", "b", "Lcom/ayman/waterbills/data/BuildingDao;", "getDb", "()Lcom/ayman/waterbills/data/AppDb;", "m", "Lcom/ayman/waterbills/data/MeterDao;", "t", "Lcom/ayman/waterbills/data/TenantDao;", "buildings", "", "Lcom/ayman/waterbills/data/Building;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "month", "Lcom/ayman/waterbills/data/MeterRecord;", "building", "y", "", "mo", "(Lcom/ayman/waterbills/data/Building;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "save", "", "r", "(Lcom/ayman/waterbills/data/MeterRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTenantName", "bid", "", "apt", "name", "(Ljava/lang/String;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTenantPhone", "phone", "sums", "Lkotlin/Triple;", "mth", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "tenantName", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "tenantPhone", "today", "app_debug"})
public final class Repo {
    @org.jetbrains.annotations.NotNull()
    private final com.ayman.waterbills.data.AppDb db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ayman.waterbills.data.BuildingDao b = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ayman.waterbills.data.TenantDao t = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ayman.waterbills.data.MeterDao m = null;
    
    public Repo(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.AppDb db) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ayman.waterbills.data.AppDb getDb() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object buildings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.ayman.waterbills.data.Building>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object month(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.Building building, int y, int mo, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.ayman.waterbills.data.MeterRecord>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object tenantName(@org.jetbrains.annotations.NotNull()
    java.lang.String bid, int apt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setTenantName(@org.jetbrains.annotations.NotNull()
    java.lang.String bid, int apt, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setTenantPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String bid, int apt, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object tenantPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String bid, int apt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object save(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.MeterRecord r, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sums(@org.jetbrains.annotations.NotNull()
    java.lang.String bid, int y, int mth, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Triple<java.lang.Integer, java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String today() {
        return null;
    }
}