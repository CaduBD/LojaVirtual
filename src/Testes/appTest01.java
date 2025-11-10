package Testes;

import app.model.Carrinho;
import app.model.Pedido;
import app.model.Produto;

public class appTest01 {
    public static void main(String[] args) {
        Produto produto1 = new Produto(1245, "feijao", 8.50, 100);
        Produto produto2 = new Produto(12422, "arroz", 10, 100);

        Pedido pedido1 = new Pedido();
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarProduto(produto1, 10);
        carrinho.adicionarProduto(produto2, 10);
        System.out.println(carrinho.calcularTotal());

        for(int i = 0; i < carrinho.getItens().size(); i++){
            System.out.println(carrinho.getItens().get(i));
        }
    }
}
