package com.ifpb.neo4j.visao;

import com.ifpb.neo4j.model.Pessoa;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "123456"));

        //Pessoa p = new Pessoa("222.222.222-02", "Maria", LocalDate.now());
        //salvarPessoa(driver, p);

        buscarPessoas(driver);
        //criarAmizade(driver, "111.111.111-01", "222.222.222-02");

        driver.close();
    }

    private static void criarAmizade(Driver driver, String cpf1, String cpf2) {
        try (Session session = driver.session()){

            Map<String, Object> mapa = new HashMap<>();
            mapa.put("cpf1", cpf1);
            mapa.put("cpf2", cpf2);

            session.run("MATCH (p1:Pessoa), (p2:Pessoa) WHERE p1.cpf = $cpf1" +
                    " AND p2.cpf=$cpf2 CREATE (p1)-[:AMIGO]->(p2) RETURN p1,p2", mapa);

        }
    }

    private static void buscarPessoas(Driver driver) {
        try(Session session = driver.session()){
            StatementResult result = session.run(
                    "MATCH (p:Pessoa) RETURN p.nome");

            result.stream().forEach(r-> System.out.println(r.get("p.nome").asString()));
        }
    }

    private static void salvarPessoa(Driver driver, Pessoa pessoa) {
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("cpf", pessoa.getCpf());
        mapa.put("nome", pessoa.getNome());
        mapa.put("nascimento", pessoa.getNascimento());

        try(Session session = driver.session()){
            session.run("CREATE (:Pessoa{cpf:$cpf," +
                            " nome:$nome, nascimento:$nascimento})",mapa);
        }

    }

}
