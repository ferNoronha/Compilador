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
public class ProgramToken {
    private String token;
    private int linha;
    private String lexema;

    public ProgramToken() {
    }

    public ProgramToken(String token, int linha, String lexema) {
        this.token = token;
        this.linha = linha;
        this.lexema = lexema;
    }

   

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    
    
}
