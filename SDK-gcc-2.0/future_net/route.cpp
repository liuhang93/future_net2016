#include "route.h"
#include "ap.h"
#include "lib_record.h"
#include <stdio.h>
#include <iostream>
#include <algorithm>
using namespace std;
int n;

int route[2][verMax];
int routeNum[2];

int repeat;

int repeatNum()
{
	int repeat=0;
	for(int i=0;i<n;i++)
	{
		if(best[0].linky[i]!=i&&best[0].linky[i]==best[1].linky[i]
			&&edgeID[1][best[0].linky[i]][i]==edgeID[0][best[0].linky[i]][i])
			repeat++;
	}
	return repeat-1;
}

void saveRoute()
{
    int sum1=0,sum2=0;
    int pos=vertexDemand[0][1];
    do{
		int from=best[0].linky[pos];
		route[0][routeNum[0]++]=edgeID[0][from][pos];
		sum1+=edgeWeight[0][from][pos];
		pos=from;
    }
    while(pos!=vertexDemand[0][0]);

    pos=vertexDemand[1][1];
    do{
		int from=best[1].linky[pos];
		if(best[0].linky[pos]!=from)
		{
			route[1][routeNum[1]++]=edgeID[0][from][pos];
			sum2+=edgeWeight[0][from][pos];
		}
		else
		{
			route[1][routeNum[1]++]=edgeID[1][from][pos];
			sum2+=edgeWeight[1][from][pos];
		}
		pos=from;
    }
    while(pos!=vertexDemand[1][0]);

    for(int i=routeNum[0]-1;i>=0;i--)
		record_result(WORK_PATH,route[0][i]);
	for(int i=routeNum[1]-1;i>=0;i--)
		record_result(BACK_PATH,route[1][i]);
	sum2-=repeat*addWeigh;
	printf("sum1=%d\n",sum1);
	printf("sum2=%d\n",sum2);
	printf("sumTotal=%d\n",sum1+sum2);
}

//你要完成的功能总入口
void search_route(char *topo[MAX_EDGE_NUM], int edge_num, char *demand[MAX_DEMAND_NUM], int demand_num)
{
	getTime(0);
    makeGraph(topo,edge_num,demand);
    //printf("time0=%d\n",getTime(2));
    n=verNum;
	int d=1;
	int isLB[2]={0,0};
    isLB[d-1]=findMinPath(d,4000);
    //printf("time1=%d\n",getTime(2));
	isLB[3-d-1]=findMinPath(3-d,8000);
	repeat=repeatNum();
	int deltaTime=getTime(2);
	//printf("time2=%d\n",deltaTime);
	while(1)
	{
		int nowTime=getTime(2);
		if(nowTime+deltaTime>8000)
			break;
		if(repeat==0&&isLB[0]==-1&&isLB[1]==-1)
			break;
		isLB[d-1]=findMinPath(d,8000);
		repeat=repeatNum();
		if(isLB[d-1]==0)
			break;
		d=3-d;
	}
	saveRoute();
	printf("repeat:%d\n",repeat);
	printf("time:%dms\n",getTime(2));
}
