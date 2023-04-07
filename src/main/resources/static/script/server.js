const neo4j = require('neo4j-driver');
const cors = require('cors');

const express = require('express');
const app = express();

app.use(cors()); //possibilita que o navegador acesse as informações do back

// define as informações do banco de dados neo4J
const uri = 'bolt://127.0.0.1:7687';
const user = 'neo4j';
const password = '1234567890';

//conexão com o banco de dados neo4j
const driver = neo4j.driver(uri, neo4j.auth.basic(user, password));
const session = driver.session();

// define o comando cypher que será enviado
const query = 'MATCH (n) OPTIONAL MATCH (n)-[r]->() RETURN n, r';

session.run(query) // roda o comando cypher no neo4J
    .then((result) => { // manipula o resultado do comando
        // armazena dados do grafo
        const nodes = []; // array de nós
        const links = []; // array de arestas
        result.records.forEach((record) => {
            // adiciona cada nó na array
            const node = record.get('n');
            nodes.push({
                id: node.identity.low,
                label: node.properties.name,
            });

            // adiciona cada aresta no array
            if (record.get('r') != null) {
                const rel = record.get('r');
                var idsource = rel.startNodeElementId.split(":")[2];
                var idtarget = rel.endNodeElementId.split(":")[2];
                links.push({
                    source: parseInt(idsource),
                    target: parseInt(idtarget),
                    type: rel.type
                });
            }
        });

        console.log(nodes);
        console.log(links);

        app.get('/data', (req, res) => {
            // criação da rota "data", a qual passará as arrays nodes e links para o front
            res.json({ nodes, links });
        });

        // criação do servidor usando node express
        app.listen(3003, () => {
            console.log('Servidor iniciado na porta 3003');
        });
        session.close();
    })
    .catch((error) => { // caso dê algum erro, ele é printado no console
        console.log(error);
    })
    .then(() => {
        driver.close();
    });


