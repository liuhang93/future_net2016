#ifndef __ROUTE_H__
#define __ROUTE_H__

#include "lib_io.h"

#ifdef _DEBUG
#define PRINT   printf
#else
#define PRINT(...)
#endif

#define edgeMax  40000
#define verMax   2002
#define inSetMax 102
#define outMax   22
#define addWeigh  1000
#define MAX      1000000

struct Node
{
    int state;				//1:路径1必经节点; 2: 路径2必经节点
    int f[verMax];
    int t[outMax];
    int inDeg;
    int outDeg;
};

//extern int edgeNum;
extern int verNum;
extern int inSetNum[2];
extern int n;
extern int repeat;

extern struct Node node[verMax];

extern int  vertexID[verMax];
//extern int  vertexID0[verMax];
extern int  vertexDemand[2][verMax];
extern int  edgeWeight[2][verMax][verMax];
extern int  edgeID[2][verMax][verMax];

void search_route(char *graph[MAX_EDGE_NUM], int edge_num, char *condition[MAX_DEMAND_NUM], int demand_num);
void readTopo(char *topo[MAX_EDGE_NUM], int edge_num);
void readDemand(char *demand[MAX_DEMAND_NUM],int d);
void makeGraph(char *topo[MAX_EDGE_NUM], int edge_num, char *demand[MAX_DEMAND_NUM]);
int getTime(int i);

int findMinPath(int d,int timeLimit);
void initDis();
#endif
