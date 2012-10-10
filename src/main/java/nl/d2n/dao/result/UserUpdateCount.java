package nl.d2n.dao.result;

public class UserUpdateCount {

    private Integer id;

    private Integer reads;
    private Integer writes;

    public UserUpdateCount(Integer id, Integer reads, Integer writes) {
        this.id = id;
        this.reads = reads;
        this.writes = writes;
    }
    public Integer getId() {
        return this.id;
    }
    public Integer getReads() {
        return this.reads;
    }
    public Integer getWrites() {
        return this.writes;
    }
}
