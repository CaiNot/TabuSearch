import Search.TabuSearch;
import model.FileOperation;
import model.Vex;

/**
 * Created by CaiNot on 2018/3/20.
 */
public class GraphColor {

    public static void main(String[] args) {
//        Scanner scanner=new Scanner(System.in);

        final String PATH = "D:/instances/instances/DSJC125.1.col";

        FileOperation fileOperation = new FileOperation();
        int[][] graph = fileOperation.readFile(PATH);
        if (graph == null) {
            System.out.println("Error file read error");
            System.exit(1);
        }

        TabuSearch search = new TabuSearch();
        int k = 39;
        int loop = 0;
        Vex[] vexes = null;
        int position = 0;

        while (k > 0) {
            search.init(graph, k);
            loop = 0;
//            position = 0;
//            vexes = search.getGraph().vexes;

//            for(int i=0;i<search.getGraph().vexes.length;i++){
//                System.out.print(String.valueOf(search.getGraph().vexes[i].getColor())+"\t"+String.valueOf(i));
//                Vex[] vexes1=search.getGraph().vexes[i].vexes_near;
//                for(int x=0;x<vexes1.length;x++){
//                    System.out.print("\t");
//                    System.out.print(vexes1[x].getVex());
//                }
//                System.out.println();
//
//            }
//            System.exit(0);

            while (search.getGraph().calcEnemyValue() != 0) {
//                if (vexes[position].calcEnemyValue() != 0) {
                loop++;

                if (loop > 300000) {
                    System.out.println("Color is " + String.valueOf(k+1));
                    System.exit(1);
                }
//                for (int i = 0; i < vexes.length; i++) {
                search.findMove(loop);
                search.makeMove(loop);
//                }
            }
            System.out.println("Color is " + String.valueOf(k));
            k--;
        }

    }
}
