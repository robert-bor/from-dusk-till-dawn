package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ZoneActionPeekText extends ZoneActionManualUpdate {

    private String peekText;
    
    @Override
    protected void modifyZone(Zone zone) throws ApplicationException {
        if (getPeekText().length() > 100) {
            throw new ApplicationException(D2NErrorCode.PEEK_TEXT_TOO_LONG);
        }
        zone.setScoutPeek(getPeekText());
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.SAVE_PEEK;
    }
    
    public ZoneActionPeekText setPeekText(final String peekText) {
        this.peekText = peekText;
        return this;
    }
    public String getPeekText() throws ApplicationException {
        if (this.peekText == null) { throw new ApplicationException(D2NErrorCode.SYSTEM_ERROR, "No peek text"); }
        return this.peekText;
    }
}
