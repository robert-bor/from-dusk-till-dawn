package nl.d2n.dao.result;

import com.google.gson.annotations.Expose;

public class UserWithDistinction {

    @Expose
    private Integer userGameId;
    @Expose
    private String userName;
    @Expose
    private int amount;

    public UserWithDistinction(Integer userGameId, String userName, int amount) {
        this.userGameId = userGameId;
        this.userName = userName;
        this.amount = amount;
    }

    public Integer getUserGameId() {
        return userGameId;
    }

    public String getUserName() {
        return userName;
    }

    public int getAmount() {
        return amount;
    }
}
