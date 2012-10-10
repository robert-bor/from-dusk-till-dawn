package nl.d2n.dao.result;

import java.util.Date;

public class LastUpdateByUser {

    private Integer id;

    private Date updated;

    public LastUpdateByUser(Integer id, Date updated) {
        this.id = id;
        this.updated = updated;
    }
    public Integer getId() {
        return this.id;
    }
    public Date getUpdated() {
        return this.updated;
    }
}
