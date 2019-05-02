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






//#line 6 "cminus.y"
import java.io.*;
import java.util.*;
//#line 20 "Parser.java"




public class Parser
             implements ParserTokens
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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    2,    0,    1,    1,    3,    3,    4,    4,    6,    6,
    7,   10,    5,    8,    8,    8,   11,   11,   12,   12,
   13,    9,   14,   14,   15,   15,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   23,   17,   17,   18,   19,
   26,   21,   22,   20,   20,   25,   25,   28,   28,   28,
   28,   28,   28,   27,   27,   29,   29,   30,   30,   31,
   31,   32,   32,   32,   32,   32,   24,   33,   33,   34,
   34,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    1,    1,    3,    6,    1,    1,
    0,    0,    8,    1,    1,    0,    3,    1,    2,    4,
    0,    5,    2,    0,    2,    0,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    4,    7,    7,    5,
    0,    6,    6,    2,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    3,    1,    1,    1,    3,    1,    1,
    1,    3,    1,    4,    1,    1,    4,    1,    0,    3,
    1,
};
final static short yydefred[] = {                         1,
    0,    0,   10,    9,    0,    4,    5,    6,    0,    3,
    0,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,    8,    0,   12,    0,    0,   21,   17,   20,
   13,    0,   24,    0,   23,    0,    0,    0,    0,   35,
   22,    0,    0,    0,    0,   28,   25,   27,   29,   30,
   31,   32,   33,   34,    0,    0,    0,    0,   41,    0,
    0,   66,   44,    0,   65,    0,    0,    0,   59,    0,
   36,    0,    0,   71,    0,    0,    0,    0,    0,    0,
    0,   45,   56,   57,   49,   50,   48,   51,   52,   53,
    0,    0,   60,   61,    0,    0,    0,   37,   67,    0,
    0,    0,    0,    0,   62,    0,    0,   58,    0,    0,
   70,    0,    0,   40,   64,    0,   43,    0,   42,    0,
   38,   39,
};
final static short yydgoto[] = {                          1,
    5,    2,    6,    7,    8,    9,   14,   20,   46,   28,
   21,   22,   32,   34,   37,   47,   48,   49,   50,   51,
   52,   53,   54,   65,   66,   78,   67,   91,   92,   68,
   95,   69,   75,   76,
};
final static short yysindex[] = {                         0,
    0, -229,    0,    0, -229,    0,    0,    0, -247,    0,
 -263,    0, -237, -248, -211, -186, -208,    0, -176, -202,
 -174,    0,    0, -161,    0, -229, -159,    0,    0,    0,
    0, -162,    0, -229,    0, -137, -249, -263, -192,    0,
    0, -151, -142, -209, -139,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -138, -219, -207, -207,    0, -207,
 -132,    0,    0, -207,    0, -131, -190, -194,    0, -207,
    0, -135, -126,    0, -129, -125, -128, -207, -127, -207,
 -124,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -207, -207,    0,    0, -207, -123, -122,    0,    0, -207,
 -119, -120, -240, -121,    0, -156, -194,    0, -240, -114,
    0, -207, -113,    0,    0, -130,    0, -111,    0, -240,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  148,    0,    0,    0,    0,    0,
 -112,    0,    0,    0,    0, -110,    0, -214,    0,    0,
 -109,    0,    0, -218,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -250,    0,    0, -117,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -108,    0,    0,    0,
 -175,    0,    0,    0,    0,    0, -257, -158,    0,    0,
    0,    0,    0,    0,    0, -107,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -117,    0,    0, -134, -141,    0, -117,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -117,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  156,  128,    0,  -12,    0,    0,  139,    0,
    0,  142,    0,    0,    0,  -62,    0,    0,    0,    0,
    0,    0,    0,  -37,  -55,    0,   78,    0,    0,   79,
    0,   75,    0,    0,
};
final static int YYTABLESIZE=171;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   73,   74,   77,   19,   79,   12,   26,   39,   81,   11,
   13,   47,   47,   19,   96,   47,   39,   47,   26,   40,
   15,   36,  102,   16,  104,   26,   26,   41,   40,   26,
   42,   26,   43,   26,   44,   26,   45,   61,   62,   42,
  114,   43,   10,   44,  111,   45,  116,   61,   62,   61,
   62,   19,   64,    3,   19,    4,  118,  122,   15,   63,
   23,   72,   64,   17,   64,   55,   93,   94,   83,   84,
   25,   55,   85,   86,   87,   88,   89,   90,   56,   57,
   24,   58,   55,   63,   63,   63,   63,   63,   63,   63,
   63,   63,   63,   63,   63,   26,   18,   63,    4,   63,
   55,   55,   83,   84,   55,   55,   55,   55,   55,   55,
   55,   55,   27,   33,   55,   30,   55,   54,   54,   38,
   59,   54,   54,   54,   54,   54,   54,   54,   54,   60,
   71,   54,   70,   54,   46,   46,   97,   82,   46,   57,
   46,   80,   98,   99,  100,  103,  101,    2,  105,  109,
  110,  112,  113,  115,  117,  119,  120,  121,   21,   11,
   10,   35,   16,   14,   69,   68,   31,   29,  106,  108,
  107,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   56,   57,   58,   16,   60,  269,  257,  257,   64,  257,
  274,  269,  270,   26,   70,  273,  257,  275,  269,  269,
  258,   34,   78,  272,   80,  276,  277,  277,  269,  280,
  280,  282,  282,  284,  284,  286,  286,  257,  258,  280,
  103,  282,  257,  284,  100,  286,  109,  257,  258,  257,
  258,  270,  272,  283,  273,  285,  112,  120,  273,  269,
  269,  281,  272,  275,  272,  103,  261,  262,  259,  260,
  273,  109,  263,  264,  265,  266,  267,  268,  271,  272,
  257,  274,  120,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  270,  283,  273,  285,  275,
  259,  260,  259,  260,  263,  264,  265,  266,  267,  268,
  269,  270,  274,  276,  273,  275,  275,  259,  260,  257,
  272,  263,  264,  265,  266,  267,  268,  269,  270,  272,
  269,  273,  272,  275,  269,  270,  272,  269,  273,  272,
  275,  274,  269,  273,  270,  273,  275,    0,  273,  273,
  273,  271,  273,  275,  269,  269,  287,  269,  276,  272,
    5,   34,  273,  273,  273,  273,   28,   26,   91,   95,
   92,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=288;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ID","NUM","PLUS","MINUS","MULT","DIVIDE","LT","GT","LTE","GTE",
