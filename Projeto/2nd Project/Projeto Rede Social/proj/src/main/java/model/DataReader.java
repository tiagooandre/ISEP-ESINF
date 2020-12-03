package model;

import graphbase.Graph;
import matrixGraph.AdjacencyMatrixGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataReader {
    private final String countriesFile;
    private final String bordersFile;
    private final String usersFile;
    private final String relationshipsFile;

    private final Graph<Country, Border> countriesGraph = new Graph<>(false);
    private final AdjacencyMatrixGraph<User, Relationship> relationshipsMatrix = new AdjacencyMatrixGraph<>();
    private final AdjacencyMatrixGraph<User, Double> connectedMatrix = new AdjacencyMatrixGraph<>();

    public Graph<Country, Border> getCountriesGraph() {
        return countriesGraph;
    }

    public AdjacencyMatrixGraph<User, Relationship> getRelationshipsMatrix() {
        return relationshipsMatrix;
    }

    public AdjacencyMatrixGraph<User, Double> getConnectedMatrix() {
        return connectedMatrix;
    }

    public DataReader(String countriesFile, String bordersFile, String usersFile, String relationshipsFile) {
        this.countriesFile = "src/main/resources/" + countriesFile;
        this.bordersFile = "src/main/resources/" + bordersFile;
        this.usersFile = "src/main/resources/" + usersFile;
        this.relationshipsFile = "src/main/resources/" + relationshipsFile;

        parseCountries();
        parseBorders();
        parseUsers();
        parseRelationships();
    }

    /*
    1. Construir os grafos a partir da informação fornecida nos ficheiros de texto.
    A capital de um país tem ligação direta com as capitais dos países com os quais
    faz fronteira. O cálculo da distância em Kms entre duas capitais deverá ser feito
    através das suas coordenadas.
     */

    private void parseCountries() {
        String line;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(this.countriesFile))){
            while ( (line = br.readLine()) != null) {
                String[] line_split = line.split(",");

                String name = line_split[0].trim();
                String continent = line_split[1].trim();
                String population_str = line_split[2].trim();
                String capital = line_split[3].trim();
                String latitude_str = line_split[4].trim();
                String longitude_str = line_split[5].trim();

                double population = Double.parseDouble(population_str);
                double latitude = Double.parseDouble(latitude_str);
                double longitude = Double.parseDouble(longitude_str);

                Country country = new Country(name, continent, population, capital, latitude, longitude);

                countriesGraph.insertVertex(country);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        } catch (IOException e) {
            System.out.println("Fail while reading the file.");
        }
    }

    private void parseBorders() {
        String line;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(this.bordersFile))){
            while ( (line = br.readLine()) != null) {
                String[] line_split = line.split(",");

                String country1 = line_split[0].trim();
                String country2 = line_split[1].trim();

                Border border = new Border(country1, country2);

                Country c1 = Utils.getCountryFromGraph(countriesGraph, country1);
                Country c2 = Utils.getCountryFromGraph(countriesGraph, country2);

                assert c1 != null;
                assert c2 != null;
                countriesGraph.insertEdge(c1, c2, border, Utils.distanceTwoCapitals(c1, c2));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        } catch (IOException e) {
            System.out.println("Fail while reading the file.");
        }
    }

    /*
    2. Devolver os amigos comuns entre os n utilizadores mais populares da rede.
    A popularidade de um utilizador é dada pelo seu número de amizades.
    */

    private void parseUsers() {
        String line;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(this.usersFile))) {
            while ((line = br.readLine()) != null) {
                String[] line_split = line.split(",");

                String user_id = line_split[0].trim();
                String age_str = line_split[1].trim();
                String city = line_split[2].trim();

                int age = Integer.parseInt(age_str);

                User user = new User(user_id, age, city);

                relationshipsMatrix.insertVertex(user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        } catch (IOException e) {
            System.out.println("Fail while reading the file.");
        }
    }

    private void parseRelationships() {
        String line;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(this.relationshipsFile))) {
            while ( (line = br.readLine()) != null) {
                String[] line_split = line.split(",");

                String user1_id = line_split[0].trim();
                String user2_id = line_split[1].trim();

                Relationship relationship = new Relationship(user1_id, user2_id);

                User user1 = Utils.getUserFromMatrix(relationshipsMatrix, user1_id);
                User user2 = Utils.getUserFromMatrix(relationshipsMatrix, user2_id);

                if (user1 == null || user2 == null) {
                    //System.out.println("Erro ao carregar a relações entre o utilizador " + user1_id + " e " + user2_id + ".");
                    continue;
                }
                relationshipsMatrix.insertEdge(user1, user2, relationship);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        } catch (IOException e) {
            System.out.println("Fail while reading the file.");
        }
    }
}
