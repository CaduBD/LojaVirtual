package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Carrinho {

    // O carrinho é composto por uma lista de Itens
    private List<ItemCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    /**
     * Lógica principal: Adiciona um produto ao carrinho.
     * Se o produto já existir, apenas incrementa a quantidade.
     * Se não existir, cria um novo ItemCarrinho.
     */
    public void adicionarProduto(Produto produto, int quantidade) {
        // 1. Verifica se o produto já está no carrinho
        Optional<ItemCarrinho> itemExistente = buscarItemPorProduto(produto);

        if (itemExistente.isPresent()) {
            // 2. Se sim, apenas aumenta a quantidade
            itemExistente.get().adicionarQuantidade(quantidade);
        } else {
            // 3. Se não, cria e adiciona um novo item ao carrinho
            ItemCarrinho novoItem = new ItemCarrinho(produto, quantidade);
            this.itens.add(novoItem);
        }
    }


     //Remove um produto (e toda sua quantidade) do carrinho.
    public void removerProduto(Produto produto) {
        Optional<ItemCarrinho> itemParaRemover = buscarItemPorProduto(produto);

        if (itemParaRemover.isPresent()) {
            this.itens.remove(itemParaRemover.get());
        }
    }

    /**
     Remove uma quantidade específica de um produto.
     Se a quantidade a remover for maior ou igual à existente,
     o item é removido da lista.
     Senão, apenas subtrai a quantidade do item.
     */
    public void removerQuantidade(Produto produto, int quantidadeParaRemover) {
        Optional<ItemCarrinho> itemExistente = buscarItemPorProduto(produto);

        if (itemExistente.isPresent()) {
            ItemCarrinho item = itemExistente.get();

            // Se a remoção for total (ou maior que o total), remove o item
            if (quantidadeParaRemover >= item.getQuantidade()) {
                this.itens.remove(item);
            } else {
                // Se for parcial, apenas chama o método do ItemCarrinho
                item.removerQuantidade(quantidadeParaRemover);
            }
        }
    }

     //Calcula o valor total de todos os itens no carrinho.
    public double calcularTotal() {
        double total = 0.0;
        for (ItemCarrinho item : this.itens) {
            total += item.getSubtotal();
        }
        return total;
    }

        //Método auxiliar para encontrar um item pelo produto
     private Optional<ItemCarrinho> buscarItemPorProduto(Produto produto) {
        for (ItemCarrinho item : this.itens) {
            if (item.getProduto().getId() == produto.getId()) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public List<ItemCarrinho> getItens() {
        return this.itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    // Limpa o carrinho
    public void limpar() {
        this.itens.clear();
    }
}