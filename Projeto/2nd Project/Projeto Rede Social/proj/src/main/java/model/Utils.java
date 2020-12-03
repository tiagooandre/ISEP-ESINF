package model;

import graphbase.Edge;
import graphbase.Graph;
import graphbase.GraphAlgorithms;
import matrixGraph.AdjacencyMatrixGraph;
import matrixGraph.EdgeAsDoubleGraphAlgorithms;

import java.lang.reflect.Array;
import java.nio.charset.MalformedInputException;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static matrixGraph.GraphAlgorithms.BFS;

public class Utils {
    /*
    1. Construir os grafos a partir da informação fornecida nos ficheiros de texto.
    A capital de um país tem ligação direta com as capitais dos países com os quais
    faz fronteira. O cálculo da distância em Kms entre duas capitais deverá ser feito
    através das suas coordenadas.
     */

    public static Country getCountryFromGraph(Graph<Country, Border> graph, String country) {
        for (Country c : graph.vertices()) {
            if (c.getName().equals(country)) return c;
        }
        return null;
    }

    public static double distanceTwoCapitals(Country c1, Country c2) {
        double R = 6371e3;

        double a = Math.sin((c1.getLatitude() * Math.PI/180)/2) * Math.sin(((c2.getLatitude()-c1.getLatitude()) * Math.PI/180)/2)
                + Math.cos((c1.getLatitude() * Math.PI/180)) * Math.cos((c2.getLatitude() * Math.PI/180))
                * Math.sin(((c2.getLongitude()-c1.getLongitude()) * Math.PI/180)/2)
                * Math.sin(((c2.getLongitude()-c1.getLongitude()) * Math.PI/180)/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return Math.round(R * c);
    }

    public static List<Country> getCountriesFromGraph(Graph<Country, Border> graph) {
        List<Country> countries = new ArrayList<>();
        for (Country c : graph.vertices()) {
            countries.add(c);
        }
        return countries;
    }
    public static List<Border> getBordersFromGraph(Graph<Country, Border> graph) {
        List<Border> borders = new ArrayList<>();
        for (Edge<Country, Border> c : graph.edges()) {
            borders.add(new Border(c.getVOrig().getName(), c.getVDest().getName()));
        }
        return borders;
    }

    /*
    2. Devolver os amigos comuns entre os n utilizadores mais populares da rede.
    A popularidade de um utilizador é dada pelo seu número de amizades.
    */

    public static User getUserFromMatrix(AdjacencyMatrixGraph<User, Relationship> matrixGraph, String name) {
        for (User u : matrixGraph.vertices()) {
            if (u.getUser().equals(name)) {
                return u;
            }
        }
        return null;
    }

    public static List<User> getUsersFromMatrix(AdjacencyMatrixGraph<User, Relationship> matrixGraph) {
        List<User> users = new ArrayList<>();
        for (User u : matrixGraph.vertices()) {
            users.add(u);
        }
        return users;
    }

    public static List<Relationship> getRelationshipsFromMatrix(AdjacencyMatrixGraph<User, Relationship> matrixGraph) {
        List<Relationship> relationships = new ArrayList<>();
        for (Relationship r : matrixGraph.edges()) {
            relationships.add(r);
        }
        return relationships;
    }

    public static Map<User, List<User>> getMostPopularUsers(AdjacencyMatrixGraph<User, Relationship> matrixGraph, int N) {
        Map<User, List<User>> users = new HashMap<>();

        for (User user : matrixGraph.vertices()) {
            List<User> friends = new ArrayList<>();
            for (User user1 : matrixGraph.vertices()) {
                if (matrixGraph.getEdge(user, user1) != null) {
                    friends.add(user1);
                }
            }
            user.setNumberOfFriends(friends.size());
            users.put(user, friends);
        }

        Map<User, List<User>> treeMap = new TreeMap<>((o1, o2) -> o2.getNumberOfFriends() - o1.getNumberOfFriends());
        treeMap.putAll(users);

        Map<User, List<User>> toReturn = new HashMap<>();
        int count = 0;
        for (Map.Entry<User,List<User>> entry : treeMap.entrySet()) {
            if (count == N) break;
            count++;
            toReturn.put(entry.getKey(), entry.getValue());
        }
        return toReturn;
    }

    public static List<User> getCommon(Map<User, List<User>> map) {
        if (map.size() == 0) {
            return new ArrayList<>();
        }

        List<User> friends = new ArrayList<>();
        List<List<User>> aux = new ArrayList<>();
        aux.addAll(map.values());

        friends.addAll(aux.get(0));

        for (int i = 1; i < aux.size(); i++) {
            friends.retainAll(aux.get(i));
        }

        return friends;
    }

    public static int getCommonFriends(AdjacencyMatrixGraph<User, Relationship> matrixGraph, int N) {
        Map<User, List<User>> users = getMostPopularUsers(matrixGraph, N);
        List<User> commonFriends = getCommon(users);
        return commonFriends.size();
    }

    /*
    3. Verificar se a rede de amizades é conectada e em caso positivo devolver o número
    mínimo de ligações necessário para nesta rede qualquer utilizador conseguir contactar
    um qualquer outro utilizador.
     */

    public static int checkIfConnectedAndMinNum(AdjacencyMatrixGraph<User, Relationship> graph){
        LinkedList<User> ll;

        Set<Relationship> relationships = new HashSet<>();

        for (User u : graph.vertices()) {
            ll = matrixGraph.GraphAlgorithms.BFS(graph, u);
            assert ll != null;
            if (ll.size() == graph.numVertices()) {
                // determinar quantas ligações são necessárias para tornar o
                // grafo fortemente conexo (todos vertices ligados entre si)
                for (User u2 : graph.vertices()) {
                    if (u2 != u) {
                        LinkedList<User> linkedList = new LinkedList<>();
                        double size = EdgeAsDoubleGraphAlgorithms.shortestPath(graph, u, u2, linkedList);
                        if (size != 1.0) {
                            // para que não adicione amizades repetidas (u1-u2 = u2-u1)
                            if (!relationships.contains(new Relationship(u2.getUser(), u.getUser()))) {
                                relationships.add(new Relationship(u.getUser(), u2.getUser()));
//                                System.out.println("size: " + size);
//                                System.out.println("user1: " + u);
//                                System.out.println("user2: " + u2);
//                                System.out.println(linkedList);
                            }
                        }
                    }
                }
            }
            else return -1; // significa que não é conexo
        }

        return relationships.size();
    }


    /*
    4. Devolver para um utilizador os amigos que se encontrem nas proximidades,
    isto é, amigos que habitem em cidades que distam
    um dado número de fronteiras da cidade desse utilizador. Devolver para cada cidade os respetivos amigos.
    */

    public static Country getCountryFromGraphWithCapital (Graph<Country, Border> graph, String capital) {
        for (Country c : graph.vertices()) {
            if (c.getCapital().equals(capital)) {
                return c;
            }
        }
        return null;
    }

    public static Map<Country, List<User>> getNearFriends(AdjacencyMatrixGraph<User, Relationship> matrixGraph, Graph<Country, Border> graph, String username, int NumberOfBorders) {
        User user = null;
        for (User u : matrixGraph.vertices()) {
            if (u.getUser().equals(username)) {
                user = u;
                break;
            }
        }
        if (user == null) return null;
        List<User> friends = new ArrayList<>();
        for (Relationship relationshipEdge : matrixGraph.outgoingEdges(user)) {
            if (!relationshipEdge.getUser2().equals(username))
                friends.add(Utils.getUserFromMatrix(matrixGraph, relationshipEdge.getUser2()));
            else
                friends.add(Utils.getUserFromMatrix(matrixGraph, relationshipEdge.getUser1()));
        }

        Country userCountry = getCountryFromGraphWithCapital(graph, user.getCity());

//        LinkedList<Country> ll = graphbase.GraphAlgorithms.DepthFirstSearch(graph, userCountry);

        List<User> listUser = new ArrayList<>();

        Graph<Country, Border> auxGraphUnweighted = new Graph<>(false);
        for (Country c : graph.vertices())
            auxGraphUnweighted.insertVertex(c);
        for (Edge<Country, Border> edge : graph.edges())
            auxGraphUnweighted.insertEdge(edge.getVOrig(), edge.getVDest(), edge.getElement(), 1);


        for (User friend : friends) {
            LinkedList<Country> ll = new LinkedList<>();
            graphbase.GraphAlgorithms.shortestPath(auxGraphUnweighted, userCountry, getCountryFromGraphWithCapital(graph, friend.getCity()), ll); /* shortestPath */
            int depth = ll.size();

            if (depth - 1 == NumberOfBorders) {
                listUser.add(friend);
            }
        }

        Map<Country, List<User>> countriesFriends = new HashMap<>();
        Set<Country> listCities = new HashSet<>();
        for (User u : listUser) {
            listCities.add(getCountryFromGraphWithCapital(graph, u.getCity()));
        }

        for (Country c : listCities) {
            List<User> cityFriends = new ArrayList<>();
            for (User u : listUser) {
                if (u.getCity().equals(c.getCapital())) {
                    cityFriends.add(u);
                }
            }
            countriesFriends.put(c, cityFriends);
        }
        return countriesFriends;
    }

    /*
    5. Devolver as n cidades com maior centralidade ou seja, as cidades que em média estão mais
    próximas de todas as outras cidades e onde habitem pelo menos p% dos utilizadores da rede
    de amizades, onde p% é a percentagem relativa de utilizadores em cada cidade.
     */

    public static double avgNearby(String capital, Graph<Country,Border> graph){
        double total=0;
        int size = 0;
        for (Country c : graph.vertices()) {
            LinkedList<Country> lk = new LinkedList<>();
            size++;
            if (!c.getCapital().equals(capital))
                total = total + GraphAlgorithms.shortestPath(graph, getCountryFromGraphWithCapital(graph, capital), c, lk);
        }
        return total/(size - 1);
    }

    public static Map<String, Double> centralizedCity(Graph<Country,Border> graph){
        Map<String, Double> topCentralized = new HashMap<>();
        for(Country c : graph.vertices()) {
            topCentralized.put(c.getCapital(), avgNearby(c.getCapital(), graph));
        }
        return topCentralized.entrySet().stream().sorted(comparingByValue()).collect(
                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static ArrayList<String> morePercentage(AdjacencyMatrixGraph<User, Relationship> graph, Graph<Country, Border> graph2, double percentage) {
        ArrayList<String> cities = new ArrayList<>();

        for (Country c : graph2.vertices()) {
            double count = 0;
            for (User u : graph.vertices()) {
                if (c.getCapital().equals(u.getCity())) {
                    count++;
                }
            }
            if (count / graph.numVertices() * 100 >= percentage)
                cities.add(c.getCapital());
        }
        return cities;
    }

    public static ArrayList<String> citiesMoreCentralized (AdjacencyMatrixGraph<User, Relationship> graph, Graph<Country, Border> graph2, double percentage, int numCities) {
        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> morePercentage = morePercentage(graph, graph2, percentage);
        Map<String, Double> centralizedCities = centralizedCity(graph2);

        for (String city : centralizedCities.keySet()) {
            if (res.size() < numCities) {
                for (int i = 0; i < morePercentage.size(); i++) {
                    if (city.equals(morePercentage.get(i)))
                        res.add(city);
                }
            }
        }
        return res;
    }

}
