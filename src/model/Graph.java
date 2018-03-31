package model;

/**
 * Created by CaiNot on 2018/3/20.
 */
public class Graph {

    public Vex[] vexes=null;
    public int[] tabu_vexes=null;

    public int best_value=10000;

    public Graph(int vexes_nums){
        vexes=new Vex[vexes_nums];
    }

    public void addVex(Vex vex){
        vexes[vex.getVex()]=vex;
    }

    public boolean hasVex(int vex){
        if(this.vexes[vex]==null)
            return false;
        else return true;
    }

    public int calcEnemyValue(){
        int value=0;
        for (int i=0;i<vexes.length;i++){
            value+=vexes[i].calcEnemyValue();
        }
        this.best_value=value;
        return value;
    }

}