package nl.d2n.model;

public enum InfoStatus {
    READ_FROM_SITE  (false, true),
    READ_FROM_CACHE (true,  false),
    READ_FROM_FILE  (true,  true);

    private boolean stale;
    private boolean enhance;

    private InfoStatus(final boolean stale, final boolean enhance) {
        this.stale = stale;
        this.enhance = enhance;
    }
    public boolean isStale() { return this.stale; }
    public boolean requiresEnhancing() { return this.enhance; }
}
