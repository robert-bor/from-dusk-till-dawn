package nl.d2n.util;

import nl.d2n.model.Citizen;
import nl.d2n.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserCitizenConverter {

    public static List<Citizen> convertUsersToCitizens(User[] users) {
        List<Citizen> citizens = new ArrayList<Citizen>();
        for (User user : users) {
            Citizen citizen = new Citizen();
            citizen.setName(user.getName());
            citizen.setId(user.getGameId());
            citizens.add(citizen);
        }
        return citizens;
    }

    public static List<Citizen> convertUserToCitizen(User user) {
        return convertUsersToCitizens(new User[] { user } );
    }
}
