int w;
int mainVar;

void testFun(int y, int z[]){
	int x;
	int xa[5];
	/*x = 9123;
	y = 1235;
	w = x + y;*/
	xa[0] = 222;
	xa[1] = 345;
	x = xa[0];
	return;
}

void main(void){
	testFun();
	mainVar = 1234;
	if(mainVar < 3234){
		mainVar = 23;
	}
}