//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 ".\exemploSem.y"
  import java.io.*;
  import java.util.ArrayList;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENT=257;
public final static short INT=258;
public final static short DOUBLE=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short STRING=262;
public final static short LITERAL=263;
public final static short AND=264;
public final static short VOID=265;
public final static short MAIN=266;
public final static short IF=267;
public final static short FUNCT=268;
public final static short STRUCT=269;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    4,    7,    0,    5,    5,   10,    8,    9,    9,   12,
   12,   13,    3,    3,   16,   14,   17,   17,   18,   19,
   15,   15,    1,    1,    1,    6,   11,   20,   20,   21,
   21,    2,    2,    2,    2,    2,    2,    2,    2,    2,
   23,    2,   22,   22,
};
final static short yylen[] = {                            2,
    0,    0,    5,    2,    0,    0,    5,    4,    5,    3,
    1,    2,    2,    0,    0,    5,    3,    1,    1,    0,
    5,    0,    1,    1,    1,    5,    3,    2,    0,    2,
    5,    3,    3,    3,    1,    3,    1,    3,    4,    3,
    0,    5,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   23,   24,   25,   15,    0,    0,    0,    0,
    2,    0,   13,    0,    0,    0,    0,    4,    0,   19,
    0,   18,    6,    0,    3,   20,    0,   16,    0,    0,
    0,   17,    0,    7,    0,   21,    0,    0,    0,    0,
    0,    0,   12,    0,    0,   29,   26,    8,    0,   10,
    0,    9,    0,   35,    0,    0,   27,    0,   28,    0,
    0,    0,    0,    0,    0,    0,    0,   30,   40,    0,
    0,   36,    0,    0,    0,    0,    0,    0,    0,    0,
   39,   42,    0,   31,    0,
};
final static short yydgoto[] = {                          1,
    6,   58,    7,    2,   11,   25,   17,   12,   34,   29,
   47,   39,   40,    8,   15,    9,   21,   22,   31,   51,
   59,   79,   70,
};
final static short yysindex[] = {                         0,
    0, -156,    0,    0,    0,    0, -251, -156,  -72, -156,
    0, -251,    0, -232, -211, -201, -208,    0,  -22,    0,
   -1,    0,    0, -191,    0,    0, -211,    0,   42,   43,
  -72,    0,   -8,    0,   56,    0, -156, -159,   64,   63,
  -15,  -15,    0, -156, -156,    0,    0,    0,  -15,    0,
  -40,    0,   69,    0,   70,  -32,    0,  -23,    0,   72,
  -32,  -30,  -32,  -32,  -32,  -32,  -32,    0,    0,  -32,
  -27,    0,   20,  -17,  -41,  -87,  -21,  -17,   50,  -39,
    0,    0,  -32,    0,  -17,
};
final static short yyrindex[] = {                         0,
    0, -192,    0,    0,    0,    0, -151, -111, -142,    0,
    0, -151,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -142,    0,    0,    0,    0,    0,   -7,    0,    0,   76,
    0,    0,    0,   -7,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,  -25,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -34,    7,    8,   19,    0,   51,    0,    0,
    0,    0,    0,    0,   55,
};
final static short yygindex[] = {                         0,
   32,   23,   10,    0,  106,    0,    0,    0,    0,    0,
  -12,   74,    0,    0,   89,    0,    0,   94,    0,    0,
   44,    0,    0,
};
final static int YYTABLESIZE=252;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   56,   66,   37,   67,   37,   37,   34,   56,   34,   34,
   72,   14,   66,   80,   41,   66,   10,   13,   14,   66,
   37,   66,   37,   37,   34,   66,   34,   34,   19,   48,
   64,   65,   37,   64,   65,   68,   52,   64,   65,   64,
   65,   16,   27,   64,   65,   20,   42,   38,   33,   67,
   38,   33,   37,   49,   37,   23,   24,   28,   34,   32,
   67,   32,   32,   67,   38,   38,   33,   67,   33,   67,
   26,   81,   14,   67,   30,   14,   38,   32,   62,   32,
   32,   33,   35,   71,   57,   73,   74,   75,   76,   77,
   82,   44,   78,   83,   44,   43,   41,   43,   43,   38,
   33,    3,    4,    5,   44,   85,   45,   46,   60,   61,
   67,   32,   69,    5,   22,   14,   11,   18,   50,   36,
   32,    0,    0,   84,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   14,    0,    0,   14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   53,   53,    0,    0,
   54,   54,   63,    0,   53,   37,   55,   55,   54,   34,
    0,   41,    0,   63,    0,   41,   63,    0,    0,    0,
   63,    0,   63,    0,    0,    0,   63,    0,    0,    3,
    4,    5,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   43,   41,   91,   43,   44,   41,   40,   43,   44,
   41,  123,   43,   41,   40,   43,  268,    8,   91,   43,
   59,   43,   61,   62,   59,   43,   61,   62,  261,   42,
   61,   62,   41,   61,   62,   59,   49,   61,   62,   61,
   62,   10,   44,   61,   62,  257,   37,   41,   41,   91,
   44,   44,   91,   44,   93,  257,  265,   59,   93,   41,
   91,   43,   44,   91,   33,   59,   59,   91,   61,   91,
   93,   93,  265,   91,  266,  268,   45,   59,   56,   61,
   62,   40,   40,   61,  125,   63,   64,   65,   66,   67,
   41,   41,   70,   44,   44,   41,   41,  257,   44,   93,
   93,  258,  259,  260,   41,   83,   44,  123,   40,   40,
   91,   93,   41,  265,  257,  123,   41,   12,   45,   31,
   27,   -1,   -1,   80,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  265,   -1,   -1,  268,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  264,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,   -1,
  261,  261,  264,   -1,  257,  264,  267,  267,  261,  264,
   -1,  257,   -1,  264,   -1,  261,  264,   -1,   -1,   -1,
  264,   -1,  264,   -1,   -1,   -1,  264,   -1,   -1,  258,
  259,  260,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=269;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,"'+'","','",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"IDENT","INT","DOUBLE","BOOL","NUM",
