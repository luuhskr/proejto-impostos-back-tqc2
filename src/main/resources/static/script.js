
const API_URL = "/api/notas-fiscais";

const formulario = document.getElementById("formCompra");
const listaCompras = document.getElementById("listaCompras");

let compraEditandoId = null;

const botaoSalvar = formulario.querySelector('button[type="submit"]');

// Formatar valores em reais
function formatarMoeda(valor) {
    return Number(valor).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

// Formatar datas
function formatarData(data) {
    const [ano, mes, dia] = data.split("-");
    return `${dia}/${mes}/${ano}`;
}

// Carregar compras do backend
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

// Mostrar compras na tela
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
        percentual.textContent =
            "Percentual de tributos: " + compra.percentualTributos + "%";

        // Área dos botões
        const acoes = document.createElement("div");
        acoes.className = "acoes-compra";

        const botaoEditar = document.createElement("button");
        botaoEditar.type = "button";
        botaoEditar.className = "btn-editar";
        botaoEditar.textContent = "Editar";

        botaoEditar.addEventListener("click", () => {
            editarCompra(compra);
        });

        const botaoExcluir = document.createElement("button");
        botaoExcluir.type = "button";
        botaoExcluir.className = "btn-excluir";
        botaoExcluir.textContent = "Excluir";

        botaoExcluir.addEventListener("click", () => {
            excluirCompra(compra.id);
        });

        acoes.append(botaoEditar, botaoExcluir);

        item.append(titulo, data, valor, tributos, percentual, acoes);

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

// Preencher formulário para editar uma compra
function editarCompra(compra) {
    compraEditandoId = compra.id;

    document.getElementById("numero").value = compra.numero;
    document.getElementById("dataEmissao").value = compra.dataEmissao;
    document.getElementById("valorTotal").value = compra.valorTotal;
    document.getElementById("tributosFederais").value = compra.tributosFederais;
    document.getElementById("tributosEstaduais").value = compra.tributosEstaduais;
    document.getElementById("tributosMunicipais").value = compra.tributosMunicipais;

    botaoSalvar.textContent = "Salvar alterações";

    document.querySelector(".formulario").scrollIntoView({
        behavior: "smooth"
    });
}

// Excluir uma compra
async function excluirCompra(id) {
    const confirmar = confirm("Tem certeza de que deseja excluir esta compra?");

    if (!confirmar) {
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível excluir a compra.");
        }

        alert("Compra excluída com sucesso!");

        await carregarCompras();

    } catch (erro) {
        console.error("Erro ao excluir:", erro);
        alert("Não foi possível excluir a compra.");
    }
}

// Cadastrar ou atualizar uma compra
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
        const editando = compraEditandoId !== null;

        const resposta = await fetch(
            editando ? `${API_URL}/${compraEditandoId}` : API_URL,
            {
                method: editando ? "PUT" : "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(compra)
            }
        );

        if (!resposta.ok) {
            const mensagem = await resposta.text();
            throw new Error(mensagem || "Erro ao salvar compra.");
        }

        alert(editando
            ? "Compra atualizada com sucesso!"
            : "Compra cadastrada com sucesso!"
        );

        formulario.reset();

        compraEditandoId = null;
        botaoSalvar.textContent = "Cadastrar compra";

        await carregarCompras();

    } catch (erro) {
        console.error("Erro ao salvar:", erro);
        alert("Não foi possível salvar a compra. Confira o console e o backend.");
    }
});

// Carregar os dados ao abrir a página
carregarCompras();
