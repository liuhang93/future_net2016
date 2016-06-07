#include <iostream>
#include <algorithm>
#include <cstring>
#include <climits>
#include "route.h"
#include "ap.h"

using namespace std;

int apSum;

const int INF = 200000;

int s[verMax][verMax];//权值
//int lx[verMax], ly[verMax]; //顶标
//int linky[verMax];//记录与i匹配的顶点
bool visx[verMax], visy[verMax];
int slack[verMax];//松弛量

void init()
{
	memset(&root, 0, sizeof(judgeNode));
	memset(root.linky, -1, sizeof(root.linky)); //记录与i匹配的顶点
	//memset(root.ly, 0, sizeof(root.ly)); ///初始化顶标y为0

	for (int i = 0; i < n; i++)
	{
		root.lx[i] = -INF;

		for (int j = 0; j < n; j++)
		{
			if (s[i][j] > root.lx[i])
			{
				root.lx[i] = s[i][j];    ///初始化顶标x为与顶点Xi关联的边的最大权
			}
		}
	}
}

bool find(int x)//匈牙利算法
{
	visx[x] = true;
	int ni=node[x].outDeg + ((node[x].state & demandIndex) == 0);
	for (int i = 0; i < ni; i++)
	{
		int y = node[x].t[i];

		if (visy[y])
		{
			continue;
		}

		int t = root.lx[x] + root.ly[y] - s[x][y];//若t==0，则为最大权匹配；

		if (t == 0)
		{
			visy[y] = true;

			if (root.linky[y] == -1 || find(root.linky[y]))
			{
				root.linky[y] = x;
				return true;        //找到增广轨
			}
		}

		else
			if (slack[y] > t)
			{
				slack[y] = t;
			}
	}

	return false;                   //没有找到增广轨（说明顶点x没有对应的匹配，与完备匹配(相等子图的完备匹配)不符）
}

void KM()                //返回最优匹配的值
{
	init();

	for (int i = 0; i < n; i++)
		if ((node[i].state & demandIndex) == 0)
		{
			visx[i] = true;
			visy[i] = true;
			root.linky[i] = i;
		}

	for (int m = 0; m < inSet; m++)
	{
		int x = vertexDemand[demandIndex - 1][m];

		for (int i = 0; i < n; i++)
		{
			slack[i] = INF;    //松弛函数初始化为无穷大
		}

		while (1)
		{
			memset(visx, 0, sizeof(visx));
			memset(visy, 0, sizeof(visy));

			if (find(x))                    //找到增广轨，退出
			{
				break;
			}

			int d = INF;

			for (int i = 0; i < n; i++)         //没找到，对l做调整(这会增加相等子图的边)，重新找
			{
				if (!visy[i] && d > slack[i])
				{
					d = slack[i];
				}
			}

			for (int i = 0; i < n; i++) //修改x的顶标
			{
				if (visx[i])
				{
					root.lx[i] -= d;
				}
			}

			for (int i = 0; i < n; i++) //修改y的顶标
			{
				if (visy[i])
				{
					root.ly[i] += d;
				}
				else
				{
					slack[i] -= d;    //修改顶标后，不在交错树中的y顶点的slack值都要减去d；
				}
			}
		}

	}

	int result = 0;

	for (int i = 0; i < n; i++)
	{
		if (root.linky[i] > -1)
		{
			result += addWeigh + 100 - s[root.linky[i]][i];
		}
	}

	root.apSum=result;
}

void AP()
{
	memset(s, 0, sizeof(s));
	int visit[verMax] = {0};

	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
		{
			if (dis[i][j] != 0)
			{
				s[i][j] = addWeigh + 100 - dis[i][j];
			}
			else
				if (i != j || node[i].state & demandIndex)
				{
					s[i][j] = addWeigh + 100-INF;
				}
				else
				{
					s[i][j] = addWeigh + 100;
				}
		}

	s[vertexDemand[0][1]][vertexDemand[0][0]] = addWeigh + 100;
	KM();
}

