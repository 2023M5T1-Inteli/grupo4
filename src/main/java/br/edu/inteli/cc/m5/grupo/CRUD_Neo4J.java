package br.edu.inteli.cc.m5.grupo;

// Abaixo, estão as bibliotecas necessárias para a conexão com o Neo4J
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.util.Scanner;

// Nesta classe, executamos as operações de CRUD no Neo4J, e como exemplo, criamos um nó com o nome "John", e depois imprimimos o nome do nó criado.
public class CRUD_Neo4J {
    
    private Driver driver;
    private Session session;

    // Método responsavel por conectar o terminal com o localhost do Neo4J
    public void connect(String uri, String user, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        this.session = driver.session();
    }

    // Método responsável por desconectar o terminal
    public void disconnect() {
        session.close();
        driver.close();
    }

    // Método que cria um nó, que será chamado caso a pessoa selecione essa opção. Nele são exigidos 3 parametros: o conjunto a qual o nó irá pertencer, a categoria e o valor do nó
    public void createNode(int id, double latKey, double lonKey, double elevationKey) {
        session.run("CREATE (node:id  {id: $id, latitude: $latKey, longitude: $lonKey, elevation: $elevationKey})",
                Values.parameters("id", id, "latKey", latKey, "lonKey", lonKey, "elevationKey", elevationKey));
    }

    // Método que "encontra" um nó, passando os parâmetros: conjunto, categoria e o valor, o método encontra o nó no Neo4J e mostra ele
    public Node readNode(String label, String propertyKey, Object propertyValue) {
        org.neo4j.driver.Record result = session.run("MATCH (n:" + label + " {" + propertyKey + ": $value}) RETURN n",
                Values.parameters("value", propertyValue)).single();
        return result.get("n").asNode();
    }


    // Método que atualiza um nó, para isso são necessários os parâmetros: conjunto, categoria antiga, valor antigo, nova categoria e novo valor. Assim ele substitui os valores antigos pelos novos
    public void updateNode(String label, String propertyKey, Object propertyValue, String updateKey, Object updateValue) {
        session.run("MATCH (n:" + label + " {" + propertyKey + ": $value}) SET n." + updateKey + " = $newValue", Values.parameters("value", propertyValue, "newValue", updateValue));
    }

    // Método que deleta um nó, passando os parâmetros: conjunto, categoria e valor ele já encontra o nó e deleta ele
    public void deleteNode(String label, String propertyKey, Object propertyValue) {
        session.run("MATCH (n:" + label + " {" + propertyKey + ": $value}) DELETE n",
                Values.parameters("value", propertyValue));
    }

    // Método que cria um relacionamento entre nós, para isso é necessário passar os parâmetros: conjunto, categoria e valores, isso dos 2 nós que serão interconectados
    public void createRelationship(int idInit, int idEnd, double peso) {
        session.run("MATCH (a:id) , (b:id) WHERE a.id = $idInit AND b.id = $idEnd CREATE (a)-[r:peso {peso: $peso}]->(b)", Values.parameters("idInit", idInit, "idEnd", idEnd, "peso", peso));
    }

    // Método que busca um relacionamento através dos parametros: conjunto, categoria e valor tanto do nó inicial, quanto do final
    public Relationship readRelationship(String startLabel, String startPropertyKey, Object startPropertyValue, String endLabel, String endPropertyKey, Object endPropertyValue, String relationshipType) {
        org.neo4j.driver.Record result = session.run("MATCH (a:" + startLabel + " {" + startPropertyKey + ": $startValue})-[r:" + relationshipType + "]->(b:" + endLabel + " {" + endPropertyKey + ": $endValue}) RETURN r", Values.parameters("startValue", startPropertyValue, "endValue", endPropertyValue)).single();
        return result.get("r").asRelationship();
    }

    // Métoodo que atualiza um relacionamento por meios dos parametros conjunto, categoria e valor tanto do nó inicial, quanto do final
    public void updateRelationship(String startLabel, String startPropertyKey, Object startPropertyValue, String endLabel, String endPropertyKey, Object endPropertyValue, String relationshipType, String updatePropertyKey, Object updatePropertyValue) {
        session.run("MATCH (a:" + startLabel + " {" + startPropertyKey + ": $startValue})-[r:" + relationshipType + "]->(b:" + endLabel + " {" + endPropertyKey + ": $endValue}) SET r." + updatePropertyKey + " = $newValue", Values.parameters("startValue", startPropertyValue, "endValue", endPropertyValue, "newValue", updatePropertyValue));
    }

    // Método que ao serem passados os parametros: conjunto do nó inicial, categoria do nó inicial e valor do nó inicial, conjunto do nó final, categoria do nó final e valor do nó final, deleta um relacionamento
    public void deleteRelationship(String startLabel, String startPropertyKey, Object startPropertyValue, String endLabel, String endPropertyKey, Object endPropertyValue, String relationshipType) {
    session.run("MATCH (a:" + startLabel + " {" + startPropertyKey + ": $startValue})-[r:" + relationshipType + "]->(b:" + endLabel + " {" + endPropertyKey + ": $endValue}) DELETE r", Values.parameters("startValue", startPropertyValue, "endValue", endPropertyValue));
    }

