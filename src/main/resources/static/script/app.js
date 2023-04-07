function calcular() { // botão que enviará os inputs do front para o back
    const lat_str = document.querySelector("lat_str");
    const lon_str = document.querySelector("lon_str");
    const lat_end = document.querySelector("lat_end");
    const lon_end = document.querySelector("lon_end");
}

function hide_show() { // funcionamento do botão do "olho" (ocultar/mostrar)
    var in_box = document.getElementById("input_box");
    var open_eye = document.getElementById("open_eye");
    var closed_eye = document.getElementById("closed_eye");
    var in_style = getComputedStyle(in_box);
    var eye_icon = document.getElementById("eye_icon");
    var ext_in = document.getElementById("ext_in");
    var sht_in = document.getElementById("sht_in");
    var graph_dim = document.getElementById("graph");
    var svg = document.getElementById("svg");

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
        graph_dim.style.height = "280px";
        svg.style.height = "280px";
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
        graph_dim.style.height = "450px";
        svg.style.height = "450px";
    }
}
var nodes;
var links;

const padding = 50;

fetch('http://localhost:3003/data', { // acessa a rota get/data criada no back para passar as duas arrays
    method: 'GET'
})
    .then(response => response.json())
    .then(data => {
        console.log(data);
        nodes = data.nodes;
        links = data.links;

        // cria o grafo com a biblioteca d3 através das informações das arrays nodes e links
        var svg = d3.select('#graph')
            .append('svg')
            .attr('class', 'svg')
            .attr('id', 'svg')
        width = +svg.attr("width"),
            height = +svg.attr("height");


        const simulation = d3.forceSimulation(nodes)
            .force("link", d3.forceLink(links).id(function (d) { return d.id; }).distance(80))
            .force('charge', d3.forceManyBody().strength(-200))
            .force("center", d3.forceCenter(width / 2, height / 2));

        const link = svg.append('g')
            .attr('stroke', '#999')
            .attr('stroke-opacity', 7)
            .selectAll('line')
            .data(links)
            .join('line')
            .attr('stroke-width', 7);

        const node = svg.append('g')
            .attr("class", "nodes")
            .attr('stroke', '#fff')
            .attr('stroke-width', 1.5)
            .selectAll('circle')
            .data(nodes)
            .join('circle')
            .attr('r', 20)
            .attr('fill', '#555')
            .attr('cx', d => d.x + padding) // adiciona o padding à coordenada x
            .attr('cy', d => d.y + padding) // adiciona o padding à coordenada y
            .call(drag(simulation));



        node.append('title')
            .text(d => d.label);

        simulation.on('tick', () => {
            link
                .attr('x1', d => d.source.x + padding)
                .attr('y1', d => d.source.y + padding)
                .attr('x2', d => d.target.x + padding)
                .attr('y2', d => d.target.y + padding);

            node
                .attr('cx', d => d.x + padding)
                .attr('cy', d => d.y + padding);
        });

        function drag(simulation) {
            function dragstarted(event, d) {
                if (!event.active) simulation.alphaTarget(0.3).restart();
                d.fx = d.x;
                d.fy = d.y;
            }

            function dragged(event, d) {
                d.fx = event.x;
                d.fy = event.y;
            }

            function dragended(event, d) {
                if (!event.active) simulation.alphaTarget(0);
                d.fx = null;
                d.fy = null;
            }
            return d3.drag()
                .on('start', dragstarted)
                .on('drag', dragged)
                .on('end', dragended);
        }
    });

