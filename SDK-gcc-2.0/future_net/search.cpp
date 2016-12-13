#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include "route.h"
#include "lib_record.h"
#include "ap.h"

using namespace std;

int dis[verMax][verMax];
int mark[verMax][verMax];
int inSet;
int demandIndex;
int sum0;
int minSum = 4500000;
int addWeigh0=0;
int lastAdd=0;
judgeNode root;
judgeNode * n0;
judgeNode best[2];

void initDis()
{
	memset(dis, 0, sizeof(dis));
	sum0 = 0;

	for (int i = 0; i < verNum; i++)
	{
		for (int j = 0; j < node[i].outDeg; j++)
		{
			int to = node[i].t[j];
			dis[i][to] = edgeWeight[0][i][to];
		}
	}

	for (int i = 0; i < verNum && best[2 - demandIndex].apSum != 0; i++)
	{
		int tmp = best[2 - demandIndex].linky[i];

		if (tmp != i && tmp >= 0)
		{
			sum0 += edgeWeight[0][tmp][i];
			dis[tmp][i] = edgeWeight[1][tmp][i];
			if(dis[tmp][i]>addWeigh)
				dis[tmp][i]=dis[tmp][i]-addWeigh+addWeigh0;
		}
	}

	if (best[2 - demandIndex].apSum == 0)
	{
		sum0 = minSum/2;
	}
}

void printRoute(judgeNode * n1, vector<vector<int>> & routeAll)
{
	/*for (int i = 0; i < routeAll.size(); i++)
	{
		for (int j = 0; j < routeAll[i].size(); j++)
		{
			cout << routeAll[i][j] << " ";
		}

		cout << endl;
	}

	cout << "apSum:" << n1->apSum << endl;*/
}

int getRoute(judgeNode * n1, vector<vector<int>> & routeAll, int * index)
{
	routeAll.clear();
	int ringNum = 0;
	int visit[verMax] = {0};
	int minNum = n;

	for (int m = 0; m < inSet; m++)
	{
		int i = vertexDemand[demandIndex - 1][m];

		if (visit[i] == 1)
		{
			continue;
		}

		vector<int> route0;
		int num0 = 0;
		visit[i] = 1;
		route0.push_back(i);
		//cout << i << "->";//vertexID0[i]
		int j = n1->linky[i];

		while (j >= 0)
		{
			//cout << j << "->";//vertexID0[j]
			route0.push_back(j);
			num0++;

			if (visit[j] == 1)
			{
				break;
			}

			visit[j] = 1;
			j = n1->linky[j];
		}

		routeAll.push_back(route0);

		if (num0 < minNum)
		{
			minNum = num0;
			*index = ringNum;
		}

		ringNum++;
		//cout <<endl;
	}

	//printRoute(n1, routeAll);
	//cout << "index:" << *index << endl;
	//cout<<ringNum<<endl;
	return ringNum;
}



int findMinPath(int d, int timeLimit)
{
	priority_queue<judgeNode*,vector<judgeNode*>,PCmp> q;
	demandIndex = d;
	inSet = inSetNum[d - 1];
	if(getTime(2)>1500)
		addWeigh0+=100;
	initDis();
	vector<vector<int>> routeAll;

	int index;
	int minLen = minSum - sum0+repeat*(addWeigh0-lastAdd); //上界
	lastAdd=addWeigh0;
	addWeigh0=addWeigh0*2+1;
	int bestTime = MAX;
	int refresh = 0; //更新
	//cout<<"ub:"<<minLen<<endl;
	AP();
	root.ringNum = getRoute(&root, routeAll, &index);

	if (root.ringNum == 1)
	{
		if (root.apSum < minLen)
		{
			minLen = root.apSum;
			best[demandIndex - 1] = root;
			minSum = sum0 + minLen;
			return -1;
		}

		//cout<<demandIndex<<"best:"<<minLen<<endl;
		//cout<<"bestTotal:"<<sum0+minLen<<endl;
		return 0;
	}
	//cout<<root.ringNum <<endl;
	root.forbNum++;

	for (int i = 1; i < routeAll[index].size(); i++)
	{
		root.forb[root.forbNum - 1][0] = routeAll[index][i];
		root.forb[root.forbNum - 1][1] = routeAll[index][i - 1];

		if (i > 1)
		{
			root.must[root.mustNum][0] = routeAll[index][i - 1];
			root.must[root.mustNum][1] = routeAll[index][i - 2];
			root.mustNum++;
		}
		judgeNode* tmp=(judgeNode*)malloc(sizeof(judgeNode));
		*tmp=root;
		q.push(tmp);
	}

	while (!q.empty())
	{
		judgeNode *tmp=q.top();
		judgeNode nd = *tmp;
		free(tmp);
		q.pop();
		if (nd.apSum >= minLen)
		{
			break;
		}
		//cout<<demandIndex<<"   "<<nd.apSum<<"   "<<nd.ringNum<<"   ";
		int lastRing=nd.ringNum;
		n0 = &nd;
		KM1();
		if (nd.apSum >= minLen)
		{
			continue;
		}

		nd.ringNum = getRoute(n0, routeAll, &index);
		//cout<<nd.apSum<<"   "<<nd.ringNum<<endl;
		//if(nd.ringNum>lastRing)
		//	continue;
		int nowTime = getTime(2);

		if (refresh == 1 && (nowTime > timeLimit || nowTime - bestTime > sqrt(verNum) ))
		{
			break;
		}
		if(best[demandIndex - 1].ringNum==1&&nowTime > timeLimit)
			break;

		if (nd.ringNum == 1)
		{
			if (nd.apSum < minLen)
			{
				minLen = nd.apSum;
				best[demandIndex - 1] = nd;
				bestTime = getTime(2);
				refresh = 1;
				//return 1;
			}

			continue;
			//break;
		}

		/*if(q.size()>8000)
		{
			continue;
		}*/

		nd.forbNum++;

		for (int i = 1; i < routeAll[index].size(); i++)
		{
			nd.forb[nd.forbNum - 1][0] = routeAll[index][i];
			nd.forb[nd.forbNum - 1][1] = routeAll[index][i - 1];

			if (i > 1)
			{
				nd.must[nd.mustNum][0] = routeAll[index][i - 1];
				nd.must[nd.mustNum][1] = routeAll[index][i - 2];
				nd.mustNum++;
			}
			judgeNode* tmp=(judgeNode*)malloc(sizeof(judgeNode));
			*tmp=nd;
			q.push(tmp);
		}

		if(q.size()>4000)
		{
			cout<<q.size()<<"  "<<nd.ringNum<<endl;
			//continue;
			//if (refresh == 1) break;
			//cout<<q.size();
			priority_queue<judgeNode*,vector<judgeNode*>,PCmp> q1;
			for(int i=0;i<3000;i++)
			{
				judgeNode* n1=q.top();
				q.pop();
				q1.push(n1);
			}
			while(!q.empty())
			{
				judgeNode* n1=q.top();
				q.pop();
				free(n1);
			}
			q.swap(q1);
			//cout<<q.size();
		}
	}

	minSum = sum0 + minLen;
	return refresh;
	//cout<<demandIndex<<"best:"<<minLen<<endl;
	//cout<<"bestTotal:"<<sum0+minLen<<endl;
}