    public static void main(String[] args) {
        /*
        // Instancia do objeto example
        CRUD_Neo4J example = new CRUD_Neo4J();

        // Conectando o objeto no localhost do Neo4J
        example.connect("bolt://localhost:7687", "neo4j", "12345678");

        Scanner scanner = new Scanner(System.in);

        int option = 0;

        // O terminal oferece 9 opções de escolha pro usuario
        while (option != 9) {
            System.out.println("Select an option:");
            System.out.println("1. Create new Vertice");
            System.out.println("2. Read Vertice");
            System.out.println("3. Delete Vertice");
            System.out.println("4. Update Vertice");
            System.out.println("5. Create new Relationship");
            System.out.println("6. Read Relationship");
            System.out.println("7. Delete Relationship");
            System.out.println("8. Update Relationship");
            System.out.println("9. Exit");

            option = scanner.nextInt();
            scanner.nextLine();

            // Baseado na escolha do usuario, o programa solicita os parametros necessários e chama o método
            switch (option) {

                case 1:

                    System.out.print("Enter label: ");
                    String label = scanner.nextLine();
                    System.out.print("Enter property key: ");
                    String propertyKey = scanner.nextLine();
                    System.out.print("Enter property value: ");
                    Object propertyValue = scanner.nextLine();

                    break;

                case 2:

                    System.out.print("Enter label: ");
                    label = scanner.nextLine();
                    System.out.print("Enter property key: ");
                    propertyKey = scanner.nextLine();
                    System.out.print("Enter property value: ");
                    propertyValue = scanner.nextLine();

                    break;

                case 3:

                    System.out.print("Enter label: ");
                    label = scanner.nextLine();
                    System.out.print("Enter property key: ");
                    propertyKey = scanner.nextLine();
                    System.out.print("Enter property value: ");
                    propertyValue = scanner.nextLine();

                    break;

                case 4:

                    System.out.print("Enter label: ");
                    label = scanner.nextLine();
                    System.out.print("Enter property key: ");
                    propertyKey = scanner.nextLine();
                    System.out.print("Enter property value: ");
                    propertyValue = scanner.nextLine();
                    System.out.print("Enter update key: ");
                    String updateKey = scanner.nextLine();
                    System.out.print("Enter update value: ");
                    Object updateValue = scanner.nextLine();

                    break;

                case 5:

                    System.out.print("Enter start label: ");
                    String startLabel = scanner.nextLine();
                    System.out.print("Enter start property key: ");
                    String startPropertyKey = scanner.nextLine();
                    System.out.print("Enter start property value: ");
                    Object startPropertyValue = scanner.nextLine();
                    System.out.print("Enter end label: ");
                    String endLabel = scanner.nextLine();
                    System.out.print("Enter end property key: ");
                    String endPropertyKey = scanner.nextLine();
                    System.out.print("Enter end property value: ");
                    Object endPropertyValue = scanner.nextLine();
                    System.out.print("Enter relationship type: ");
                    String relationshipType = scanner.nextLine();

                    break;

                case 6:

                    System.out.print("Enter start label: ");
                    startLabel = scanner.nextLine();
                    System.out.print("Enter start property key: ");
                    startPropertyKey = scanner.nextLine();
                    System.out.print("Enter start property value: ");
                    startPropertyValue = scanner.nextLine();
                    System.out.print("Enter end label: ");
                    endLabel = scanner.nextLine();
                    System.out.print("Enter end property key: ");
                    endPropertyKey = scanner.nextLine();
                    System.out.print("Enter end property value: ");
                    endPropertyValue = scanner.nextLine();
                    System.out.print("Enter relationship type: ");
                    relationshipType = scanner.nextLine();

                    break;

                case 7:
                    System.out.print("Enter start label: ");
                    startLabel = scanner.nextLine();
                    System.out.print("Enter start property key: ");
                    startPropertyKey = scanner.nextLine();
                    System.out.print("Enter start property value: ");
                    startPropertyValue = scanner.nextLine();
                    System.out.print("Enter end label: ");
                    endLabel = scanner.nextLine();
                    System.out.print("Enter end property key: ");
                    endPropertyKey = scanner.nextLine();
                    System.out.print("Enter end property value: ");
                    endPropertyValue = scanner.nextLine();
                    System.out.print("Enter relationship type: ");
                    relationshipType = scanner.nextLine();

                    break;

                case 8:
                    System.out.print("Enter start label: ");
                    startLabel = scanner.nextLine();
                    System.out.print("Enter start property key: ");
                    startPropertyKey = scanner.nextLine();
                    System.out.print("Enter start property value: ");
                    startPropertyValue = scanner.nextLine();
                    System.out.print("Enter end label: ");
                    endLabel = scanner.nextLine();
                    System.out.print("Enter end property key: ");
                    endPropertyKey = scanner.nextLine();
                    System.out.print("Enter end property value: ");
                    endPropertyValue = scanner.nextLine();
                    System.out.print("Enter relationship type: ");
                    relationshipType = scanner.nextLine();
                    System.out.print("Enter update key: ");
                    updateKey = scanner.nextLine();
                    System.out.print("Enter update value: ");
                    updateValue = scanner.nextLine();

                    break;

                case 9:

                    System.out.println("Exiting...");
                    break;

                    // Caso o que  é solicitado não seja realizado, ou o usuario insira algum argumento invalido, o programa responde um erro
                    default:

                    System.out.println("Invalid option.");
                    break;

            }
        }

        // No fim do que é feito, o objeto se desconecta do localhost
        scanner.close();
        example.disconnect();
    }
 */
    }
}