"STRING","LITERAL","AND","VOID","MAIN","IF","FUNCT","STRUCT",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"$$2 :",
"prog : $$1 dList fList $$2 main",
"fList : declFunc fList",
"fList :",
"$$3 :",
"declFunc : FUNCT type IDENT $$3 nDeclfuncT",
"nDeclfuncT : '(' ')' dList bloco",
"nDeclfuncT : '(' lstArgs ')' dList bloco",
"lstArgs : arg ',' lstArgs",
"lstArgs : arg",
"arg : type IDENT",
"dList : decl dList",
"dList :",
"$$4 :",
"decl : type $$4 TArray Lid ';'",
"Lid : Lid ',' id",
"Lid : id",
"id : IDENT",
"$$5 :",
"TArray : '[' NUM ']' $$5 TArray",
"TArray :",
"type : INT",
"type : DOUBLE",
"type : BOOL",
"main : VOID MAIN '(' ')' bloco",
"bloco : '{' listacmd '}'",
"listacmd : listacmd cmd",
"listacmd :",
"cmd : exp ';'",
"cmd : IF '(' exp ')' cmd",
"exp : exp '+' exp",
"exp : exp '>' exp",
"exp : exp AND exp",
"exp : NUM",
"exp : '(' exp ')'",
"exp : IDENT",
"exp : exp '=' exp",
"exp : exp '[' exp ']'",
"exp : IDENT '(' ')'",
"$$6 :",
"exp : IDENT '(' $$6 LExp ')'",
"LExp : LExp ',' exp",
"LExp : exp",
};

//#line 205 ".\exemploSem.y"

  private Yylex lexer;

  private TabSimb ts;
  private TabSimb tf;

  public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
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

//#line 432 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 25 ".\exemploSem.y"
{ currClass = ClasseID.VarGlobal; }
break;
case 2:
//#line 25 ".\exemploSem.y"
{ currClass = ClasseID.VarGlobal;
                                                         currEscopo = null; }
break;
case 6:
//#line 33 ".\exemploSem.y"
{ ts.insert(new TS_entry(val_peek(0).sval, (TS_entry)val_peek(1).obj, ClasseID.NomeFuncao)); /*HERE*/
                              currEscopo = ts.pesquisa(val_peek(0).sval);
                            }
break;
case 12:
//#line 46 ".\exemploSem.y"
{  TS_entry nodo = ts.pesquisa(currEscopo.getId());
                    TS_entry newArg = new TS_entry(val_peek(0).sval, (TS_entry)val_peek(1).obj, ClasseID.ArgFuncao, nodo);
                    ts.insert(newArg); /*here*/
                    nodo.addArgs((TS_entry)val_peek(1).obj);
                  }
break;
case 15:
//#line 59 ".\exemploSem.y"
{currentType = (TS_entry)val_peek(0).obj; }
break;
case 16:
//#line 60 ".\exemploSem.y"
{}
break;
case 19:
//#line 67 ".\exemploSem.y"
{ 
                TS_entry nodo = ts.pesquisa(val_peek(0).sval, currEscopo);
                if (nodo != null) 
                  yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
                else if(currEscopo != null){ 
                  ts.insert(new TS_entry(val_peek(0).sval, currentType, ClasseID.VarLocal, currEscopo)); /*here*/
                }
                else{
                  ts.insert(new TS_entry(val_peek(0).sval, currentType, currClass));  
                }
            }
