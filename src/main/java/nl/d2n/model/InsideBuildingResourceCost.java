package nl.d2n.model;

import com.google.gson.annotations.Expose;

public class InsideBuildingResourceCost {

    @Expose
    private Integer itemId;

    @Expose
    private Integer amount;

    public Integer getItemId() { return this.itemId; }
    public Integer getAmount() { return this.amount; }

    public void setItemId(Integer itemId) { this.itemId = itemId; }
    public void setAmount(Integer amount) { this.amount = amount; }
}
