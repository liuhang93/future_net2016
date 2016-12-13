#include "route.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include<iostream>
using namespace std;

//int edgeNum;    				//边数
int verNum;     				//顶点数
int inSetNum[2];				//必经节点数

struct Node node[verMax];

int  vertexID[verMax];			//节点重新编号
//int  vertexID0[verMax];         //原始编号
int  vertexDemand[2][verMax];	//必经节点集合
int  edgeWeight[2][verMax][verMax];//边的权重,记录两组
int  edgeID[2][verMax][verMax];

void makeGraph(char *topo[MAX_EDGE_NUM], int edge_num, char *demand[MAX_DEMAND_NUM])
{
    readTopo(topo,edge_num);
    //printf("time0=%d\n",getTime(2));
    readDemand(demand,1);
    //printf("time1=%d\n",getTime(2));
    readDemand(demand,2);
    //printf("time2=%d\n",getTime(2));
    node[vertexDemand[0][1]].outDeg=0;
    node[vertexDemand[0][0]].inDeg=0;
	node[vertexDemand[0][1]].t[node[vertexDemand[0][1]].outDeg++]=vertexDemand[0][0];
	node[vertexDemand[0][0]].f[node[vertexDemand[0][0]].inDeg++]=vertexDemand[0][1];
}

void readTopo(char *topo[MAX_EDGE_NUM], int edge_num)
{
	//edgeNum=edge_num;
	int id,f,t,w;
	int num[4]={0};
	int ID=0;
	fill(vertexID,vertexID+verMax,-1);
    for(int i=0;i<edge_num;i++)
    {
    	//sscanf(topo[i],"%d,%d,%d,%d",&id,&f,&t,&w);
    	int j=0,numi=0,tmp=0;
    	while(numi<4)
		{
			char c=topo[i][j++];
			if(c>='0'&&c<='9')
				tmp=tmp*10+c-'0';
			else
			{
				num[numi++]=tmp;
				tmp=0;
			}
		}
		id=num[0];f=num[1];t=num[2];w=num[3];
        if(vertexID[f]==-1)
		{
			//vertexID0[ID]=f;
			vertexID[f]=ID++;
		}

		if(vertexID[t]==-1)
		{
			//vertexID0[ID]=t;
			vertexID[t]=ID++;
		}
		int id1=vertexID[f],id2=vertexID[t];
		if(edgeWeight[0][id1][id2]==0)
		{
			edgeWeight[0][id1][id2]=w;
			edgeID[0][id1][id2]=id;
			node[id1].t[node[id1].outDeg++]=id2;
			node[id2].f[node[id2].inDeg++]=id1;
			edgeWeight[1][id1][id2]=w+addWeigh;
			edgeID[1][id1][id2]=id;
		}
		else if(w<edgeWeight[1][id1][id2])
		{
			if(w<edgeWeight[0][id1][id2])
			{
				edgeWeight[1][id1][id2]=edgeWeight[0][id1][id2];
				edgeWeight[0][id1][id2]=w;
				edgeID[1][id1][id2]=edgeID[0][id1][id2];
				edgeID[0][id1][id2]=id;
			}
			else
			{
				edgeWeight[1][id1][id2]=w;
				edgeID[1][id1][id2]=id;
			}
		}
    }
    verNum=ID;
    for(int i=0;i<verNum;i++)
		node[i].t[node[i].outDeg]=i;
}

void readDemand(char *demand[MAX_DEMAND_NUM],int d)//解析第d条必经节点
{
	char *p;
	p=strtok(demand[d-1],",");
	p=strtok(NULL,",");
	int n=0;
	while(p)
	{
		if(*p=='N') break;
		int num=atoi(p);
		int id=vertexID[num];
		vertexDemand[d-1][n++]=id;
		node[id].state|=d;
		if(n<2)
			p=strtok(NULL,",");
		else
			p=strtok(NULL,"|");
	}
	inSetNum[d-1]=n;/*
	int j=0,n=-1,tmp=0;
	while(1)
	{
		char c=demand[d-1][j++];
		if(c>='0'&&c<='9')
			tmp=tmp*10+c-'0';
		else
		{
			if(n==-1) {
				tmp=0;n=0;continue;
			}
			if(c==' ') continue;
			if(c!=','&&c!='|'&&c!='\0'&&c!='\n') break;
            int id=vertexID[tmp];
            vertexDemand[d-1][n++]=id;
			node[id].state|=d;
			tmp=0;
			if(c!=','&&c!='|') break;
		}
	}
	inSetNum[d-1]=n;*/
}

int getTime(int i)
{
    static clock_t begin = clock();
    static clock_t last=begin;
    clock_t now=clock();
	int usedTime=(now-last)*1000/CLOCKS_PER_SEC;//返回距last的时间
	if(i==1)
		last=now;								//更新last时间
	if(i==2)
		return (now-begin)*1000/CLOCKS_PER_SEC; //返回总时间
    return usedTime;
}
