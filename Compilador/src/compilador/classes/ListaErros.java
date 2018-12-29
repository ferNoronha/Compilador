/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.classes;

/**
 *
 * @author Fernando A. Noronha
 */
public class ListaErros {
    private String erro;
    private int linha;

    public ListaErros() {
    }

    public ListaErros(String erro, int linha) {
        this.erro = erro;
        this.linha = linha;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    @Override
    public String toString() {
        return "ERRO= " + erro + " ~~ LINHA=" + linha ;
    }
    
    
    
}
