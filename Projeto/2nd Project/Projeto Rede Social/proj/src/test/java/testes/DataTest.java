package testes;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class DataTest {
    private DataReader reader, reader_big;
    @Before
    public void setup() {
        reader = new DataReader(
                "smallnetwork/scountries.txt",
                "smallnetwork/sborders.txt",
                "smallnetwork/susers.txt",
                "smallnetwork/srelationships.txt"
        );
        reader_big = new DataReader(
                "bignetwork/bcountries.txt",
                "bignetwork/bborders.txt",
                "bignetwork/busers.txt",
                "bignetwork/brelationships.txt"
        );
    }

    @Test
    public void testCountry_Small() {
        Country country = new Country("brasil", "americasul", 206.12, "brasilia", -15.7797200, -47.9297200);
        Country country1 = new Country("colombia", "americasul", 46.86, "bogota", 4.6097100, -74.0817500);
        Country country2 = new Country("guianafrancesa", "americasul", 2.88, "caiena", 4.9333300, -52.3333300);
        List<Country> countries = Utils.getCountriesFromGraph(this.reader.getCountriesGraph());

        assertTrue(countries.contains(country));
        assertTrue(countries.contains(country1));
        assertTrue(countries.contains(country2));

    }

    @Test
    public void testCountry_Big() {
        Country country = new Country("ucrania", "europa", 42.59, "kiev", 50.440951, 30.5271814);
        Country country1 = new Country("hungria", "europa", 9.8, "budapeste",47.4984056,19.0407578);
        Country country2 = new Country("moldavia", "europa", 3.55, "chisinau", 47.026859, 28.841551);
        List<Country> countries = Utils.getCountriesFromGraph(this.reader_big.getCountriesGraph());

        assertTrue(countries.contains(country));
        assertTrue(countries.contains(country1));
        assertTrue(countries.contains(country2));

    }

    @Test
    public void testBorder_Small(){
        Border border = new Border("argentina","chile");
        Border border1 = new Border("brasil","venezuela");
        Border border2 = new Border("equador","peru");
        Border border3 = new Border("argentina", "peru");
        List<Border> borders = Utils.getBordersFromGraph(reader.getCountriesGraph());

        assertTrue(borders.contains(border));
        assertTrue(borders.contains(border1));
        assertTrue(borders.contains(border2));
        assertFalse(borders.contains(border3));
    }

    @Test
    public void testBorder_Big(){
        Border border = new Border("bulgaria", "romenia");
        Border border1 = new Border("bielorussia", "lituania");
        Border border2 = new Border("alemanha", "polonia");
        Border border3 = new Border("argentina", "peru");
        List<Border> borders = Utils.getBordersFromGraph(reader_big.getCountriesGraph());

        assertTrue(borders.contains(border));
        assertTrue(borders.contains(border1));
        assertTrue(borders.contains(border2));
        assertFalse(borders.contains(border3));
    }

    @Test
    public void userTest_Small() {
        User user = new User("u3",20,"quito");
        User user1 = new User("u31",52,"buenosaires");
        User user2 = new User("u35",23,"porto");
        List<User> users = Utils.getUsersFromMatrix(this.reader.getRelationshipsMatrix());

        assertTrue(users.contains(user));
        assertTrue(users.contains(user1));
        assertFalse(users.contains(user2));
    }

    @Test
    public void userTest_Big() {
        User user = new User("u769",16,"valletta");
        User user1 = new User("u480",30,"buenosaires");
        User user2 = new User("u800",23,"porto");
        List<User> users = Utils.getUsersFromMatrix(this.reader_big.getRelationshipsMatrix());

        assertTrue(users.contains(user));
        assertTrue(users.contains(user1));
        assertFalse(users.contains(user2));
    }

    @Test
    public void relationshipTest_Small() {
        Relationship rs = new Relationship("u9","u1");
        Relationship rs1 = new Relationship("u34","u32");
        Relationship rs2 = new Relationship("u35","u23");
        List<Relationship> relationships = Utils.getRelationshipsFromMatrix(this.reader.getRelationshipsMatrix());

        assertTrue(relationships.contains(rs));
        assertTrue(relationships.contains(rs1));
        assertFalse(relationships.contains(rs2));
    }

    @Test
    public void relationshipTest_Big() {
        Relationship rs = new Relationship("u249","u21");
        Relationship rs1 = new Relationship("u626","u133");
        Relationship rs2 = new Relationship("u769","u770");
        List<Relationship> relationships = Utils.getRelationshipsFromMatrix(this.reader_big.getRelationshipsMatrix());

        assertTrue(relationships.contains(rs));
        assertTrue(relationships.contains(rs1));
        assertFalse(relationships.contains(rs2));
    }

    /*
    2
     */
    @Test
    public void mostPopularUsers() {
        User u1 = new User("u1", 27, "brasilia");
        User u33 = new User("u33", 48, "brasilia");
        User u34 = new User("u34", 35, "caracas");

        Map<User, List<User>> popularN1 = Utils.getMostPopularUsers(this.reader.getRelationshipsMatrix(), 1);
        Map<User, List<User>> popularN2 = Utils.getMostPopularUsers(this.reader.getRelationshipsMatrix(), 2);
        Map<User, List<User>> popularN3 = Utils.getMostPopularUsers(this.reader.getRelationshipsMatrix(), 3);
//        System.out.println(popularN1.keySet());
//        System.out.println(popularN1.size());

//        N1:
        assertTrue(popularN1.containsKey(u34));
//        N2:
        assertTrue(popularN2.containsKey(u34));
        assertTrue(popularN2.containsKey(u33));
//        N3:
        assertTrue(popularN3.containsKey(u34));
        assertTrue(popularN3.containsKey(u33));
        assertTrue(popularN3.containsKey(u1));

        assertFalse(popularN1.containsKey(u1));
    }

    @Test
    public void commonFriends() {
        Map<User, List<User>> popular = Utils.getMostPopularUsers(this.reader.getRelationshipsMatrix(), 2);
        List<User> commonFriends = Utils.getCommon(popular);

        assertEquals(10, commonFriends.size());
//        System.out.println(commonFriends);
//        System.out.println(commonFriends.size());
    }

    /*
    3
     */

    @Test
    public void testMinNumberConnections_Small() {
        int connections = Utils.checkIfConnectedAndMinNum(this.reader.getRelationshipsMatrix());
        System.out.println("Conexões necessárias: " + connections);
    }

    @Test
    public void testMinNumberConnections_Big() {
        int connections = Utils.checkIfConnectedAndMinNum(this.reader_big.getRelationshipsMatrix());
        System.out.println("Conexões necessárias: " + connections);
    }

    /*
    4
     */
    @Test
    public void testNearFriends() {
        User u14 = new User("u14",22,"paramaribo");
        User u29 = new User("u29",13,"santiago");

        Map<Country, List<User>> listNearFriendsU14 = Utils.getNearFriends(this.reader.getRelationshipsMatrix(), this.reader.getCountriesGraph(), u14.getUser(), 1); // número de borders exatamente igual
        Map<Country, List<User>> listNearFriendsU29 = Utils.getNearFriends(this.reader.getRelationshipsMatrix(), this.reader.getCountriesGraph(), u29.getUser(), 2); // número de borders exatamente igual

//        System.out.println(listNearFriendsU14.size());
//        System.out.println(listNearFriendsU29.size());
        assertEquals(1, listNearFriendsU14.size());
        assertEquals(2, listNearFriendsU29.size());
    }

    /*
    5
     */
    @Test
    public void testCitiesMoreCentralized() {
        ArrayList<String> p5num2 = Utils.citiesMoreCentralized(this.reader.getRelationshipsMatrix(), this.reader.getCountriesGraph(), 5, 2);

        assertEquals(2, p5num2.size());
    }
}