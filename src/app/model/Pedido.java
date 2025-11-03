package app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemCarrinho> itens;
    private double valorTotal;
    private LocalDateTime data;
    private String status;

    public Pedido(Cliente cliente, Carrinho carrinho) {
        this.cliente = cliente;
        this.itens = new ArrayList<>(carrinho.getItens());
        this.valorTotal = carrinho.calcularTotal();
        this.data = LocalDateTime.now();
        this.status = "Pendente"; // Status inicial
    }

    public Pedido() {
        this.itens = new ArrayList<>();
    }

    public String gerarResumo() {
        StringBuilder resumo = new StringBuilder();
        resumo.append("--- Resumo do Pedido ---\n");
        resumo.append("ID do Pedido: ").append(this.id).append("\n");
        resumo.append("Cliente: ").append(this.cliente.getNome()).append("\n");
        resumo.append("Data: ").append(this.data.toString()).append("\n"); // Pode formatar melhor depois
        resumo.append("Status: ").append(this.status).append("\n");
        resumo.append("Itens:\n");

        for (ItemCarrinho item : itens) {
            resumo.append("  - ")
                    .append(item.getProduto().getNome())
                    .append(" (Qtd: ").append(item.getQuantidade())
                    .append(") - Subtotal: R$ ").append(String.format("%.2f", item.getSubtotal()))
                    .append("\n");
        }

        resumo.append("----------------------------\n");
        resumo.append("VALOR TOTAL: R$ ").append(String.format("%.2f", this.valorTotal)).append("\n");

        return resumo.toString();
    }

    public void confirmar(){
        this.status = "confirmado";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
