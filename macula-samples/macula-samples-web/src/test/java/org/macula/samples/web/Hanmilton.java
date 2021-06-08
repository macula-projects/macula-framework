/*
 * Copyright 2004-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.macula.samples.web;

import java.util.Scanner;

/**
 * <p>
 * <b>Hanmilton</b> 汉密尔顿最短路径求解
 * </p>
 *
 * 1 2 4
 * 1 3 6
 * 1 4 10
 * 1 5 5
 * 2 3 9
 * 2 4 8
 * 2 5 8
 * 3 4 2
 * 3 5 11
 * 4 5 20
 *
 * @author Rain
 * @since 2021-02-21
 */
public class Hanmilton {

    int[][] data;     //邻接矩阵 到sa 拉黑圣诞节，
    int[] isvisited; //访问数组
    int nVertex;         //顶点数
    int nEdge;         //边数
    int[] best_path;  //汉密尔顿最佳路径

    //构造函数
    public Hanmilton(int nv, int ne, int[][] data) {
        this.nVertex = nv;
        this.nEdge = ne;
        this.data = data;
        this.best_path = new int[nv + 1];
        // 初始化访问数组
        this.isvisited = new int[nv + 1];
        for (int i = 0; i < nv + 1; i++) {
            best_path[i] = 0;
            this.isvisited[i] = 0;
        }
    }

    //求汉密尔顿回路函数
    public int solve() {
        int path[] = new int[1000];
        int cur_vertex = 0;     // 作为保存当前结点
        int length = 0;         // 汉密尔顿回路长度
        int min = 10000;        // 最小长度


        // 对每个顶点为初始点进行比遍历寻找汉密尔顿回路
        for (int i = 1; i < this.nVertex + 1; i++) {
            // 重新设置最端长度为0
            length = 0;

            // 重新初始化访问数组为0
            for (int aa = 0; aa < this.nVertex + 1; aa ++) {
                this.isvisited[aa] = 0;
            }

            // 标记当前结点为已访问
            this.isvisited[i] = 1;
            // 保存到临时路径数组的第一个
            path[1] = i;
            // 保存当前顶点
            cur_vertex = i;

            // 访问剩余的结点
            for (int j = 2; j < this.nVertex + 1; j++) {

                int k = 0;
                // 寻找到第一个未访问的结点
                for (k = 1; k < this.nVertex + 1; k++) {
                    if (this.isvisited[k] == 0) {
                        break;
                    }
                }

                // 保存当前顶点到该结点的路径长度
                int tmp = this.data[cur_vertex][k];
                // 向后寻找有没有路径更短的节点
                for (int m = k + 1; m < this.nVertex + 1; m++) {
                    if ((this.isvisited[m] == 0) && this.data[cur_vertex][m] > 0 && (tmp > this.data[cur_vertex][m])) {
                        // 更新当前最短路径
                        tmp = this.data[cur_vertex][m];
                        // 更新第一个未被访问的结点
                        k = m;
                    }
                }
                // 保存路径上的结点
                path[j] = k;
                // 标记为已访问
                this.isvisited[k] = 1;
                // 跟新当前结点
                cur_vertex = k;
                // 跟新长度
                length += tmp;
                // 当前长度大于最小长度，则该路径无效，跳出循环
                if (length > min) {
                    break;
                }
            }
            length = length + this.data[cur_vertex][i];
            // 更新最小长度并保存最佳路径
            if (min > length) {
                min = length;
                for (int m = 0; m < this.nVertex + 1; m++) {
                    this.best_path[m] = path[m];
                }
            }
        }
        //返回最小长度
        return min;
    }

    // 打印最佳汉密尔顿回路
    public void Print_Best_Path() {
        System.out.println("Best Path: " + this.best_path[1]);
        for (int i = 2; i < this.nVertex + 1; i++) {
            System.out.print(" -> " + this.best_path[i]);
        }
        System.out.print(" -> " + this.best_path[1]);
    }

    // 打印邻接矩阵
    public void Print() {
        for (int i = 0; i < this.nVertex + 1; i++) {
            for (int j = 0; j < this.nVertex + 1; j++) {
                System.out.print(String.format("%3d", this.data[i][j]));
            }
            System.out.print('\n');
        }
    }

    public static void main(String args[]) {

        int data[][] = {
            {0, 0, 0, 0, 0, 0},
            {0, 0, 4, 6, 10, 5},
            {0, 4, 0, 9, 8, 8},
            {0, 6, 9, 0, 2, 11},
            {0, 10, 8, 2, 0, 20},
            {0, 5, 8, 11, 20, 0}
        };
        Hanmilton graph = new Hanmilton(5, 10, data);

        System.out.println("邻接矩阵为：");
        graph.Print();

        int length = graph.solve();
        System.out.println("该图的汉密尔顿回路长度为：" + length);

        System.out.println("汉密尔顿回路路径为：");
        graph.Print_Best_Path();
    }
}
