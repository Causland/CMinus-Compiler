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
    5,    7,    7,    7,    9,    9,   10,   10,   11,    8,
   12,   12,   13,   13,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   21,   15,   15,   16,   17,   19,   20,
   18,   18,   23,   23,   25,   25,   25,   25,   25,   25,
   24,   24,   26,   26,   27,   27,   28,   28,   29,   29,
   29,   29,   29,   22,   30,   30,   31,   31,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    1,    1,    3,    6,    1,    1,
    6,    1,    1,    0,    3,    1,    2,    4,    0,    5,
    2,    0,    2,    0,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    4,    7,    7,    5,    5,    6,
    2,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    3,    1,    1,    1,    3,    1,    1,    1,    3,    1,
    4,    1,    1,    4,    1,    0,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   10,    9,    0,    4,    5,    6,    0,    3,
    0,    7,    0,    0,    0,    0,    0,    0,   16,    0,
    0,   19,    0,    0,    0,   11,    0,   15,    8,   18,
   22,    0,   21,    0,    0,    0,    0,   33,   20,    0,
    0,    0,    0,   26,   23,   25,   27,   28,   29,   30,
   31,   32,    0,    0,    0,    0,    0,    0,    0,   63,
   41,    0,   62,    0,    0,    0,   56,    0,   34,    0,
    0,   68,    0,    0,    0,    0,    0,    0,    0,   42,
   53,   54,   46,   47,   45,   48,   49,   50,    0,    0,
   57,   58,    0,    0,    0,   35,   64,    0,    0,    0,
    0,    0,   59,    0,    0,   55,    0,    0,   67,    0,
   39,   38,   61,    0,   40,    0,    0,   36,   37,
};
final static short yydgoto[] = {                          1,
    5,    2,    6,    7,    8,    9,   17,   44,   18,   19,
   27,   32,   35,   45,   46,   47,   48,   49,   50,   51,
   52,   63,   64,   65,   89,   90,   66,   93,   67,   73,
   74,
};
final static short yysindex[] = {                         0,
    0, -229,    0,    0, -229,    0,    0,    0, -236,    0,
 -133,    0, -204, -217,    0, -209, -218, -206,    0, -195,
 -162,    0, -229, -171, -160,    0, -163,    0,    0,    0,
    0, -229,    0, -138, -249, -256, -211,    0,    0, -152,
 -143, -207, -140,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -139, -234, -205, -205, -205, -205, -130,    0,
    0, -205,    0, -132, -190, -166,    0, -205,    0, -129,
 -124,    0, -127, -123, -126, -125, -122, -205, -121,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -205, -205,
    0,    0, -205, -120, -119,    0,    0, -205, -116, -113,
 -240, -118,    0, -157, -166,    0, -240, -111,    0, -205,
    0,    0,    0, -137,    0, -110, -240,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  160,    0,    0,    0,    0,    0,
    0,    0, -112,    0, -214,    0,    0, -109,    0,    0,
 -202,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -250,    0,    0, -114,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -108,    0,    0,    0, -176,    0,
    0,    0,    0,    0, -259, -159,    0,    0,    0,    0,
    0,    0,    0, -107,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -114,    0,    0, -135, -142,    0, -114,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -114,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  158,  135,    0,   26,    0,  146,    0,  147,
    0,    0,    0,  -95,    0,    0,    0,    0,    0,    0,
    0,  -35,  -53,   80,    0,    0,   81,    0,   79,    0,
    0,
};
final static int YYTABLESIZE=172;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   71,   72,   75,   76,   77,  112,   24,   37,   79,   44,
   44,  114,   12,   44,   94,   44,   37,   14,   24,   38,
   11,  119,   59,   60,  102,   24,   24,   39,   38,   24,
   40,   24,   41,   24,   42,   24,   43,   62,   16,   40,
   20,   41,   10,   42,  109,   43,   70,   21,   16,   59,
   60,   59,   60,    3,   22,    4,  116,   34,   13,   54,
   55,   61,   56,   23,   62,   53,   62,   17,   81,   82,
   17,   53,   83,   84,   85,   86,   87,   88,   15,   24,
    4,   53,   60,   60,   60,   60,   60,   60,   60,   60,
   60,   60,   60,   60,   91,   92,   60,   29,   60,   52,
   52,   81,   82,   52,   52,   52,   52,   52,   52,   52,
   52,   25,   31,   52,   30,   52,   51,   51,   36,   57,
   51,   51,   51,   51,   51,   51,   51,   51,   58,   69,
   51,   68,   51,   43,   43,   12,   80,   43,   13,   43,
   14,   55,   95,   78,   96,   97,   98,  100,   99,  117,
  101,  103,  107,  108,  110,  111,  113,  115,  118,    2,
   14,   19,   10,   12,   66,   65,   33,   26,  104,   28,
  105,  106,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         35,
   54,   55,   56,   57,   58,  101,  257,  257,   62,  269,
  270,  107,  269,  273,   68,  275,  257,  274,  269,  269,
  257,  117,  257,  258,   78,  276,  277,  277,  269,  280,
  280,  282,  282,  284,  284,  286,  286,  272,   13,  280,
  258,  282,  257,  284,   98,  286,  281,  257,   23,  257,
  258,  257,  258,  283,  273,  285,  110,   32,  273,  271,
  272,  269,  274,  270,  272,  101,  272,  270,  259,  260,
  273,  107,  263,  264,  265,  266,  267,  268,  283,  275,
  285,  117,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  270,  261,  262,  273,  269,  275,  259,
  260,  259,  260,  263,  264,  265,  266,  267,  268,  269,
  270,  274,  276,  273,  275,  275,  259,  260,  257,  272,
  263,  264,  265,  266,  267,  268,  269,  270,  272,  269,
  273,  272,  275,  269,  270,  269,  269,  273,  272,  275,
  274,  272,  272,  274,  269,  273,  270,  273,  275,  287,
  273,  273,  273,  273,  271,  269,  275,  269,  269,    0,
  273,  276,    5,  273,  273,  273,   32,   22,   89,   23,
   90,   93,
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
"fun_declaration : type_specifier ID LPAREN params RPAREN compound_stmt",
"params : param_list",
"params : VOID",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_specifier ID",
"param : type_specifier ID LBRACKET RBRACKET",
"$$2 :",
"compound_stmt : $$2 LCBRACKET local_declarations statement_list RCBRACKET",
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
"print_stmt : PRINT LPAREN expression RPAREN SEMI",
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

