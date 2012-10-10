package nl.d2n.model;

public enum DiscoveryStatus {
    UNDISCOVERED(0),
    DISCOVERED_NOT_VISITED_TODAY(1),
    DISCOVERED_AND_VISITED_TODAY(2);

    private int priority;

    private DiscoveryStatus(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return this.priority;
    }
    public DiscoveryStatus getHighestDiscoveryStatus(DiscoveryStatus discoveryStatus) {
        if (discoveryStatus == null) {
            return this;
        }
        return getPriority() > discoveryStatus.getPriority() ? this : discoveryStatus;
    }
}
