
const API_URL = "/api/notas-fiscais";

const formulario = document.getElementById("formCompra");
const listaCompras = document.getElementById("listaCompras");

// Formatação de valores em reais
function formatarMoeda(valor) {
    return Number(valor).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

// Formatação de datas
function formatarData(data) {
    const [ano, mes, dia] = data.split("-");
    return `${dia}/${mes}/${ano}`;
}

// Buscar compras cadastradas no backend
async function carregarCompras() {
    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível buscar as compras.");
        }

        const compras = await resposta.json();

        mostrarCompras(compras);
        atualizarResumo(compras);

    } catch (erro) {
        console.error("Erro ao carregar compras:", erro);
        listaCompras.textContent = "Não foi possível carregar as compras.";
    }
}

// Exibir compras na tela
function mostrarCompras(compras) {
    listaCompras.innerHTML = "";

    if (compras.length === 0) {
        listaCompras.textContent = "Nenhuma compra cadastrada ainda.";
        return;
    }

    compras.forEach(compra => {
        const item = document.createElement("div");
        item.className = "item-compra";

        const titulo = document.createElement("h3");
        titulo.textContent = compra.numero;

        const data = document.createElement("p");
        data.textContent = "Data: " + formatarData(compra.dataEmissao);

        const valor = document.createElement("p");
        valor.textContent = "Valor da compra: " + formatarMoeda(compra.valorTotal);

        const tributos = document.createElement("p");
        tributos.textContent = "Total de tributos: " + formatarMoeda(compra.totalTributos);

        const percentual = document.createElement("p");
        percentual.textContent = "Percentual de tributos: " + compra.percentualTributos + "%";

        item.append(titulo, data, valor, tributos, percentual);

        listaCompras.appendChild(item);
    });
}

// Atualizar os cartões do dashboard
function atualizarResumo(compras) {
    const totalCompras = compras.reduce(
        (soma, compra) => soma + Number(compra.valorTotal || 0), 0
    );

    const totalTributos = compras.reduce(
        (soma, compra) => soma + Number(compra.totalTributos || 0), 0
    );

    const percentual = totalCompras > 0
        ? (totalTributos / totalCompras) * 100
        : 0;

    document.getElementById("totalCompras").textContent =
        formatarMoeda(totalCompras);

    document.getElementById("totalTributos").textContent =
        formatarMoeda(totalTributos);

    document.getElementById("percentualMedio").textContent =
        percentual.toFixed(2).replace(".", ",") + "%";
}

// Cadastrar uma nova compra
formulario.addEventListener("submit", async function(evento) {
    evento.preventDefault();

    const compra = {
        numero: document.getElementById("numero").value.trim(),
        dataEmissao: document.getElementById("dataEmissao").value,
        valorTotal: Number(document.getElementById("valorTotal").value),
        status: "Registrada",
        tributosFederais: Number(document.getElementById("tributosFederais").value),
        tributosEstaduais: Number(document.getElementById("tributosEstaduais").value),
        tributosMunicipais: Number(document.getElementById("tributosMunicipais").value)
    };

    const totalTributos =
        compra.tributosFederais +
        compra.tributosEstaduais +
        compra.tributosMunicipais;

    if (totalTributos > compra.valorTotal) {
        alert("O total dos tributos não pode ser maior que o valor da compra.");
        return;
    }

    try {
        const resposta = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(compra)
        });

        if (!resposta.ok) {
            const mensagem = await resposta.text();
            throw new Error(mensagem || "Erro ao cadastrar compra.");
        }

        alert("Compra cadastrada com sucesso!");

        formulario.reset();

        await carregarCompras();

    } catch (erro) {
        console.error("Erro ao cadastrar:", erro);
        alert("Não foi possível cadastrar a compra. Confira o console e o backend.");
    }
});

// Carregar os dados ao abrir a página
carregarCompras();
