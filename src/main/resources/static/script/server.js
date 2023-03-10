const neo4j = require('neo4j-driver');
const cors = require('cors');

const express = require('express');
const app = express();

app.use(cors());

// criação da conexão com o banco de dados neo4J
const uri = 'bolt://127.0.0.1:7687';
const user = 'neo4j';
const password = '1234567890';


const driver = neo4j.driver(uri, neo4j.auth.basic(user, password));
const session = driver.session();

const query = 'MATCH (n) OPTIONAL MATCH (n)-[r]->() RETURN n, r';

session.run(query)
    .then((result) => {
        // armazena dados do grafo
        const nodes = [];
        const links = [];
        result.records.forEach((record) => {
            // adiciona nó
            const node = record.get('n');
            nodes.push({
                id: node.identity.low,
                label: node.properties.name,
            });

            // adiciona aresta
            if (record.get('r') != null) {
                const rel = record.get('r');
                links.push({
                    source: rel.start.low,
                    target: rel.end.low,
                    type: rel.type,
                });
            }
        });

        console.log(nodes);
        console.log(links);

        app.get('/data', (req, res) => {
            res.json({ nodes, links });
        });

        // inicia o servidor
        app.listen(3003, () => {
            console.log('Servidor iniciado na porta 3003');
        });
        session.close();
    })
    .catch((error) => {
        console.log(error);
    })
    .then(() => {
        driver.close();
    });


