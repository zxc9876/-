package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SLR {
    static List<String> grammar = new ArrayList<String>();
    private static HashMap<String, List<String>> follow = new HashMap<String, List<String>>();
    public static String[][] SLR = new String[43][26];
    private Stack<String> stateStack = new Stack<String>();   //状态栈
    private Stack<String> charStack = new Stack<String>();  //符号栈
    private Stack<String> yuanyu = new Stack<String>();

    //输出SLR表
    public static void show() {
    	GUI.textArea2.append("控制语句文法：\n");
    	int k=0;
        for (String s : grammar) {
            GUI.textArea2.append("["+(k++)+"]:"+s+"\n");
        }
        GUI.textArea2.append("SLR分析表如下:"+"\n");
        GUI.textArea2.append("  begin end if then id := + * - ( ) or and not rop true false # S C A L K B E"+"\n");
        for (int i = 0; i < 43; i++) {
            GUI.textArea2.append(String.valueOf(i));
            for (int j = 0; j < 26; j++) {
            	if(SLR[i][j]!=" ") 
            		GUI.textArea2.append(" " + SLR[i][j]);
            	else
                GUI.textArea2.append(" 空");
            }
            GUI.textArea2.append("\n");
        }
    }

    //初始化SLR表
    public static void Init() {
    	setGrammar();//创建控制语句文法
    	setFollow();//创建follow集
    	
        for (int i = 0; i < 43; i++) {
            for (int j = 0; j < 26; j++) {
                SLR[i][j] = " ";
            }
        }
        //创建SLR表
        SLR[0][0] = "S4";SLR[0][2] = "S5";SLR[0][5] = "S6";SLR[0][19] = "1";SLR[0][20] = "2";SLR[0][21] = "3";
        SLR[1][18] = "acc";
        SLR[2][0] = "S4";SLR[2][2]="S5";SLR[2][5]="S6";SLR[2][19]="7";SLR[2][20]="2";SLR[2][21]="3";
        SLR[3][1]="r3";SLR[3][4]="r3";SLR[3][18]="r3";
        SLR[4][0]="S4";SLR[4][2]="S5";SLR[4][5]="S6";SLR[4][19]="8";SLR[4][20]="2";SLR[4][21]="3";SLR[4][22]="9";SLR[4][23]="10";
        SLR[5][1]="r5";SLR[5][4]="r5";SLR[5][5]="S13";SLR[5][9]="S14";SLR[5][10]="S15";SLR[5][14]="S16";SLR[5][16]="S17";SLR[5][17]="S18";SLR[5][24]="11";SLR[5][25]="12";
        SLR[6][6]="S19";
        SLR[7][1]="r1";SLR[7][4]="r1";SLR[7][18]="r1";
        SLR[8][1]="r5";SLR[8][4]="r5";
        SLR[9][1]="S20";SLR[9][4]="S21";
        SLR[10][0]="S4";SLR[10][2]="S5";SLR[10][5]="S6";SLR[10][19]="22";SLR[10][20]="2";SLR[10][21]="3";
        SLR[11][3]="S23";SLR[11][12]="S24";SLR[11][13]="S25";
        SLR[12][7]="S26";SLR[12][8]="S27";SLR[12][15]="S28";
        SLR[13][1]="r13";SLR[13][3]="r13";SLR[13][4]="r13";SLR[13][7]="r13";SLR[13][8]="r13";SLR[13][11]="r13";SLR[13][12]="r13";SLR[13][13]="r13";SLR[13][15]="r13";SLR[13][18]="r13";
        SLR[14][5]="S13";SLR[14][9]="S14";SLR[14][10]="S30";SLR[14][25]="29";
        SLR[15][5]="S13";SLR[15][9]="S14";SLR[15][10]="S15";SLR[15][14]="S16";SLR[15][16]="S17";SLR[15][17]="S18";SLR[15][24]="31";SLR[15][25]="32";
        SLR[16][5]="S13";SLR[16][9]="S14";SLR[16][10]="S15";SLR[16][14]="S16";SLR[16][16]="S17";SLR[16][17]="S18";SLR[16][24]="33";SLR[16][25]="12";
        SLR[17][3]="r19";SLR[17][11]="r19";SLR[17][12]="r19";SLR[17][13]="r19";
        SLR[18][3]="r20";SLR[18][11]="r20";SLR[18][12]="r20";SLR[18][13]="r20";
        SLR[19][5]="S13";SLR[19][9]="S14";SLR[19][10]="S30";SLR[19][25]="34";
        SLR[20][1]="r2";SLR[20][4]="r2";SLR[20][18]="r2";
        SLR[21][0]="r7";SLR[21][2]="r7";SLR[21][5]="r7";
        SLR[22][1]="r6";SLR[22][4]="r6";
        SLR[23][0]="r4";SLR[23][2]="r4";SLR[23][5]="r4";
        SLR[24][5]="S13";SLR[24][9]="S14";SLR[24][10]="S15";SLR[24][14]="S16";SLR[24][16]="S17";SLR[24][17]="S18";SLR[24][24]="35";SLR[24][25]="12";
        SLR[25][5]="S13";SLR[25][9]="S14";SLR[25][10]="S15";SLR[25][14]="S16";SLR[25][16]="S17";SLR[25][17]="S18";SLR[25][24]="36";SLR[25][25]="12";
        SLR[26][5]="S13";SLR[26][9]="S14";SLR[26][10]="S30";SLR[26][25]="37";
        SLR[27][5]="S13";SLR[27][9]="S14";SLR[27][10]="S30";SLR[27][25]="38";
        SLR[28][5]="S13";SLR[28][9]="S14";SLR[28][10]="S30";SLR[28][25]="39";
        SLR[29][1]="r11";SLR[29][4]="r11";SLR[29][7]="r11";SLR[29][8]="r11";SLR[29][11]="r11";SLR[29][15]="r11";SLR[29][18]="r11";
        SLR[30][5]="S13";SLR[30][9]="S14";SLR[30][10]="S30";SLR[30][25]="40";
        SLR[31][11]="S41";SLR[31][12]="S24";SLR[31][13]="S25";
        SLR[32][7]="S26";SLR[32][8]="S27";SLR[32][11]="S42";SLR[32][15]="S28";
        SLR[33][3]="r16";SLR[33][11]="r16";SLR[33][12]="r16";SLR[33][13]="r16";
        SLR[34][1]="r8";SLR[34][4]="r8";SLR[34][7]="S26";SLR[34][8]="S27";SLR[34][18]="r8";
        SLR[35][3]="r14";SLR[35][11]="r14";SLR[35][12]="r14";SLR[35][13]="S25";
        SLR[36][3]="r15";SLR[36][11]="r15";SLR[36][12]="r15";SLR[36][13]="r15";
        SLR[37][1]="r9";SLR[37][4]="r9";SLR[37][7]="r9";SLR[37][8]="S27";SLR[37][11]="r9";SLR[37][15]="r9";SLR[37][18]="r9";
        SLR[38][1]="r10";SLR[38][4]="r10";SLR[38][7]="r10";SLR[38][8]="r10";SLR[38][11]="r10";SLR[38][15]="r10";SLR[38][18]="r10";
        SLR[39][3]="r18";SLR[39][7]="S26";SLR[39][8]="S27";SLR[39][11]="r18";SLR[39][12]="r18";SLR[39][13]="r18";
        SLR[40][7]="S26";SLR[40][8]="S27";SLR[40][11]="S42";
        SLR[41][3]="r17";SLR[41][11]="r17";SLR[41][12]="r17";SLR[41][13]="r17";
        SLR[42][1]="r12";SLR[42][4]="r12";SLR[42][7]="r12";SLR[42][8]="r12";SLR[42][11]="r12";SLR[42][15]="r12";SLR[42][18]="r12";
        SLR[42][3]="r12";
        SLR[38][3]="r10";
        SLR[37][3]="r9";

    }

    public static void setGrammar() {//创建控制语句文法表
    	grammar.add("S'->S");
        grammar.add("S->CS");
        grammar.add("S->beginLend");
        grammar.add("S->A");
        grammar.add("C->ifBthen");
        grammar.add("L->S");
        grammar.add("L->KS");
        grammar.add("K->L;");
        grammar.add("A->id:=E");
        grammar.add("E->E+E");
        grammar.add("E->E*E");
        grammar.add("E->-E");
        grammar.add("E->(E)");
        grammar.add("E->id");
        grammar.add("B->BorB");
        grammar.add("B->BandB");
        grammar.add("B->notB");
        grammar.add("B->(B)");
        grammar.add("B->EropE");
        grammar.add("B->true");
        grammar.add("B->false");

    }
    public static void setFollow() {//建立follow集
        List<String> S_Follow = new ArrayList<>();//S的follow集
        List<String> C_Follow = new ArrayList<>();//C的follow集
        List<String> A_Follow = new ArrayList<>();//A的follow集
        List<String> L_Follow = new ArrayList<>();//L的follow集
        List<String> K_Follow = new ArrayList<>();//K的follow集
        List<String> B_Follow = new ArrayList<>();//B的follow集
        List<String> E_Follow = new ArrayList<>();//E的follow集

        S_Follow.add("#");S_Follow.add("end");
        C_Follow.add("#");C_Follow.add("end");
        A_Follow.add("#");A_Follow.add("end");
        L_Follow.add("end");L_Follow.add(";");
        K_Follow.add("#");K_Follow.add("end");
        B_Follow.add("then");B_Follow.add("or");B_Follow.add("and");B_Follow.add(")");
        E_Follow.add("#");E_Follow.add("end");E_Follow.add("rop");E_Follow.add("then");E_Follow.add("or");E_Follow.add("and");E_Follow.add(")");E_Follow.add("+");E_Follow.add("*");

        follow.put("S", S_Follow);
        follow.put("C", C_Follow);
        follow.put("A", A_Follow);
        follow.put("L", L_Follow);
        follow.put("K", K_Follow);
        follow.put("B", B_Follow);
        follow.put("E", E_Follow);
    }
    
    
}
