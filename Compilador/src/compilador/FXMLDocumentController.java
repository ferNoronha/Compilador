/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import compilador.classes.ListaErros;
import compilador.classes.ProgramToken;
import compilador.classes.TabelaSemantica;
import compilador.classes.Tokens;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Fernando A. Noronha
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private Button btnCompilar;
    @FXML
    private TextArea txaCod;

    private ArrayList<Tokens> tokens;
    @FXML
    private Button btnTokens;
    @FXML
    private ListView<Tokens> lvTokens;

    private ArrayList<ProgramToken> programToken;
    @FXML
    private ListView<ListaErros> lvErros;

    private ArrayList<ListaErros> listaerros;

    private ArrayList<TabelaSemantica> semantica;

    private int posToken;
    private int contador;
    @FXML
    private Button btnArq;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        contador = 0;
        tokens = new ArrayList();
        programToken = new ArrayList();
        listaerros = new ArrayList();
        semantica = new ArrayList<>();
        //Tokens tk;
        inserirTokens();
        ObservableList<Tokens> tk;
        tk = FXCollections.observableArrayList(tokens);
        lvTokens.setItems(tk);
        //  executa();
    }

    public void executa() {
        boolean validarcoment = false;
        boolean validarcomentuma = false;
        boolean validaraspas = false;

        String[] arrayLinha = txaCod.getText().split("\n");
        ListaErros le;
        //String[] arrayEspaco;
        String teste = "";
        String verifica = "";
        String separador = "+-/*%,({][})@$;";
        // \n \t :: " := || 

        for (int i = 0; i < arrayLinha.length; i++) {
            validarcomentuma = false;
            for (int j = 0; j < arrayLinha[i].length(); j++) {
                if ((separador.contains("" + arrayLinha[i].charAt(j)) || (arrayLinha[i].charAt(j) == ':') || (arrayLinha[i].charAt(j) == '|') || (arrayLinha[i].charAt(j) == '"') || (arrayLinha[i].charAt(j) == '>') || (arrayLinha[i].charAt(j) == '<') || (arrayLinha[i].charAt(j) == '!')
                        || (arrayLinha[i].charAt(j) == '*') || (arrayLinha[i].charAt(j) == ' ') || (arrayLinha[i].charAt(j) == '\t')) && validarcomentuma == false) {

                    if (arrayLinha[i].charAt(j) == ' ' && !validaraspas && !validarcoment) {
                        // espaço
                        if (!teste.isEmpty()) {
                            buscaInsere(teste, i);
                        }
                        teste = "";
                    } else {
                        if (arrayLinha[i].charAt(j) == '\t' && !validarcoment) {
                            //\t
                            if (!teste.isEmpty()) {
                                buscaInsere(teste, i);
                            }
                            teste = "";
                        }
                    }

                    /*
                    *Comparando a string separador
                     */
                    if ((separador.contains(arrayLinha[i].charAt(j) + "") || arrayLinha[i].charAt(j) == '\"') && !validarcoment) {
                        //separador
                        if (arrayLinha[i].charAt(j) == '\"')// && arrayLinha[i].charAt(j+1)=='"')
                        {

                            //"
                            if (!validaraspas) {
                                buscaInsere(teste, i);
                                buscaInsere('\"' + "", i);
                                teste = "";
                                validaraspas = !validaraspas;
                            } else {
                                validaraspas = !validaraspas;
                            }

                        } else {
                            if (!teste.isEmpty()) {
                                buscaInsere(teste, i);
                            }
                            buscaInsere(arrayLinha[i].charAt(j) + "", i);

                            teste = "";
                        }

                    }

                    /*
                    * sem ser a string separador
                     */
                    if (arrayLinha[i].charAt(j) == ':' && !validaraspas) {
                        verifica = "";
                        try {
                            verifica = arrayLinha[i].charAt(j + 1) + "";
                            if (verifica.equals('=' + "") && !validarcoment) {
                                // :=
                                buscaInsere(teste, i);
                                buscaInsere(":=", i);
                                teste = "";
                                j++;
                            } else {
                                if (verifica.equals(':' + "") && !validarcoment) {
                                    //::
                                    buscaInsere(teste, i);
                                    buscaInsere("::", i);
                                    teste = "";
                                    j++;
                                } else {
                                    if (verifica.equals('|' + "")) {
                                        // :|    
                                        // buscaInsere(teste,i);
                                        //   buscaInsere(":|",i);
                                        teste = "";
                                        validarcoment = false;
                                        j++;
                                    } else {
                                        if (!validarcoment) {
                                            buscaInsere(teste, j);
                                            teste = "";
                                            System.out.println("erro lexico no :=");
                                            le = new ListaErros("Erro lexico :", i);
                                            listaerros.add(le);
                                        }
                                    }
                                }

                            }
                        } catch (Exception e1) {
                            if (!validarcoment) {
                                buscaInsere(teste, j);
                                teste = "";
                                System.out.println("erro lexico no :=");
                                le = new ListaErros("Erro lexico :", i);
                                listaerros.add(le);
                            }
                        }

                    } else {
                        if (arrayLinha[i].charAt(j) == '|' && !validaraspas && !validarcoment) {
                            verifica = "";
                            try {
                                verifica = arrayLinha[i].charAt(j + 1) + "";
                                if (verifica.equals('|' + "")) {
                                    //||
                                    validarcomentuma = true;
                                    buscaInsere(teste, i);
                                    buscaInsere("||", i);
                                    teste = "";
                                    j++;
                                } else {
                                    if (verifica.equals(':' + "")) {
                                        //|:
                                        buscaInsere(teste, i);
                                        //buscaInsere("|:",i);
                                        teste = "";
                                        //j++;
                                        validarcoment = true;
                                    } else {
                                        buscaInsere(teste, j);
                                        teste = "";
                                        System.out.println("erro lexio no ||");
                                        le = new ListaErros("Erro lexico |", i);
                                        listaerros.add(le);
                                    }
                                }
                            } catch (Exception ex2) {
                                if (!teste.isEmpty()) {
                                    buscaInsere(teste, j);
                                    teste = "";
                                    System.out.println("erro lexio no ||");
                                    le = new ListaErros("Erro lexico |", i);
                                    listaerros.add(le);
                                }
                            }

                        } else {

                            if (arrayLinha[i].charAt(j) == '>' && !validaraspas && !validarcoment) {
                                verifica = "";
                                try {
                                    verifica = arrayLinha[i].charAt(j + 1) + "";
                                    if (verifica.equals('=' + "")) {
                                        //>=
                                        buscaInsere(teste, i);
                                        buscaInsere(">=", i);
                                        teste = "";
                                        j++;
                                    } else {
                                        //>
                                        buscaInsere(teste, i);
                                        buscaInsere(">", i);
                                        teste = "";

                                    }

                                } catch (Exception ex4) {
                                    buscaInsere(">", i);
                                    teste = "";
                                }

                            } else {
                                if (arrayLinha[i].charAt(j) == '<' && !validaraspas && !validarcoment) {
                                    verifica = "";
                                    try {
                                        verifica = arrayLinha[i].charAt(j + 1) + "";
                                        if (verifica.equals('=' + "")) {
                                            //<=
                                            buscaInsere(teste, i);
                                            buscaInsere("<=", i);
                                            teste = "";
                                            j++;
                                        } else {
                                            //<
                                            buscaInsere(teste, i);
                                            buscaInsere("<", i);
                                            teste = "";

                                        }
                                    } catch (Exception ex5) {
                                        buscaInsere("<", i);
                                        teste = "";
                                    }

                                } else {
                                    if (arrayLinha[i].charAt(j) == '!' && !validaraspas && !validarcoment) {
                                        //arrayLinha[i].charAt(j+1)!='\n' && 
                                        verifica = "";
                                        try {
                                            verifica = arrayLinha[i].charAt(j + 1) + "";
                                            if (verifica.equals("!")) {
                                                //!!
                                                buscaInsere(teste, i);
                                                buscaInsere("!!", i);
                                                teste = "";
                                                j++;
                                            } else {
                                                //!
                                                buscaInsere(teste, i);
                                                buscaInsere("!", i);
                                                teste = "";
                                            }
                                        } catch (Exception exe) {
                                            //!
                                            if (!teste.isEmpty()) {
                                                buscaInsere(teste, i);
                                            }
                                            buscaInsere("!", i);
                                            teste = "";
                                        }

                                    }

                                }
                            }

                        }
                    }

                } else {
                    //if()verificar erros como só = ; ou outra coisa do tipo
                    if (validaraspas || validarcomentuma) {
                        teste = "";
                        j++;
                    } else {
                        teste = teste + arrayLinha[i].charAt(j);
                    }
                }
            }
            if (!arrayLinha[i].isEmpty() && (!validaraspas || validarcomentuma == false)) {
                if (!validarcoment) {
                    buscaInsere(teste, i);
                    teste = "";
                }
                //teste=teste.replace(" ", "");

            }
        }
        for (int k = 0; k < programToken.size(); k++) {
            System.out.println("" + programToken.get(k).getToken() + " " + programToken.get(k).getLexema());

        }
        System.out.println("-------------------------------");

    }

    public void buscaInsere(String teste, int k) {

        boolean validar = false;
        boolean validarID = false;
        boolean validarErroLexico = false;
        ProgramToken pt;
        if (!teste.isEmpty()) {
            if (tokens.get(4).getLexema().contains(teste)) {
                pt = new ProgramToken(tokens.get(4).getToken(), k, teste);
                programToken.add(pt);
            } else {
                try {

                    double y = Double.parseDouble(teste);
                    validar = true;
                } catch (Exception ex) {

                }
                try {
                    int x = Integer.parseInt(teste);
                    validar = true;
                } catch (Exception e) {

                }
                if (validar) {
                    pt = new ProgramToken();
                    pt.setToken("TK_NUM");
                    pt.setLexema(teste);
                    pt.setLinha(k);
                    programToken.add(pt);
                } else {
                    for (int i = 0; i < tokens.size() && validarID == false; i++) {
                        if (tokens.get(i).getLexema().equals(teste)) {
                            pt = new ProgramToken(tokens.get(i).getToken(), k, teste);
                            programToken.add(pt);
                            validarID = true;
                        }
                    }

                    if (!validarID) {
                        try {
                            int num = Integer.parseInt(teste.charAt(0) + "");
                            if (num >= 0) {
                                validarErroLexico = true;
                                //Erro Léxico
                                ListaErros le = new ListaErros("Erro lexico Variavel", k);
                                listaerros.add(le);
                            }
                        } catch (Exception x) {

                        }
                        if (validarErroLexico == false) {
                            if (teste.matches("[A-Za-z0-9]+")) {
                                pt = new ProgramToken();
                                pt.setToken("TK_ID");
                                pt.setLexema(teste);
                                pt.setLinha(k);
                                programToken.add(pt);
                            } else {
                                ListaErros le = new ListaErros("Erro lexico", k);
                                listaerros.add(le);
                            }

                        }
                        /* else{
                                if(!teste.isEmpty())
                                    System.out.println("Erro Léxico "+teste);
                            }*/

                    }

                }

            }
        }

    }

    public void inserirTokens() {
        Tokens tk;
        tk = new Tokens("TK_MAIN", "main");
        tokens.add(tk);
        tk = new Tokens("TK_IF", "se");
        tokens.add(tk);
        tk = new Tokens("TK_ELSE", "senao");
        tokens.add(tk);
        tk = new Tokens("TK_WHILE", "enq");
        tokens.add(tk);
        tk = new Tokens("TK_OPER", "+-*/%"); //5 foi
        tokens.add(tk);
        tk = new Tokens("TK_FOR", "para");
        tokens.add(tk);
        tk = new Tokens("TK_ATRIB", ":="); //1 foi
        tokens.add(tk);
        //tk = new Tokens("TK_ID","main");
        //tokens.add(tk);
        tk = new Tokens("TK_COMP", "::"); //1 foi
        tokens.add(tk);
        tk = new Tokens("TK_VRG", ","); //1 foi
        tokens.add(tk);
        // tk = new Tokens("TK_NUM","main");
        // tokens.add(tk);
        tk = new Tokens("TK_APAR", "(");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_FPAR", ")");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_COMENT", "||");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_AND", "@");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_OR", "$");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_NULL", "vazio");
        tokens.add(tk);
        tk = new Tokens("TK_ASPAS", "\"");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_ACOL", "[");//1 foi 
        tokens.add(tk);
        tk = new Tokens("TK_FCOL", "]");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_ACHAVE", "{");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_FCHAVE", "}");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_ACOMENT", "|:");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_FCOMENT", ":|");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_FINALLINHA", ";");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_NEGAR", "!");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_MAIOR", ">");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_MENOR", "<");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_MAIORIGUAL", ">=");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_MENORIGUAL", "<=");//1 foi
        tokens.add(tk);
        tk = new Tokens("TK_DIFERENTE", "!!");//1
        tokens.add(tk);
        tk = new Tokens("TK_INT", "inteiro");
        tokens.add(tk);
        tk = new Tokens("TK_DOUBLE", "double");
        tokens.add(tk);
        tk = new Tokens("TK_CHAR", "caracter");
        tokens.add(tk);
        tk = new Tokens("TK_STRING", "texto");
        tokens.add(tk);
        tk = new Tokens("TK_BOOL", "bl");
        tokens.add(tk);
        tk = new Tokens("TK_TRUE", "verdadeiro");
        tokens.add(tk);
        tk = new Tokens("TK_FALSE", "falso");
        tokens.add(tk);
    }

    @FXML
    private void clkCompilar(ActionEvent event) {
        programToken.clear();
        listaerros.clear();
        semantica.clear();
        ObservableList<ListaErros> letk;
        letk = FXCollections.observableArrayList(listaerros);
        lvErros.setItems(letk);
        txaCod.setStyle(null);
        lvErros.setStyle(null);
        executa();
        contador = 0;
        //  analiseSintatica();
        semantica();
        if (!listaerros.isEmpty()) {
            ObservableList<ListaErros> let;
            let = FXCollections.observableArrayList(listaerros);
            lvErros.setItems(let);
            lvErros.setStyle("-fx-border-color:red;");
            txaCod.setStyle("-fx-border-color:red;");

        } else {
            txaCod.setStyle("-fx-border-color:green;");
            lvErros.setStyle("-fx-border-color:green;");
        }

    }

    @FXML
    private void clkLimpar(ActionEvent event) {
        programToken.clear();
        listaerros.clear();

        ObservableList<ListaErros> letk;
        letk = FXCollections.observableArrayList(listaerros);
        lvErros.setItems(letk);
        txaCod.setStyle(null);
        lvErros.setStyle(null);
        txaCod.setStyle(null);
        lvErros.setStyle(null);
        txaCod.clear();
    }

    public void opMat() {
        posToken++;
        if (programToken.get(posToken).getToken().equals("TK_NUM")
                || programToken.get(posToken).getToken().equals("TK_ID")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_OPER")) {
                posToken++;
                if (programToken.get(posToken).getToken().equals("TK_NUM")
                        || programToken.get(posToken).getToken().equals("TK_ID")) {
                    posToken++;
                } else {
                    /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado um número ou uma variável!", programToken.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("erro sintatico, numero ou variael");
                }
            } else {
                /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado uma operação aritmética!", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/
                System.out.println("erro sintatico, operacao aritmetica");
            }
        } else {
            if (programToken.get(posToken).getToken().equals("TK_OPER")) {
                posToken++;
                if (programToken.get(posToken).getToken().equals("TK_NUM")
                        || programToken.get(posToken).getToken().equals("TK_ID")) {
                    opMat();
                } else {
                    /* Erro e = new Erro("ERRO SINTÁTICO: Era esperado uma operação aritmética!", lexica.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("erro sintatico, operacao aritmetica");
                }
            } else {
                /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado uma operação aritmética!", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/
                System.out.println("erro sintatico, operacao aritmetica");
            }
        }
    }

    public void cmdAtribuicao() {
        posToken++;

        if (programToken.get(posToken).getToken().equals("TK_NUM")
                || programToken.get(posToken).getToken().equals("TK_ID")
                /* || programToken.get(posToken).getToken().equals("TK_STRING")*/
                || programToken.get(posToken).getToken().equals("TK_BOOL")
                || programToken.get(posToken).getToken().equals("TK_APAR") /* || programToken.get(posToken).getToken().equals("T_V")*/) {
            posToken++;
            if (!programToken.get(posToken).getToken().equals("TK_FINALLINHA")) {

                if (programToken.get(posToken).getToken().equals("TK_OPER")) {
                    cmdAtribuicao();
                }
            }
        } else {
            if (programToken.get(posToken).getToken().equals("TK_APAR")) {
                opMat();
                while (programToken.get(posToken).getToken().equals("TK_OPER")) {
                    opMat();
                }
                if (programToken.get(posToken).getToken().equals("TK_FPAR")) {
                    if (programToken.get(posToken).getToken().equals("TK_FINALLINHA")) {
                        posToken++;
                    }
                }
            } else {
                /*Erro e = new Erro("ERRO SINTÁTICO! Era esperado uma operação aritmética!", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/

                System.out.println("erro sintatico, operacao aritimetica");
            }
        }

    }

    public void cmdDeclaracao() {
        if (programToken.get(posToken).getToken().equals("TK_INT") || programToken.get(posToken).getToken().equals("TK_DOUBLE") || programToken.get(posToken).getToken().equals("TK_CHAR") || programToken.get(posToken).getToken().equals("TK_BOOL")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_ID")) {
                posToken++;
                if (programToken.get(posToken).getToken().equals("TK_FINALLINHA")) {
                    posToken++;
                } else {
                    if (programToken.get(posToken).getToken().equals("TK_ATRIB")) {
                        cmdAtribuicao();
                    }
                }
            } else {

            }
        }
    }

    public void cmdIf() {
        if (programToken.get(posToken).getToken().equals("TK_IF")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_ACHAVE")) {
                cmds();
                if (programToken.get(posToken).getToken().equals("TK_FCHAVE")) {
                    posToken++;
                } else {
                    /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '}'!", lexica.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("Esperando um }");
                }
            }
            if (programToken.get(posToken).getToken().equals("TK_IF")) {
                posToken++;
                cmdIf();
            }
        }
    }

    public void cmdElse() {
        if (programToken.get(posToken).getToken().equals("TK_ELSE")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_ACHAVE")) {
                cmds();
                if (programToken.get(posToken).getToken().equals("TK_FCHAVE")) {
                    posToken++;
                } else {
                    /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '}'!", lexica.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("esperando um }");
                }
            }
            if (programToken.get(posToken).getToken().equals("TK_IF")) {
                posToken++;
                cmdIf();
            }

        }
    }

    public void cond() {
        if (programToken.get(posToken).getToken().equals("TK_NUM")
                || programToken.get(posToken).getToken().equals("TK_ID")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_COMP")
                    || programToken.get(posToken).getToken().equals("TK_MENOR")
                    || programToken.get(posToken).getToken().equals("TK_MAIOR")
                    || programToken.get(posToken).getToken().equals("TK_MENORIGUAL")
                    || programToken.get(posToken).getToken().equals("TK_MAIORIGUAL")
                    || programToken.get(posToken).getToken().equals("TK_DIFERENTE")) {
                posToken++;
                if (programToken.get(posToken).getToken().equals("TK_NUM")
                        || programToken.get(posToken).getToken().equals("TK_ID")) {
                    posToken++;
                    if (programToken.get(posToken).getToken().equals("TK_AND")
                            || programToken.get(posToken).getToken().equals("TK_OR")) {
                        cond();
                    }
                } else {
                    /* Erro e = new Erro("ERRO SINTÁTICO! Era esperado um número ou variável!", lexica.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("esperando um numero ou variavel");
                }
            } else {
                /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado uma operação de comparação!", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/

                System.out.println("esperando um numero ou variavel");
            }
        } else {
            /*  Erro e = new Erro("ERRO SINTÁTICO: Era esperado um número ou uma variável!", lexica.get(posToken).getLinha());
            tabelaErros.getItems().add(e);*/

            System.out.println("esperando um numero ou variavel");
        }

    }

    public void cmdWhile() {
        if (programToken.get(posToken).getToken().equals("TK_WHILE")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_APAR")) {
                posToken++;
                while (!programToken.get(posToken).getToken().equals("TK_FPAR")) {
                    cond();
                }
            } else {
                /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '('", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/

                System.out.println("esperando um numero ou variavel");
            }
        }
    }

    public void cmdFor() {
        if (programToken.get(posToken).getToken().equals("TK_FOR")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_APAR")) {
                posToken++;
                if (programToken.get(posToken).getToken().equals("TK_ID")) {
                    posToken++;
                    if (programToken.get(posToken).getToken().equals("TK_ATRIB")) {
                        posToken++;
                        cmdAtribuicao();
                        posToken++;
                        cond();
                        if (programToken.get(posToken).getToken().equals("TK_FINALLINHA")) {
                            posToken++;
                            if (programToken.get(posToken).getToken().equals("TK_OPER")) {
                                posToken++;
                                if (programToken.get(posToken).getToken().equals("TK_FPAR")) {
                                    posToken++;
                                } else {
                                    /* Erro e = new Erro("ERRO SINTÁTICO: Era esperado um ')'", lexica.get(posToken).getLinha());
                                    tabelaErros.getItems().add(e);*/

                                    System.out.println("esperando um numero ou variavel");
                                }
                            }
                        }

                    }
                }
            } else {
                /* Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '('", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/

                System.out.println("esperando um numero ou variavel");
            }
        }
    }

    public void cmds() {
        while (posToken < programToken.size() - 1) {
            switch (programToken.get(posToken).getToken()) {
                case "TK_INT":
                    cmdDeclaracao();
                    break;
                case "TK_CHAR":
                    cmdDeclaracao();
                    break;
                case "TK_DOUBLE":
                    cmdDeclaracao();
                    break;
                case "TK_BOOL":
                    cmdDeclaracao();
                    break;

                /*case "TK_ID":
                    if (!programToken.get(posToken + 1).getToken().equals("T_INC")) {
                        cmdAtribuicao();
                    }
                    break;*/
                case "TK_IF":
                    cmdIf();
                    break;
                case "TK_ELSE":
                    cmdElse();
                    break;
                case "TK_WHILE":
                    cmdWhile();
                    break;
                case "TK_FOR":
                    cmdFor();
                    break;

            }
            posToken++;
        }

    }

    public void analiseSintatica() {
        posToken = 0;

        if (programToken.get(posToken).getToken().equals("TK_MAIN")) {
            posToken++;
            if (programToken.get(posToken).getToken().equals("TK_ACHAVE")) {
                posToken++;
                cmds();
                if (!programToken.get(posToken).getToken().equals("TK_FCHAVE")) {
                    /*Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '}'!", lexica.get(posToken).getLinha());
                    tabelaErros.getItems().add(e);*/
                    System.out.println("esperando um numero ou variavel");
                }
            } else {
                /* Erro e = new Erro("ERRO SINTÁTICO: Era esperado um '}'!", lexica.get(posToken).getLinha());
                tabelaErros.getItems().add(e);*/
                System.out.println("esperando um numero ou variavel");
            }
        } else {
            /*Erro e = new Erro("ERRO SINTÁTICO: Falta o Main!", lexica.get(posToken).getLinha());
            tabelaErros.getItems().add(e);*/
            System.out.println("esperando um numero ou variavel");
            cmds();
        }
    }

    public void semantica() {
        
        
      //  semantica = new ArrayList<>();
        String lexema = "";
        String tipo = "";
        String token = "";
        String valor = "";

        for (int i = 0; i < programToken.size(); i++) {

            if (programToken.get(i).getToken().equals("TK_INT") || programToken.get(i).getToken().equals("TK_DOUBLE") || programToken.get(i).getToken().equals("TK_CHAR") || programToken.get(i).getToken().equals("TK_BOOL")) 
            {
                //declaracao
                tipo = programToken.get(i).getToken();
                i++;
                //declarção
                if (programToken.get(i).getToken().equals("TK_ID")) {
                    token = programToken.get(i).getToken();
                    lexema = programToken.get(i).getLexema();
                    i++;

                    if (programToken.get(i).getToken().equals("TK_FINALLINHA")) {

                       // valor = programToken.get(i - 1).getToken();
                        if (verificaID(programToken.get(i - 1).getLexema())) {
                            semantica.add(new TabelaSemantica(token, tipo, "-", lexema));
                        } else {
                            listaerros.add(new ListaErros("Variavel ja utilizada",programToken.get(i-1).getLinha()));
                            System.out.println("Variavel ja utilizada");
                        }

                    }
                    else
                    {
                         listaerros.add(new ListaErros("Sem ';'",programToken.get(i-1).getLinha()));
                        System.out.println("Erro sintatico nao tem ;");
                    }
                } else {
                    listaerros.add(new ListaErros("Nao possui ID depois do tipo",programToken.get(i).getLinha()));
                    System.out.println("Erro TK_ID");
                }

            }
        }
        
        for (int i = 0; i < programToken.size(); i++) {
            if(programToken.get(i).getToken().equals("TK_ATRIB"))
            {
                int pos = i-1;
                lexema = programToken.get(i-1).getLexema();
                if(!verificaID(lexema))
                {
                    tipo = buscaTipo(lexema);
                    //String[] vet = new String[1000];
                    String vet="";
                    int cont=0;
                    boolean flag = true;
                    for (int j = i+1; !programToken.get(j).getToken().equals("TK_FINALLINHA") && flag; j++) {
                        if(programToken.get(j).getToken().equals("TK_NUM"))
                        {
                            if(tipo.equals("TK_INT") || tipo.equals("TK_DOUBLE"))
                                vet += programToken.get(j).getLexema();
                            else
                            {
                                flag= false;
                                listaerros.add(new ListaErros("Variavel de tipo 'nao int' recebendo inteiro",programToken.get(i-1).getLinha()));
                                System.out.println("Variavel de tipo nao int recebendo inteiro");
                            }
                        }
                        if(programToken.get(j).getToken().equals("TK_ID") && flag)
                        {
                            if(!verificaID(programToken.get(j).getLexema()))
                            {
                                String tipo2 = buscaTipo(programToken.get(j).getLexema());
                                if((!(tipo.equals("TK_INT") || tipo.equals("TK_DOUBLE")) && (tipo2.equals("TK_INT") || tipo2.equals("TK_DOUBLE"))) || ((tipo.equals("TK_INT") || tipo.equals("TK_DOUBLE")) && !(tipo2.equals("TK_INT") || tipo2.equals("TK_DOUBLE"))))
                                {
                                    listaerros.add(new ListaErros("Erro de tipo",programToken.get(i-1).getLinha()));
                                
                                    System.out.println("Erro de tipo");
                                    flag=false;
                                }
                                else{
                                    if((tipo.equals("TK_CHAR") && !tipo2.equals("TK_CHAR")) ||(!tipo.equals("TK_CHAR") && tipo2.equals("TK_CHAR")) )
                                    {
                                        flag = false;
                                        listaerros.add(new ListaErros("Variavel de tipo 'nao int' recebendo inteiro",programToken.get(i-1).getLinha()));
                                
                                        System.out.println("Erro de tipo");
                                    }
                                    if(flag)
                                        vet+= programToken.get(j).getLexema();
                                    
                                }
                                
                                
                            }
                            else{
                                flag=false;
                                listaerros.add(new ListaErros("variavel recebendo variavel indefinida",programToken.get(i-1).getLinha()));
                                
                                System.out.println("variavel recebendo variavel indefinida");
                            }
                            
                        }
                        if(programToken.get(j).getToken().equals("TK_OPER") && flag)
                        {
                            vet+=programToken.get(j).getLexema();
                        }
                    }
                    
                    for (int j = 0; j < semantica.size(); j++) {
                     
                        TabelaSemantica s = semantica.get(j);
                        if(s.getLexema().equals(lexema+""))
                            s.setValor(vet);
                        
                    }
                    /*for (TabelaSemantica tabelaSemantica : semantica) {
                        
                    }*/
                    
                }
                else
                {
                    listaerros.add(new ListaErros("variavel inexistente",programToken.get(i-1).getLinha()));
                    System.out.println("Variavel inexistente");
                }
            }
        }
        
        for (int i = 0; i < programToken.size(); i++) {
            if(programToken.get(i).getToken().equals("TK_ID"))
            {
                if(verificaID(programToken.get(i).getLexema())){
                    listaerros.add(new ListaErros("Variavel não inicializada",programToken.get(i).getLinha()));
                    System.out.println("ERRO, VARIAVEL NAO INICIALIZADA");
                }
            }
                
        }
        
        boolean flag2 = true;
        for (int i = 0; i < programToken.size() && flag2; i++) {
            if(programToken.get(i).getToken().equals("TK_MAIOR") || programToken.get(i).getToken().equals("TK_MENOR") || programToken.get(i).getToken().equals("TK_MAIORIGUAL") || programToken.get(i).getToken().equals("TK_MENORIGUAL") || programToken.get(i).getToken().equals("TK_DIFERENTE") || programToken.get(i).getToken().equals("TK_COMP"))
            {
                String lexema1 = programToken.get(i-1).getLexema();
                String lexema2 = programToken.get(i+1).getLexema();
                String tipo1 = buscaTipo(lexema1);
                String tipo2 = buscaTipo(lexema2);
                if(!tipo1.equals(tipo2))
                {
                    listaerros.add(new ListaErros("Erro na comparação",programToken.get(i-1).getLinha()));
                    System.out.println("Erro na comparacao");
                    flag2=false;
                }
                
            }
        }

        for (TabelaSemantica tabelaSemantica : semantica) {
            System.out.println("" + tabelaSemantica.getLexema()+"|"+ tabelaSemantica.getTipo()+"|"+tabelaSemantica.getValor());
        }

    }

    public String buscaTipo(String lexema){
        
    for (TabelaSemantica tabelaSemantica : semantica) {
            if (tabelaSemantica.getLexema().equals(lexema)) {
                return tabelaSemantica.getTipo();
            }
        }

        return "";
    }
    public boolean verificaID(String lexema) {

        for (TabelaSemantica tabelaSemantica : semantica) {
            if (tabelaSemantica.getLexema().equals(lexema)) {
                return false;
            }
        }

        return true;

    }

   
    @FXML
    private void clkAbrirArq(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                bufferedReader = new BufferedReader(new FileReader(file));

                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuffer.append(text);
                    stringBuffer.append("\n");
                }
                txaCod.setText(stringBuffer.toString());
            } catch (FileNotFoundException ex) {
            }

        }
    }

}