"EQ","NOTEQ","SEMI","COMMA","ASSIGN","LPAREN","RPAREN","LBRACKET","RBRACKET",
"LCBRACKET","RCBRACKET","LCOMMENT","RCOMMENT","PRINT","INPUT","WHILE","VOID",
"RETURN","INT","IF","ELSE","ERROR",
};
final static String yyrule[] = {
"$accept : program",
"$$1 :",
"program : $$1 declaration_list",
"declaration_list : declaration_list declaration",
"declaration_list : declaration",
"declaration : var_declaration",
"declaration : fun_declaration",
"var_declaration : type_specifier ID SEMI",
"var_declaration : type_specifier ID LBRACKET NUM RBRACKET SEMI",
"type_specifier : INT",
"type_specifier : VOID",
"$$2 :",
"$$3 :",
"fun_declaration : type_specifier ID $$2 LPAREN params RPAREN $$3 compound_stmt",
"params : param_list",
"params : VOID",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_specifier ID",
"param : type_specifier ID LBRACKET RBRACKET",
"$$4 :",
"compound_stmt : $$4 LCBRACKET local_declarations statement_list RCBRACKET",
"local_declarations : local_declarations var_declaration",
"local_declarations :",
"statement_list : statement_list statement",
"statement_list :",
"statement : assign_stmt",
"statement : compound_stmt",
"statement : selection_stmt",
"statement : iteration_stmt",
"statement : return_stmt",
"statement : print_stmt",
"statement : input_stmt",
"statement : call_stmt",
"statement : SEMI",
"call_stmt : call SEMI",
"assign_stmt : ID ASSIGN expression SEMI",
"assign_stmt : ID LBRACKET expression RBRACKET ASSIGN expression SEMI",
"selection_stmt : IF LPAREN expression RPAREN statement ELSE statement",
"iteration_stmt : WHILE LPAREN expression RPAREN statement",
"$$5 :",
"print_stmt : PRINT LPAREN $$5 expression RPAREN SEMI",
"input_stmt : ID ASSIGN INPUT LPAREN RPAREN SEMI",
"return_stmt : RETURN SEMI",
"return_stmt : RETURN expression SEMI",
"expression : additive_expression relop additive_expression",
"expression : additive_expression",
"relop : LTE",
"relop : LT",
"relop : GT",
"relop : GTE",
"relop : EQ",
"relop : NOTEQ",
"additive_expression : additive_expression addop term",
"additive_expression : term",
"addop : PLUS",
"addop : MINUS",
"term : term mulop factor",
"term : factor",
"mulop : MULT",
"mulop : DIVIDE",
"factor : LPAREN expression RPAREN",
"factor : ID",
"factor : ID LBRACKET expression RBRACKET",
"factor : call",
"factor : NUM",
"call : ID LPAREN args RPAREN",
"args : arg_list",
"args :",
"arg_list : arg_list COMMA expression",
"arg_list : expression",
};

