const neo4j = require('neo4j-driver');

function calcular(){ // função que pega os valores dos inputs no frontend
    const lat_str = document.querySelector("lat_str");
    const lon_str = document.querySelector("lon_str");
    const lat_end = document.querySelector("lat_end");
    const lon_end = document.querySelector("lon_end");
    console.log("gg")
}

function hide_show() {
    var in_box = document.getElementById("input_box");
    var open_eye = document.getElementById("open_eye");
    var closed_eye = document.getElementById("closed_eye");
    var in_style = getComputedStyle(in_box);
    var eye_icon = document.getElementById("eye_icon");
    var ext_in = document.getElementById("ext_in");
    var sht_in = document.getElementById("sht_in");

    var lat_str = document.getElementById("lat_str").value;
    var lon_str = document.getElementById("lon_str").value;
    var lat_end = document.getElementById("lat_end").value;
    var lon_end = document.getElementById("lon_end").value;

    var lat_1 = document.getElementById("lat_1");
    var lon_1 = document.getElementById("lon_1");
    var lat_2 = document.getElementById("lat_2");
    var lon_2 = document.getElementById("lon_2");

    if (in_style.height === "40px") {
        in_box.style.height = "300px"; // aumenta o tamanho da caixa de input
        open_eye.style.display = "none"; // esconde a imagem do olho aberto
        closed_eye.style.display = "block"; // mostra a imagem do olho fechado
        eye_icon.style.top = "278px"; // desce o ícone do olho
        ext_in.style.display = "block";
        sht_in.style.display = "none";
    }

    else {
        in_box.style.height = "40px"; // diminui o tamanho da caixa de input
        open_eye.style.display = "block"; // mostra a imagem do olho aberto
        closed_eye.style.display = "none"; // esconde a imagem do olho fechado
        eye_icon.style.top = "16px"; // sobe o ícone do olho
        ext_in.style.display = "none";
        sht_in.style.display = "block";
        lat_1.innerHTML = "Latitude: " + lat_str + " - ";
        lon_1.innerHTML = "Longitude: " + lon_str;
        lat_2.innerHTML = "Latitude: " + lat_end + " - ";
        lon_2.innerHTML = "Longitude: " + lon_end;
    }
}

// criação da conexão com o banco de dados neo4J
const uri = 'bolt://127.0.0.1:7687';
const user = 'neo4j';
const password = '1234567890';


const driver = neo4j.driver(uri, neo4j.auth.basic(user, password));
const session = driver.session();

const query = 'MATCH (n) OPTIONAL MATCH (n)-[r]->() RETURN n, r';

session
    .run(query)
    .then((result) => {
        result.records.forEach((record) => {
            console.log(record.get('n'));
            if (record.get('r') != null) console.log(record.get('r'));
            //console.log(record.get('r').start);
            //console.log(record.get('r').end);
        });
    })
    .catch((error) => {
        console.error(error);
    })
    .finally(() => {
        session.close();
        driver.close();
    });