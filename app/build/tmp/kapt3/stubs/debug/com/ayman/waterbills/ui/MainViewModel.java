package com.ayman.waterbills.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0014J\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u0017J\u0016\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0006\u0010 \u001a\u00020\u0014R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006!"}, d2 = {"Lcom/ayman/waterbills/ui/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/ayman/waterbills/repo/Repo;", "(Lcom/ayman/waterbills/repo/Repo;)V", "_ui", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ayman/waterbills/ui/UiState;", "ui", "Lkotlinx/coroutines/flow/StateFlow;", "getUi", "()Lkotlinx/coroutines/flow/StateFlow;", "refresh", "Lkotlinx/coroutines/Job;", "save", "r", "Lcom/ayman/waterbills/data/MeterRecord;", "setBuilding", "", "id", "", "setMonth", "m", "", "setQuery", "q", "setYear", "y", "tenantName", "apt", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "tenantPhone", "today", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.ayman.waterbills.repo.Repo repo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ayman.waterbills.ui.UiState> _ui = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.ayman.waterbills.ui.UiState> ui = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.repo.Repo repo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.ayman.waterbills.ui.UiState> getUi() {
        return null;
    }
    
    public final void setMonth(int m) {
    }
    
    public final void setYear(int y) {
    }
    
    public final void setBuilding(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void setQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String q) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job refresh() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job save(@org.jetbrains.annotations.NotNull()
    com.ayman.waterbills.data.MeterRecord r) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object tenantName(int apt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object tenantPhone(int apt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String today() {
        return null;
    }
}