//#line 420 "cminus.y"

/* reference to the lexer object */
private static Yylex lexer;

/* The symbol table */
public final SymTab<SymTabRec> symtab = new SymTab<SymTabRec>();

/* To check if main has been encountered and is the last declaration */
private boolean seenMain = false;

/* To take care of nuance associated with params and decls in compound stsmt */
private boolean firstTime = false;

/* To gen boilerplate code for read only if input was encountered  */
private boolean usesRead = false;

/* Interface to the lexer */
private int yylex()
{
    int retVal = -1;
    try
	{
		retVal = lexer.yylex();
    }
	catch (IOException e)
	{
		System.err.println("IO Error:" + e);
    }
    return retVal;
}
	
/* error reporting */
public void yyerror (String error)
{
    System.err.println("Parse Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol() + ". Got: " + lexer.yytext());
}

/* For semantic errors */
public void semerror (String error)
{
	if (ParseMain.SYMBOL_TABLE_OUTPUT){
    	System.err.println("Semantic Error : " + error + " at line " + 
			lexer.getLine() + " column " + 
			lexer.getCol());
		}
}

/* constructor taking in File Input */
public Parser (Reader r)
{
	lexer = new Yylex(r, this);
}

/* This is how to invoke the parser

public static void main (String [] args) throws IOException
{
	Parser yyparser = new Parser(new FileReader(args[0]));
	yyparser.yyparse();
}

*/

//#line 413 "Parser.java"
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
//#line 18 "cminus.y"
{ 
					symtab.enterScope();
					GenCode.genPrologue();
                }
break;
case 2:
//#line 23 "cminus.y"
{
                	if (usesRead) GenCode.genReadMethod();
                	GenCode.genClassConstructor();
                	GenCode.genEpilogue(symtab);
                	symtab.exitScope();
                	if (!seenMain) semerror("No main in file"); 
				}
break;
case 7:
//#line 40 "cminus.y"
{

						int vartype = val_peek(2).ival;
						String name = val_peek(1).sval;
						int scope = symtab.getScope();

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of variable " + name + " in the current scope");
						}
						else if(vartype == VOID){
							semerror("Variable " + name + " cannot be of type void");
						}
						else
						{
						/*Symbol table add*/
						SymTabRec rec = new VarRec(name, scope, vartype);
						symtab.insert(name, rec);

						if (scope == 0)
							GenCode.genStaticDecl(rec); 
						}
					}
break;
case 8:
//#line 64 "cminus.y"
{
						int vartype = val_peek(5).ival;
						String name = val_peek(4).sval;
						int scope = symtab.getScope();
						int arrayLength = val_peek(2).ival;

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of array " + name + " in the current scope");
						}
						else if(vartype == VOID){
							semerror("Array " + name + " cannot be of type void");
						}
						else
						{
						/*Symbol table add*/
						SymTabRec rec = new ArrRec(name, scope, vartype, arrayLength);
						symtab.insert(name, rec); 
						}
					}
break;
case 9:
//#line 86 "cminus.y"
{ yyval = val_peek(0); }
break;
case 10:
//#line 87 "cminus.y"
{ yyval = val_peek(0); }
break;
case 11:
//#line 91 "cminus.y"
{
						

						int funtype = val_peek(1).ival;
						String name = val_peek(0).sval;
						int scope = symtab.getScope();

						/*List<SymTabRec> params = $4.ival;*/

						/* Create a symbol table record*/
						FunRec rec = new FunRec(name, scope, funtype, null);
						yyval = new ParserVal(rec);

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of function " + name + " in the current scope");
						}
						else if (!seenMain){
							SymTabRec.setJVMNum(0);
							/* Insert record into symbol table*/
							symtab.insert(name,rec);

							if(name.equals("main")){
								seenMain = true;
								SymTabRec.setJVMNum(1);
							}
						}
						else{
							semerror("Function " + name + " cannot be declared after main");
						}
						symtab.enterScope();
					}
break;
case 12:
//#line 124 "cminus.y"
{
						symtab.enterScope();

						int funtype = val_peek(5).ival;
						String name = val_peek(4).sval;

						/* Get params from $5*/
						List<SymTabRec> params = (List<SymTabRec>)val_peek(1).obj;
						FunRec rec = ((FunRec)(val_peek(3).obj));
						rec.setParams(params);
						

						if(name.equals("main")){
							if(funtype != VOID){
								semerror("Return type of main function must be void");
							}
							if(val_peek(1).obj != null){
								semerror("Parameters of main function must be void or empty");
							}
						}

						GenCode.genFunBegin(rec);

					}
