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
    5,    7,    7,    7,    9,    9,   10,   10,    8,   11,
   11,   12,   12,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   20,   14,   14,   15,   16,   18,   19,   17,
   17,   22,   22,   24,   24,   24,   24,   24,   24,   23,
   23,   25,   25,   26,   26,   27,   27,   28,   28,   28,
   28,   28,   21,   29,   29,   30,   30,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    1,    1,    3,    6,    1,    1,
    6,    1,    1,    0,    3,    1,    2,    4,    4,    2,
    0,    2,    0,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    4,    7,    7,    5,    5,    6,    2,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    1,    1,    1,    3,    1,    1,    1,    3,    1,    4,
    1,    1,    4,    1,    0,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   10,    9,    0,    4,    5,    6,    0,    3,
    0,    7,    0,    0,    0,    0,    0,    0,   16,    0,
    0,    0,    0,    0,    0,   21,   11,   15,    8,   18,
    0,   20,    0,    0,    0,    0,   32,   19,    0,    0,
    0,    0,   25,   22,   24,   26,   27,   28,   29,   30,
   31,    0,    0,    0,    0,    0,    0,    0,   62,   40,
    0,   61,    0,    0,    0,   55,    0,   33,    0,    0,
   67,    0,    0,    0,    0,    0,    0,    0,   41,   52,
   53,   45,   46,   44,   47,   48,   49,    0,    0,   56,
   57,    0,    0,    0,   34,   63,    0,    0,    0,    0,
    0,   58,    0,    0,   54,    0,    0,   66,    0,   38,
   37,   60,    0,   39,    0,    0,   35,   36,
};
final static short yydgoto[] = {                          1,
    5,    2,    6,    7,    8,    9,   17,   43,   18,   19,
   31,   34,   44,   45,   46,   47,   48,   49,   50,   51,
   62,   63,   64,   88,   89,   65,   92,   66,   72,   73,
};
final static short yysindex[] = {                         0,
    0, -265,    0,    0, -265,    0,    0,    0, -247,    0,
 -122,    0, -234, -227,    0, -194, -208, -195,    0, -199,
 -178, -198, -265, -171, -173,    0,    0,    0,    0,    0,
 -265,    0, -154, -250, -257, -203,    0,    0, -160, -157,
 -202, -153,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -156, -244, -177, -177, -177, -177, -213,    0,    0,
 -177,    0, -149, -125, -219,    0, -177,    0, -143, -139,
    0, -141, -134, -138, -129, -120, -177, -119,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -177, -177,    0,
    0, -177, -118, -117,    0,    0, -177, -123, -112, -222,
 -116,    0, -207, -219,    0, -222, -111,    0, -177,    0,
    0,    0, -127,    0, -108, -222,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  162,    0,    0,    0,    0,    0,
    0,    0, -110,    0, -235,    0,    0, -109,    0,    0,
 -262,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -236,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -107,    0,    0,    0, -176,    0,    0,
    0,    0,    0, -196, -159,    0,    0,    0,    0,    0,
    0,    0, -106,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -124, -142,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  160,  137,    0,   -7,    0,  147,    0,  148,
    0,    0,  -77,    0,    0,    0,    0,    0,    0,    0,
  -34,  -52,   82,    0,    0,   83,    0,   81,    0,    0,
};
final static int YYTABLESIZE=173;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
   70,   71,   74,   75,   76,   16,   36,   17,   78,   11,
   17,   12,   58,   59,   93,   16,   14,    3,   37,    4,
   23,   10,  111,   33,  101,   26,   38,   61,  113,   39,
   20,   40,   23,   41,   36,   42,   69,   13,  118,   23,
   23,   90,   91,   23,  108,   23,   37,   23,   15,   23,
    4,   80,   81,   26,   58,   59,  115,   39,   54,   40,
   77,   41,   21,   42,   22,   52,   60,   53,   54,   61,
   55,   52,   43,   43,   23,   24,   43,   26,   43,   58,
   59,   52,   59,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   61,   25,   59,   29,   59,   51,
   51,   30,   35,   51,   51,   51,   51,   51,   51,   51,
   51,   56,   68,   51,   57,   51,   50,   50,   67,   79,
   50,   50,   50,   50,   50,   50,   50,   50,   94,   95,
   50,   96,   50,   80,   81,   97,   98,   82,   83,   84,
   85,   86,   87,   99,   42,   42,   12,  109,   42,   13,
   42,   14,  100,  102,  106,  107,  110,  114,  112,  116,
  117,    2,   14,   12,   10,   65,   64,   32,   27,  103,
   28,  104,  105,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         34,
   53,   54,   55,   56,   57,   13,  257,  270,   61,  257,
  273,  269,  257,  258,   67,   23,  274,  283,  269,  285,
  257,  257,  100,   31,   77,  276,  277,  272,  106,  280,
  258,  282,  269,  284,  257,  286,  281,  273,  116,  276,
  277,  261,  262,  280,   97,  282,  269,  284,  283,  286,
  285,  259,  260,  276,  257,  258,  109,  280,  272,  282,
  274,  284,  257,  286,  273,  100,  269,  271,  272,  272,
  274,  106,  269,  270,  270,  275,  273,  276,  275,  257,
  258,  116,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  270,  272,  274,  273,  269,  275,  259,
  260,  275,  257,  263,  264,  265,  266,  267,  268,  269,
  270,  272,  269,  273,  272,  275,  259,  260,  272,  269,
  263,  264,  265,  266,  267,  268,  269,  270,  272,  269,
  273,  273,  275,  259,  260,  270,  275,  263,  264,  265,
  266,  267,  268,  273,  269,  270,  269,  271,  273,  272,
  275,  274,  273,  273,  273,  273,  269,  269,  275,  287,
  269,    0,  273,  273,    5,  273,  273,   31,   22,   88,
   23,   89,   92,
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
"compound_stmt : LCBRACKET local_declarations statement_list RCBRACKET",
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

//#line 164 "cminus.y"

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

//#line 400 "Parser.java"
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
					/* TODO generate code prologue*/
					symtab.enterScope();
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

						/*Symbol table add*/
						SymTabRec rec = new VarRec(name, scope, vartype);
						symtab.insert(name, rec); 
					}
break;
case 9:
//#line 52 "cminus.y"
{ yyval = val_peek(0); }
break;
case 10:
//#line 53 "cminus.y"
{ yyval = val_peek(0); }
break;
case 12:
//#line 59 "cminus.y"
{ yyval = val_peek(0); }
break;
//#line 590 "Parser.java"
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
