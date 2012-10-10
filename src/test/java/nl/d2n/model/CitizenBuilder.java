package nl.d2n.model;

public class CitizenBuilder {

    public static Citizen createCitizen(String name, Integer id) {
        Citizen citizen = new Citizen();
        citizen.setName(name);
        citizen.setId(id);
        return citizen;
    }
}
