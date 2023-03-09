function calcular() {
    const lat_str = document.querySelector("lat_str");
    const lon_str = document.querySelector("lon_str");
    const lat_end = document.querySelector("lat_end");
    const lon_end = document.querySelector("lon_end");
}

function hide_show() {
    var in_box = document.getElementById("input_box");
    var open_eye = document.getElementById("open_eye");
    var closed_eye = document.getElementById("closed_eye");
    var in_style = getComputedStyle(in_box);
    var eye_icon = document.getElementById("eye_icon");
    var ext_in = document.getElementById("ext_in");
    var sht_in = document.getElementById("sht_in");
    var graph_dim = document.getElementById("graph");

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
    }
}
var nodes;
var links;

fetch('http://localhost:3000/data', {
    method: 'GET'
})
    .then(response => response.json())
    .then(data => {
        console.log(data);
        nodes = data.nodes;
        links = data.links;
        // cria o grafo com a biblioteca d3
        const svg = d3.select('#graph')
            .append('svg')
            .attr('width', '100%')
            .attr('height', '100%');


        const simulation = d3.forceSimulation(nodes)
            .force('link', d3.forceLink(links).id(d => d.id))
            .force('charge', d3.forceManyBody().strength(-400))
            .force('center', d3.forceCenter(svg.attr('width') / 2, svg.attr('height') / 2))
            .forceCenter(svg.attr('width') / 2, svg.attr('height') / 2);

        const link = svg.append('g')
            .attr('stroke', '#999')
            .attr('stroke-opacity', 0.6)
            .selectAll('line')
            .data(links)
            .join('line')
            .attr('stroke-width', d => Math.sqrt(d.value));

        const node = svg.append('g')
            .attr('stroke', '#fff')
            .attr('stroke-width', 1.5)
            .selectAll('circle')
            .data(nodes)
            .join('circle')
            .attr('r', 20)
            .attr('fill', '#555')
            .call(drag(simulation));

        node.append('title')
            .text(d => d.label);

        simulation.on('tick', () => {
            link
                .attr('x1', d => d.source.x)
                .attr('y1', d => d.source.y)
                .attr('x2', d => d.target.x)
                .attr('y2', d => d.target.y);

            node
                .attr('cx', d => d.x)
                .attr('cy', d => d.y);
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