break;
case 20:
//#line 81 ".\exemploSem.y"
{ currentType = new TS_entry("?", Tp_ARRAY, 
                                                   currClass, val_peek(1).ival, currentType); }
break;
case 23:
//#line 91 ".\exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 24:
//#line 92 ".\exemploSem.y"
{ yyval.obj = Tp_DOUBLE; }
break;
case 25:
//#line 93 ".\exemploSem.y"
{ yyval.obj = Tp_BOOL; }
break;
case 31:
//#line 107 ".\exemploSem.y"
{  if ( ((TS_entry)val_peek(2).obj) != Tp_BOOL) 
                                     yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)val_peek(2).obj).getTipo());
                             }
break;
case 32:
//#line 113 ".\exemploSem.y"
{ yyval.obj = validaTipo('+', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 33:
//#line 114 ".\exemploSem.y"
{ yyval.obj = validaTipo('>', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 34:
//#line 115 ".\exemploSem.y"
{ yyval.obj = validaTipo(AND, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 35:
//#line 116 ".\exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 36:
//#line 117 ".\exemploSem.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 37:
//#line 118 ".\exemploSem.y"
{ TS_entry nodo = null;
                    /*verifica se encontra o ident dentro do escopo*/
                    if(currEscopo != null){
                      nodo = ts.pesquisa(val_peek(0).sval, currEscopo);
                    }
                    /*se nao encontra procurra no global*/
                    if(nodo==null){
                      nodo = ts.pesquisa(val_peek(0).sval, null);
                    }

                    if (nodo == null) {
                      yyerror("(sem) var <" + val_peek(0).sval + "> nao declarada"); 
                      yyval.obj = Tp_ERRO;    
                    }           
                    else
                        yyval.obj = nodo.getTipo();
                  }
break;
case 38:
//#line 135 ".\exemploSem.y"
{  yyval.obj = validaTipo(ATRIB, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj);  }
break;
case 39:
//#line 136 ".\exemploSem.y"
{  if ((TS_entry)val_peek(1).obj != Tp_INT) 
                              yyerror("(sem) indexador não é numérico ");
                           else 
                               if (((TS_entry)val_peek(3).obj).getTipo() != Tp_ARRAY)
                                  yyerror("(sem) elemento não indexado ");
                               else 
                                  yyval.obj = ((TS_entry)val_peek(3).obj).getTipoBase();
                         }
break;
case 40:
//#line 144 ".\exemploSem.y"
{ 
                      TS_entry nodo = ts.pesquisa(val_peek(2).sval, currEscopo); /*here*/
                      if(nodo == null){
                        yyerror("funcao <" + val_peek(2).sval + "> nao declarada!");
                        yyval.obj = Tp_ERRO;
                      }
                      else{
                        if(nodo.getArgs().size() == 0){
                          yyval.obj = nodo.getTipo();
                        }else{
                          yyerror("funcao <" + val_peek(2).sval + "> espera 0 argumentos, recebeu: <" + nodo.getArgs().size() + ">");
                          yyval.obj = Tp_ERRO;
                        } 
                      }
                  }
break;
case 41:
//#line 159 ".\exemploSem.y"
{lstParams.clear();}
break;
case 42:
//#line 159 ".\exemploSem.y"
{
                          TS_entry nodo = ts.pesquisa(val_peek(4).sval);
                          Boolean erro = false;
                          
                          if(nodo == null){
                            yyerror("(sem) funct <" + val_peek(4).sval + "> nao declarada"); 
                            yyval.obj = Tp_ERRO;
                            erro = true;
                          }
                          
                          else if(nodo.getArgs().size() != lstParams.size()){
                            yyerror("funct <" + val_peek(4).sval + "> espera <"+ nodo.getArgs().size() +"> argumento(s), mas recebeu <" + lstParams.size() + "> argumento(s)!"); 
                            yyval.obj = Tp_ERRO;
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
                                /*erro = True;*/
                                yyerror("funct <" + val_peek(4).sval + "> espera <" + nodo.getArgs().size() + "> parametro(s) < "+ funcArgs +">  mas recebeu <" + lstParams.size() + "> <"  + funcParam + "> !"); 
                                yyval.obj = Tp_ERRO;
                                erro = true;
                                break;
                              }
                            }
                          }
                          if(!erro)
                            yyval.obj = nodo.getTipo();
                        }
break;
case 43:
//#line 200 ".\exemploSem.y"
{lstParams.add((TS_entry)val_peek(0).obj);}
break;
case 44:
//#line 201 ".\exemploSem.y"
{lstParams.add((TS_entry)val_peek(0).obj);}
break;
//#line 777 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
