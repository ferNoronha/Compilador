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
public class TabelaSemantica {
    private String token;
    private String tipo;
    private String valor;
    private String lexema;
    
    public TabelaSemantica(String token, String tipo, String valor, String lexema) {
        this.token = token;
        this.tipo = tipo;
        this.valor = valor;
        this.lexema = lexema;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }


    
    
    
}
