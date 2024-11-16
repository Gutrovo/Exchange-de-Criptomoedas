
package model;

public class OutrasMoedas extends Moedas {
    private float TaxaCompra, TaxaVenda;

    public OutrasMoedas() {
    }

    public OutrasMoedas(String nome, double cotacao, float taxaCompra, float taxaVenda) {
        super(nome, cotacao, taxaCompra, taxaVenda);
        this.TaxaCompra = taxaCompra;
        this.TaxaVenda = taxaVenda;
    }

    public float getTaxaCompra() {
        return TaxaCompra;
    }

    public float getTaxaVenda() {
        return TaxaVenda;
    }

    public void setTaxaCompra(float TaxaCompra) {
        this.TaxaCompra = TaxaCompra;
    }

    public void setTaxaVenda(float TaxaVenda) {
        this.TaxaVenda = TaxaVenda;
    } 
}
