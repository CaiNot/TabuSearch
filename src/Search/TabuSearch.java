package Search;

import model.*;

import java.util.ArrayList;

/**
 * Created by CaiNot on 2018/3/20.
 */
public class TabuSearch {

    private Vex vex = null;
    private int k = 0;


    private Graph graph = null;
    private Vex[] vexes_tabu = null;
    private int best_f = 0;
    private int f = 0, delt = 10000, delt_tabu = 10000;

    /**
     * @param graph_matrix 矩阵图
     * @param k            颜色数
     * @return 0正常 其余错误
     * @description 将矩阵的图存为邻接表的形式，并为每一个结点涂色
     * 创建仇人表
     */
    public int init(int[][] graph_matrix, int k) {
        if (graph_matrix == null) {
            return 1;
        }
        this.k = k;
//        this.vex_enemy = new Vex[graph_matrix.length];
        this.graph = new Graph(graph_matrix.length);

        for (int i = 0; i < graph_matrix.length; i++) {// 初始化所有节点

            this.vex = new Vex(i, (int) (Math.random() * this.k), new int[k]);
            int length = 0;

            for (int x = 0; x < graph_matrix.length; x++) {
                if (graph_matrix[i][x] != 0) {
                    length++;
                }
            }
            this.vex.initVexNear(length);
            this.graph.addVex(this.vex);
        }

        for (int i = 0; i < graph_matrix.length; i++) {
            int position = 0;

            for (int x = 0; x < graph_matrix.length; x++) {
                if (graph_matrix[i][x] != 0) {
                    this.graph.vexes[i].addEdge(this.graph.vexes[x], position);
                    position++;
                }
            }
        }
        f = this.graph.calcEnemyValue();
        best_f = f;
        return 0;
    }

    /**
     * need to be 修改：禁忌移动的移动条件需要修改。
     * 找到使当前冲突减少最多的节点
     * 如果禁忌了，
     *
     * @param step
     * @return
     */
    public int findMove(int step) {
        Vex vex = null, vex_outside_tabu = null,vex_in_tabu=null;

        int color_before = 0;
        int color_after_in_tabu = 0, color_after_outside_tabu = 0;
        int best_count_in_tabu = 0, best_count_outside_tabu = 0;
        int neightbor_size = 0;
        int tmp = 0;

        for (int x = 0; x < this.graph.vexes.length; x++) {
            vex = this.graph.vexes[x];
            if (vex.calcEnemyValue() == 0) {
                continue;
            }

            color_before = vex.getColor();
            neightbor_size = vex.calcNeighborValue();

            for (int i = 0; i < this.k; i++) {
                if (i != color_before) {
                    tmp = vex.ifMove(color_before, i);
                    if (vex.isTabu(step, i)) {//如果该行为被禁忌
                        if (tmp <= delt_tabu) {
                            if (tmp < delt_tabu) {
                                delt_tabu = tmp;
                                best_count_in_tabu = 0;
                            }
                            best_count_in_tabu++;
                            if (1 > (int) (Math.random() * best_count_in_tabu)) {
                                vex_in_tabu = vex;
                                color_after_outside_tabu = i;
                            }
                        }
                    } else {
                        if (tmp <= delt) {
                            if (tmp < delt) {
                                best_count_outside_tabu = 0;
                                delt = tmp;
                            }
                            best_count_outside_tabu++;
                            if (1 > (int) (Math.random() * best_count_outside_tabu)) {//等概率交换
                                color_after_outside_tabu = i;
                                vex_outside_tabu = vex;
                            }
                        }
                    }
                }
            }

        }

    }

    public int move_tabu(int step) {
        this.vexes_tabu = new Vex[this.graph.vexes.length];
        Vex v = null;
        int best_value = 0, cur_value = 0;
        for (int i = 0; i < this.graph.vexes.length; i++) {
            v = this.graph.vexes[i];
            if (v.isTabu(step))
                vexes_tabu[i] = v;
        }
    }

    public Graph getGraph() {
        return graph;
    }


}
