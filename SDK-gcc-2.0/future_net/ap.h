#ifndef AP_H_INCLUDED
#define AP_H_INCLUDED

#define forbMax 50
#define mustMax 500

struct judgeNode
{
	int lx[verMax];
	int ly[verMax];
	short int linky[verMax];
	short int forb[forbMax][2];
	short int must[mustMax][2];
	int forbNum;
	int mustNum;
	int apSum;
	int ringNum;
	/*friend bool operator<(judgeNode a, judgeNode b)   //自定义优先级，key小的优先
	{
		return (a.apSum!=b.apSum)?(a.apSum > b.apSum):(a.ringNum>b.ringNum);
		//return a.apSum*a.ringNum>b.apSum*b.ringNum;
	}*/
};

extern judgeNode root;

struct PCmp
{
    bool operator () (judgeNode const *a, judgeNode const *b)
    {
    	if(root.ringNum>10)
			return a->apSum*a->ringNum>b->apSum*b->ringNum;
        return (a->apSum!=b->apSum)?(a->apSum > b->apSum):(a->ringNum>b->ringNum);
    }
};


extern judgeNode *n0;
extern judgeNode best[2];
extern int s[verMax][verMax];//权值

extern int dis[verMax][verMax];
extern int inSet;
extern int demandIndex;

void AP();
int findMinPath(int d);
void KM1() ;

#endif // AP_H_INCLUDED
