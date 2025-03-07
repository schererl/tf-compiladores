    
%{
  import java.io.*;
  import java.util.ArrayList;
%}


%token IDENT, INT, DOUBLE, VOID, BOOL, NUM, STRING
%token LITERAL, AND, MAIN, IF, FUNCT, RETURN
%token STRUCT

%right '=', RETURN
%nonassoc '>'
%left '+'
%left AND
%left '['  

%type <sval> IDENT
%type <ival> NUM
%type <obj> type
%type <obj> typef
%type <obj> exp

%%

prog : { currClass = ClasseID.VarGlobal; } dList fList { currClass = ClasseID.VarGlobal;
                                                         currEscopo = null; } main ;

/* LISTA DE FUNCOES */
fList :  declFunc fList 
      |
      ;

declFunc : FUNCT typef IDENT { ts.insert(new TS_entry($3, (TS_entry)$2, ClasseID.NomeFuncao)); //HERE
                              currEscopo = ts.pesquisa($3);
                            }  nDeclfuncT
                            
         ;


nDeclfuncT : '('')' dList bloco
          |  '('lstArgs')' dList bloco
          ;
lstArgs : arg ',' lstArgs
        | arg
        ;

arg : type IDENT {  TS_entry nodo = ts.pesquisa(currEscopo.getId());
                    TS_entry newArg = new TS_entry($2, (TS_entry)$1, ClasseID.ArgFuncao, nodo);
                    ts.insert(newArg); //here
                    nodo.addArgs((TS_entry)$1);
                  }
                ;
typef: type | VOID   { $$ = Tp_VOID; } ;

/* LISTA DE DECLARACOES */
dList : decl dList | ;

decl : type  {currentType = (TS_entry)$1; } 
       TArray Lid ';' {}
      ;

Lid : Lid  ',' id 
    | id  
    ;

id : IDENT   { 
                TS_entry nodo = ts.pesquisa($1, currEscopo);
                if (nodo != null) 
                  yyerror("(sem) variavel >" + $1 + "< jah declarada");
                else if(currEscopo != null){ 
                  ts.insert(new TS_entry($1, currentType, ClasseID.VarLocal, currEscopo)); //here
                }
                else{
                  ts.insert(new TS_entry($1, currentType, currClass));  
                }
            }
    
    ;

TArray : '[' NUM ']'  { currentType = new TS_entry("?", Tp_ARRAY, 
                                                   currClass, $2, currentType); }
          TArray
       |
       ;
 

             //
              // faria mais sentido reconhecer todos os tipos como ident! 
              // 
type : INT    { $$ = Tp_INT; }
     | DOUBLE  { $$ = Tp_DOUBLE; }
     | BOOL   { $$ = Tp_BOOL; }
     ;



main :  VOID MAIN '(' ')' bloco ;

bloco : '{' listacmd '}';

listacmd : listacmd cmd
        |
         ;

cmd :  exp ';' 
      | IF '(' exp ')' cmd   {  if ( ((TS_entry)$3) != Tp_BOOL) 
                                     yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)$3).getTipo());
                             }     
       ;


exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
    | exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
    | exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); } 
    | NUM         { $$ = Tp_INT; }      
    | '(' exp ')' { $$ = $2; }
    | IDENT       { TS_entry nodo = null;
                    //verifica se encontra o ident dentro do escopo
                    if(currEscopo != null){
                      nodo = ts.pesquisa($1, currEscopo);
                    }
                    //se nao encontra procurra no global
                    if(nodo==null){
                      nodo = ts.pesquisa($1, null);
                    }

                    if (nodo == null) {
                      yyerror("(sem) var <" + $1 + "> nao declarada"); 
                      $$ = Tp_ERRO;    
                    }           
                    else
                        $$ = nodo.getTipo();
                  }                   
     | exp '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  } 
     | exp '[' exp ']'  {  if ((TS_entry)$3 != Tp_INT) 
                              yyerror("(sem) indexador não é numérico ");
                           else 
                               if (((TS_entry)$1).getTipo() != Tp_ARRAY)
                                  yyerror("(sem) elemento não indexado ");
                               else 
                                  $$ = ((TS_entry)$1).getTipoBase();
                         } 
    | IDENT '('')' { 
                      TS_entry nodo = ts.pesquisa($1, currEscopo); //here
                      if(nodo == null){
                        yyerror("funcao <" + $1 + "> nao declarada!");
                        $$ = Tp_ERRO;
                      }
                      else{
                        if(nodo.getArgs().size() == 0){
                          $$ = nodo.getTipo();
                        }else{
                          yyerror("funcao <" + $1 + "> espera 0 argumentos, recebeu: <" + nodo.getArgs().size() + ">");
                          $$ = Tp_ERRO;
                        } 
                      }
                  }
     | IDENT '(' {lstParams.clear();} LExp ')' {
                          TS_entry nodo = ts.pesquisa($1);
                          Boolean erro = false;
                          
                          if(nodo == null){
                            yyerror("(sem) funct <" + $1 + "> nao declarada"); 
                            $$ = Tp_ERRO;
                            erro = true;
                          }
                          
                          else if(nodo.getArgs().size() != lstParams.size()){
                            yyerror("funct <" + $1 + "> espera <"+ nodo.getArgs().size() +"> argumento(s), mas recebeu <" + lstParams.size() + "> argumento(s)!"); 
                            $$ = Tp_ERRO;
                            erro = true;
                          }
                          
                          else{
                            for(int i=0; i < nodo.getArgs().size(); i++){
                              if(nodo.getArgs().get(i) != lstParams.get(i)){
                                String funcArgs = "";
                                String funcParam = "";
                                for(int j=0; j < nodo.getArgs().size(); j++){
                                  TS_entry arg = nodo.getArgs().get(j);
                                  funcArgs+= arg.getTipoStr() + " ";
                                }
                                for(int j=0; j < lstParams.size(); j++){
                                  TS_entry param = lstParams.get(j); 
                                  funcParam+= param.getTipoStr() + " ";
                                }
                                //erro = True;
                                yyerror("funct <" + $1 + "> espera <" + nodo.getArgs().size() + "> parametro(s) < "+ funcArgs +">  mas recebeu <" + lstParams.size() + "> <"  + funcParam + "> !"); 
                                $$ = Tp_ERRO;
                                erro = true;
                                break;
                              }
                            }
                          }
                          if(!erro)
                            $$ = nodo.getTipo();
                        }
      | RETURN exp {
                      if($2 != currEscopo.getTipo()){
                        yyerror("Erro funcao <" + currEscopo.getId() +"> tipo retornado <" + ((TS_entry)$2).getTipoStr() +"> eh diferente do especificado na declaracao <"+currEscopo.getTipo().getId()+">");
                        $$ = Tp_ERRO;
                      }
                  }
    ;
    LExp: LExp ',' exp {lstParams.add((TS_entry)$3);}
      | exp {lstParams.add((TS_entry)$1);}
    ;

%%

  private Yylex lexer;

  private TabSimb ts;
  private TabSimb tf;

  public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
  public static TS_entry Tp_VOID = new TS_entry("void", null,  ClasseID.TipoBase);
  public static TS_entry Tp_ARRAY = new TS_entry("array", null,  ClasseID.TipoBase);
  
  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  ClasseID.TipoBase);

  public static final int ARRAY = 1500;
  public static final int ATRIB = 1600;

  private TS_entry currEscopo;
  private ClasseID currClass;

  private ArrayList<TS_entry> lstParams;

  private TS_entry currentType;

  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    //System.err.println("Erro (linha: "+ lexer.getLine() + ")\tMensagem: "+error);
    System.err.printf("Erro (linha: %2d) \tMensagem: %s\n", lexer.getLine(), error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);

    ts = new TabSimb();
    tf = new TabSimb();
    lstParams = new ArrayList<TS_entry>();

    //
    // não me parece que necessitem estar na TS
    // já que criei todas como public static...
    //
    ts.insert(Tp_ERRO);
    ts.insert(Tp_INT);
    ts.insert(Tp_DOUBLE);
    ts.insert(Tp_BOOL);
    ts.insert(Tp_VOID);
    ts.insert(Tp_ARRAY);
    

  }  

  public void setDebug(boolean debug) {
    yydebug = debug;
  }

  public void listarTS() { ts.listar();}

  public void listarTF() { tf.listar();}

  public static void main(String args[]) throws IOException {
    System.out.println("\n\nVerificador semantico simples\n");
    

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Programa de entrada:\n");
        yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();

      yyparser.listarTS();

      System.out.print("\n\nFeito!\n");
    
  }


   TS_entry validaTipo(int operador, TS_entry A, TS_entry B) {
      if(A == Tp_VOID || B == Tp_VOID){
        yyerror("Tipo void nao pode ser usado dentro de uma expressao");
        return Tp_ERRO;
      } 

         switch ( operador ) {
              case ATRIB:
                    if ( (A == Tp_INT && B == Tp_INT)                        ||
                         ((A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE))) ||
                         (A == B) )
                         return A;
                     else
                         yyerror("(sem) tipos incomp. para atribuicao: "+ A.getTipoStr() + " = "+B.getTipoStr());
                    break;

              case '+' :
                    if ( A == Tp_INT && B == Tp_INT)
                          return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                                            (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else
                        yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;

             case '>' :
                     if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE))
                         return Tp_BOOL;
                      else
                        yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
                      break;

             case AND:
                     if (A == Tp_BOOL && B == Tp_BOOL)
                         return Tp_BOOL;
                      else
                        yyerror("(sem) tipos incomp. para op lógica: "+ A.getTipoStr() + " && "+B.getTipoStr());
                 break;
            }

            return Tp_ERRO;
           
     }

