package br.com.galeradacaneca.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade Venda.
 *
 * SOLID — SRP: modela exclusivamente os dados de uma venda.
 */
@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vendas")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vendedores", nullable = true)
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto", nullable = true)
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    public Venda() {}

    public Integer getId()                    { return id; }
    public void setId(Integer id)             { this.id = id; }
    public Vendedor getVendedor()             { return vendedor; }
    public void setVendedor(Vendedor v)       { this.vendedor = v; }
    public Produto getProduto()               { return produto; }
    public void setProduto(Produto p)         { this.produto = p; }
    public Cliente getCliente()               { return cliente; }
    public void setCliente(Cliente c)         { this.cliente = c; }
    public BigDecimal getValorTotal()         { return valorTotal; }
    public void setValorTotal(BigDecimal val) { this.valorTotal = val; }
}
