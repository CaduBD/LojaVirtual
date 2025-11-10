package app.db;

import app.model.Cliente;
import app.model.ItemCarrinho;
import app.model.Pedido;
import app.model.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Esta classe simula nosso banco de dados.
 * Ela guarda os dados em listas estáticas.
 */
public class Repositorio {

    // Nossas "tabelas"
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Produto> produtos = new ArrayList<>();
    private static List<Pedido> pedidos = new ArrayList<>();

    /**
     * Prepara o banco falso com dados iniciais.
     */
    public static void init() {
        // Criar clientes
        clientes.add(new Cliente(1, "usuario1", "fulano@email.com", "123"));
        clientes.add(new Cliente(2, "usuario2", "ciclana@email.com", "abc"));

        // Criar produtos
        produtos.add(new Produto(101, "Notebook Gamer", 4500.00, 10));
        produtos.add(new Produto(102, "Mouse Óptico", 150.00, 20));
        produtos.add(new Produto(103, "Teclado Mecânico", 350.00, 15));
        produtos.add(new Produto(104, "Monitor 24\" 144Hz", 1800.00, 5));
    }

    // --- Métodos para Cliente ---

    /**
     * Pega o primeiro cliente para simular o login.
     */
    public static Cliente getClientePadrao() {
        if (!clientes.isEmpty()) {
            return clientes.get(0); // Retorna o primeiro usuario
        }
        return null;
    }

    // --- Métodos para Produto ---

    public static List<Produto> listarProdutos() {
        // Retorna apenas produtos com estoque
        List<Produto> disponiveis = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getQuantidadeEstoque() > 0) {
                disponiveis.add(p);
            }
        }
        return disponiveis;
    }

    // Método auxiliar para buscar um produto pelo ID
    private static Optional<Produto> buscarProdutoPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    // --- Métodos para Pedido ---

    public static boolean salvarPedido(Pedido pedido) {
        // 1. Salva o pedido
        pedido.setId(pedidos.size() + 1); // Simula auto-increment
        pedidos.add(pedido);

        // 2. Atualiza o estoque dos produtos (aqui usamos o método de POO!)
        for (ItemCarrinho item : pedido.getItens()) {
            Optional<Produto> prodOpt = buscarProdutoPorId(item.getProduto().getId());
            if (prodOpt.isPresent()) {
                Produto produtoNoEstoque = prodOpt.get();
                // Usa o método da classe Produto para reduzir o estoque
                produtoNoEstoque.reduzirEstoque(item.getQuantidade());
            } else {
                // Se o produto não existe, algo deu muito errado (não deveria acontecer)
                return false;
            }
        }

        // Imprime o resumo no console (para debug)
        System.out.println("====== NOVO PEDIDO SALVO ======");
        System.out.println(pedido.gerarResumo());

        return true;
    }
}