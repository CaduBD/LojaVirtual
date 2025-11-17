package app.model;

public class ItemCarrinho {
    private Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto= produto;
        this.quantidade = quantidade;
    }

    //calcular subtotal do item
    public double getSubtotal(){
        return produto.getPreco() * quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // para adicionar mais do mesmo produto
    public void adicionarQuantidade(int qtd){
            this.quantidade += qtd;
    }

    //Remove uma quantidade do item
    public void removerQuantidade(int qtd) {
        if (qtd <= this.quantidade) {
            this.quantidade -= qtd;
        }
    }

    // para debug
    @Override
    public String toString() {
        // Isso vai aparecer no ListView do carrinho
        return String.format("%s (Qtd: %d) - Subtotal: R$ %.2f",
                produto.getNome(),
                quantidade,
                getSubtotal());
    }
}
