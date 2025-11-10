package app.util;

import app.model.Carrinho;
import app.model.Cliente;

/**
 * Singleton para gerenciar a sessão do usuário (cliente logado)
 * e o carrinho de compras atual.
 */
public class SessaoManager {

    private static SessaoManager instancia;

    private Cliente clienteLogado;
    private Carrinho carrinho;

    // Construtor privado para impedir novas instâncias
    private SessaoManager() {
        this.carrinho = new Carrinho(); // O carrinho já começa aqui
    }

    // Método público para pegar a única instância
    public static SessaoManager getInstance() {
        if (instancia == null) {
            instancia = new SessaoManager();
        }
        return instancia;
    }

    // Getters e Setters
    public Cliente getClienteLogado() {
        return clienteLogado;
    }

    public void setClienteLogado(Cliente clienteLogado) {
        this.clienteLogado = clienteLogado;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    // Reinicia o carrinho (ex: após finalizar uma compra)
    public void novoCarrinho() {
        this.carrinho = new Carrinho();
    }
}