//#line 240 "cminus.y"

/* reference to the lexer object */
private static Yylex lexer;

/* The symbol table */
public final SymTab<SymTabRec> symtab = new SymTab<SymTabRec>();

/* To check if main has been encountered and is the last declaration */
private boolean seenMain = false;

/* To take care of nuance associated with params and decls in compound stsmt */
private boolean firstTime = true;

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
    System.err.println("Semantic Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol());
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

//#line 403 "Parser.java"
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
					/* TODO generate code prologue*/
                }
break;
case 2:
//#line 23 "cminus.y"
{
                	if (usesRead) GenCode.genReadMethod();
                	/* TODO generate class constructor code*/
                	/* TODO generate epilog*/
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
						else
						{
						/*Symbol table add*/
						SymTabRec rec = new VarRec(name, scope, vartype);
						symtab.insert(name, rec); 
						}
					}
break;
case 8:
//#line 57 "cminus.y"
{
						int vartype = val_peek(5).ival;
						String name = val_peek(4).sval;
						int scope = symtab.getScope();
						int arrayLength = val_peek(2).ival;

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of array " + name + " in the current scope");
						}
						else
						{
						/*Symbol table add*/
						SymTabRec rec = new ArrRec(name, scope, vartype,arrayLength);
						symtab.insert(name, rec); 
						}
					}
break;
case 9:
//#line 76 "cminus.y"
{ yyval = val_peek(0); }
break;
case 10:
//#line 77 "cminus.y"
{ yyval = val_peek(0); }
break;
case 11:
//#line 81 "cminus.y"
{
						

						int funtype = val_peek(5).ival;
						String name = val_peek(4).sval;
						int scope = symtab.getScope();

						/*List<SymTabRec> params = $4.ival;*/

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of function " + name + " in the current scope");
						}
						else
						{
							List<SymTabRec> params = (List<SymTabRec>)val_peek(2).obj;
							FunRec rec = new FunRec(name, scope, funtype, null);
							symtab.insert(name,rec);
						}
					}
break;
case 12:
//#line 103 "cminus.y"
{ yyval = val_peek(0); }
break;
case 13:
//#line 104 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 14:
//#line 105 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 15:
//#line 109 "cminus.y"
{
						/*List<SymTabRec> param_list = $1;*/

					}
break;
case 16:
//#line 114 "cminus.y"
{
						List<SymTabRec> param_list =  new ArrayList<SymTabRec>();
						param_list.add((SymTabRec)val_peek(0).obj);
						yyval = new ParserVal(param_list);
					}
break;
case 17:
//#line 122 "cminus.y"
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
case 19:
//#line 140 "cminus.y"
{
						symtab.enterScope();
					}
break;
case 20:
//#line 145 "cminus.y"
{
						symtab.exitScope();
					}
break;
//#line 696 "Parser.java"
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