break;
case 13:
//#line 149 "cminus.y"
{
						firstTime = true;
						GenCode.genFunEnd();
					}
break;
case 14:
//#line 156 "cminus.y"
{ yyval = val_peek(0); }
break;
case 15:
//#line 157 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 16:
//#line 158 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 17:
//#line 162 "cminus.y"
{
						List<SymTabRec> params = (List<SymTabRec>)val_peek(2).obj;
						params.add((SymTabRec)val_peek(0).obj);
						yyval = new ParserVal(params);

					}
break;
case 18:
//#line 169 "cminus.y"
{
						List<SymTabRec> params =  new ArrayList<SymTabRec>();
						params.add((SymTabRec)val_peek(0).obj);
						yyval = new ParserVal(params);
					}
break;
case 19:
//#line 177 "cminus.y"
{
						int vartype = val_peek(1).ival;
						String name = val_peek(0).sval;
						int scope = symtab.getScope();
						VarRec rec = new VarRec(name,scope,vartype);
						yyval = new ParserVal(rec);
						if (symtab.lookup(name))
						{
							semerror("Redeclaration of param "+ name + " in the current scope");
						}
						else
						{
							symtab.insert(name,rec);
						}
					}
break;
case 20:
//#line 193 "cminus.y"
{
						int vartype = val_peek(3).ival;
						String name = val_peek(2).sval;
						int scope = symtab.getScope();
						SymTabRec rec = new ArrRec(name,scope,vartype,-1);
						yyval = new ParserVal(rec);
						if (symtab.lookup(name))
						{
							semerror("Redeclaration of param array "+ name + " in the current scope");
						}
						else
						{
							symtab.insert(name,rec);
							GenCode.genArrInit(rec);
						}
					}
break;
case 21:
//#line 211 "cminus.y"
{
						if(firstTime){
							firstTime = false;
						}
						else{
							symtab.enterScope();
						}
					}
break;
case 22:
//#line 220 "cminus.y"
{
						symtab.exitScope();
					}
break;
case 37:
//#line 249 "cminus.y"
{
						String name = val_peek(3).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared variable" + name + "in the current scope");
						}
						else if(!rec.isVar())
						{
							semerror("Name " + name + " is not a variable in the current scope");
						}
						else if(rec.type != val_peek(1).ival){
							semerror("Type mismatch of variable " + name + ". Expected " + rec.type);
						}
						else
						{
							GenCode.genStore(rec);
						}

					}
break;
case 38:
//#line 270 "cminus.y"
{
						String name = val_peek(6).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared array" + name + "in the current scope");
						}
						else if(!rec.isArr())
						{
							semerror("Name " + name + " is not an array in the current scope");
						}
					}
break;
case 41:
//#line 291 "cminus.y"
{
						GenCode.genBeginPrint();
					}
break;
case 42:
//#line 295 "cminus.y"
{
						GenCode.genEndPrint();
					}
break;
case 43:
//#line 301 "cminus.y"
{
						String name = val_peek(5).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared variable" + name + " in input statement in the current scope");
						}
						else if(!rec.isVar())
						{
							semerror("Name " + name + " is not a variable in the input statement in the current scope");
						}
						else
						{
							usesRead = true;
							GenCode.genRead(rec);
						}
					}
break;
case 63:
//#line 354 "cminus.y"
{
						String name = val_peek(0).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared factor variable " + name + "in the current scope");
						}
						else if(!rec.isVar() && !rec.isArr())
						{
							System.out.println(rec);
							semerror("Name " + name + " is not a factor variable in the current scope");
						}
						else
						{
							if (rec.isVar())
								GenCode.genLoadVar(rec);
							else
								GenCode.genLoadArrAddr(rec);
						}
					}
break;
case 64:
//#line 375 "cminus.y"
{
						String name = val_peek(3).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared factor array" + name + "in the current scope");
						}
						else if(!rec.isArr())
						{
							semerror("Name " + name + " is not a factor array in the current scope");
						}

					}
break;
case 66:
//#line 390 "cminus.y"
{
						int num = val_peek(0).ival;
						GenCode.genLoadConst(num);
					}
break;
case 67:
//#line 397 "cminus.y"
{
						String name = val_peek(3).sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared function" + name + "in the current scope");
						}
						else if(!rec.isFun())
						{
							semerror("Name " + name + " is not a function in the current scope");
						}
					}
break;
//#line 919 "Parser.java"
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
