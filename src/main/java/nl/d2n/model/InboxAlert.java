package nl.d2n.model;

import com.google.gson.annotations.Expose;

public class InboxAlert {

    @Expose private Integer messages;
    @Expose private Integer invitations;
    private Integer userid;
    private String tempkey;
    private Long valid;
    @Expose private String oourl;

    public Integer getMessages() { return this.messages; }
    public Integer getInvitations() { return this.invitations; }
    public Integer getUserId() { return this.userid; }
    public String getTempkey() { return this.tempkey; }
    public Long getValid() { return this.valid; }
    public String getOourl() { return this.oourl; }
}
