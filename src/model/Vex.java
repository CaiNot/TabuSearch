package model;

/**
 * Created by CaiNot on 2018/3/20.
 */
public class Vex {
    private int vex;
    private int step_to_move;
    private int color;
    private int last_color;
    private int score;

    /**
     * enemy[] 长度为颜色数
     */
    private int[] enemy = null;

    public Vex[] vexes_near = null;

    public Vex(int vex, int color, int[] enemy) {
        this.vex = vex;
        this.color = color;
        this.last_color = color;
        this.step_to_move = 0;
        this.enemy = enemy;
    }

    public void initVexNear(int length) {
        this.vexes_near = new Vex[length];
    }

    public void addEdge(Vex vex, int position) {
        vexes_near[position] = vex;
        enemy[vex.getColor()]++;
    }

    /**
     * @return
     * @description 获得邻域大小
     */
    public int getNeighborSize() {
        int result = 0;
        for (int i = 0; i < this.vexes_near.length; i++) {
            if (this.vexes_near[i].getColor() != this.color) {
                result++;
            }
        }
        return result;
    }

    public int getVex() {
        return vex;
    }

    public void setVex(int vex) {
        this.vex = vex;
    }

    public boolean isTabu(int now_step, int color) {
        if (this.step_to_move > now_step && color == this.last_color) {
            return true;
        } else {
            if (this.step_to_move <= now_step)
                this.step_to_move = 0;
            return false;
        }
    }

    public boolean isTabu(int now_step) {
        if (this.step_to_move > now_step) {
            return true;
        } else {
            this.step_to_move = 0;
            return false;
        }
    }

    public int ifMove(int color_before, int color_after) {
        int[] enemy_temp = null;
        int value = 0, value_before = 0;


        for (int i = 0; i < vexes_near.length; i++) {
            enemy_temp = vexes_near[i].getEnemy();

            value_before += vexes_near[i].calcEnemyValue();//在修改之前获得其仇恨值

            enemy_temp[color_before]--;
            enemy_temp[color_after]++;
            value += vexes_near[i].calcEnemyValue();
            enemy_temp[color_before]++;
            enemy_temp[color_after]--;
        }

        return value-value_before;
    }

    public void move(int color_before, int color_after, int step_now) {
        int[] enemy_temp = null;

        this.setColor(color_after);
        this.setTabu(step_now);

        for (int i = 0; i < vexes_near.length; i++) {
            enemy_temp = vexes_near[i].getEnemy();
            enemy_temp[color_before]--;
            enemy_temp[color_after]++;
        }
    }

    private void setTabu(int step_now) {
        step_now += this.getNeighborSize() + (int) (Math.random() * 10);
        this.step_to_move = step_now;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.last_color = this.color;
        this.color = color;
    }

    public int[] getEnemy() {
        return enemy;
    }

    public int getLastColor() {
        return last_color;
    }

    public void setLastColor(int last_color) {
        this.last_color = last_color;
    }

    /**
     * 获得与它同色的相邻节点的个数
     * @return
     */
    public int calcEnemyValue() {
//        this.enemy_score = value;
        this.score = this.enemy[this.color];
        return this.score;
    }

    public int calcNeighborValue() {
        int value = 0;
        for (int i = 0; i < this.vexes_near.length; i++) {
            value += this.vexes_near[i].calcEnemyValue();
        }
        return value;
    }
}
