package Search;

import model.*;


/**
 * Created by CaiNot on 2018/3/20.
 */
public class TabuSearch {

    private Vex vex = null, vex_outside_tabu = null, vex_in_tabu = null;
    private int k = 0;
    private Graph graph = null;
    private int best_f = 0, color = 0;
    private int f = 0, delt = 10000, delt_tabu = 10000, best_delt = 100;
    private int color_after_in_tabu = 0, color_after_outside_tabu = 0;
    private int best_count_in_tabu = 0, best_count_outside_tabu = 0;

    int count_xx = 0;

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
    public void findMove(int step) {
        Vex vex = null;
        this.delt = 10000;
        this.delt_tabu = 10000;

        int color_before = 0;


        int tmp = 0;
        count_xx = 0; //记录被禁忌的节点数

        for (int x = 0; x < this.graph.vexes.length; x++) {
            vex = this.graph.vexes[x];
            if (vex.calcEnemyValue() == 0) {
                count_xx++;
                continue;
            }
            color_before = vex.getColor();
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

                            count_xx++;

                            if (1 > (int) (Math.random() * best_count_in_tabu)) {
                                vex_in_tabu = vex;
                                color_after_in_tabu = i;
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
//                                count_xx++;
//                                System.out.println(String.valueOf(count_xx));
                            }
                        }
                    }
                }
            }
        }



        if ((delt_tabu < best_f - f) && (delt_tabu < delt)) {
            delt = delt_tabu;
            this.vex = this.vex_in_tabu;
            this.color = this.color_after_in_tabu;

            System.out.println("AA" + String.valueOf(count_xx));

        } else {
            this.vex = this.vex_outside_tabu;
            this.color = this.color_after_outside_tabu;

//            System.out.println("BB"+String.valueOf(count_xx));
        }

        if(count_xx==this.graph.vexes.length){
            delt = delt_tabu;
            this.vex = this.vex_in_tabu;
            this.color = this.color_after_in_tabu;

            System.out.println("BB" + String.valueOf(count_xx));
        }

    }

    public void makeMove(int step) {
        f = delt + f;
        if (delt < best_delt)
            best_delt = delt;
        if (f < best_f) {
            best_f = f;
        }
        this.vex.move(this.vex.getColor(), this.color, step);

    }


    public Graph getGraph() {
        return graph;
    }


}
