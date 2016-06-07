#include <iostream>
#include <algorithm>
#include <cstring>
#include <climits>
#include "route.h"
#include "ap.h"

using namespace std;

int apSum1;
const int INF = 200000;

int w[verMax][verMax];//权值
//int lx1[verMax]={1098,1099,1099,1100,1100,1099}, ly1[verMax]={0,1,0,0,0,1}; //顶标
//int linky1[verMax]={3,-1,1,2,4,5};//记录与i匹配的顶点
bool visx1[verMax], visy1[verMax];
int slack1[verMax];//松弛量
//int forb[verMax][2]={{0,1}};
//int must[verMax][2]={{2,3}};

void init1()
{
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
		{
			w[i][j]=s[i][j];
		}

	/*for (int i = 0; i < verNum; i++)
	{
		visx1[i] = 1;
		visy1[i] = 1;
	}*/

	for (int i = 0; i < n0->forbNum; i++)
	{
		w[n0->forb[i][0]][n0->forb[i][1]] -= INF;
		//visx1[n0->forb[i][0]]=0;
		//visy1[n0->forb[i][1]]=0;
	}

	for (int i = 0; i < n0->mustNum; i++)
	{
		for (int j = 0; j < verNum; j++)
		{
			if (j != n0->must[i][1])
			{
				w[n0->must[i][0]][j] -= INF;
			}
		}
	}
}

bool find1(int x) //匈牙利算法
{
	visx1[x] = true;
	int ni = node[x].outDeg + ((node[x].state & demandIndex) == 0);

	for (int i = 0; i < ni; i++) //node[x].outDeg+((node[x].state&demandIndex)==0)
	{
		int y = node[x].t[i]; //node[x].t[i];

		if (visy1[y])
		{
			continue;
		}

		int t = n0->lx[x] + n0->ly[y] - w[x][y];//若t==0，则为最大权匹配；

		if (t == 0)
		{
			visy1[y] = true;

			if (n0->linky[y] == -1 || find1(n0->linky[y]))
			{
				n0->linky[y] = x;
				return true;        //找到增广轨
			}
		}

		else
			if (slack1[y] > t)
			{
				slack1[y] = t;
			}
	}

	return false;                   //没有找到增广轨（说明顶点x没有对应的匹配，与完备匹配(相等子图的完备匹配)不符）
}

void KM1()                //返回最优匹配的值
{
	init1();
	int x = n0->forb[n0->forbNum - 1][0];
	n0->linky[n0->forb[n0->forbNum - 1][1]]=-1;

	for (int i = 0; i < verNum; i++)
	{
		slack1[i] = INF;    //松弛函数初始化为无穷大
	}

	while (1)
	{
		memset(visx1, 0, sizeof(visx1));
		memset(visy1, 0, sizeof(visy1));

		if (find1(x))                   //找到增广轨，退出
		{
			break;
		}

		int d = INF;

		for (int i = 0; i < verNum; i++)         //没找到，对l做调整(这会增加相等子图的边)，重新找
		{
			if (!visy1[i] && d > slack1[i])
			{
				d = slack1[i];
			}
		}

		for (int i = 0; i < verNum; i++) //修改x的顶标
		{
			if (visx1[i])
			{
				n0->lx[i] -= d;
			}
		}

		for (int i = 0; i < verNum; i++) //修改y的顶标
		{
			if (visy1[i])
			{
				n0->ly[i] += d;
			}
			else
			{
				slack1[i] -= d;    //修改顶标后，不在交错树中的y顶点的slack值都要减去d；
			}
		}
	}

	int result = 0;

	for (int i = 0; i < n; i++)
	{
		if (n0->linky[i] > -1)
		{
			result += addWeigh + 100 - w[n0->linky[i]][i];
		}
	}

	n0->apSum=result;
}

