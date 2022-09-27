package code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class yufa_analysis {
	
    private String[][] SLRTable = SLR.SLR;
    private Stack<String> stateStack = new Stack<String>();   //״̬ջ
    private Stack<String> charStack = new Stack<String>();  //����ջ
    private Stack<String> yuanyu = new Stack<String>();
    private ArrayList<String> yuan = new ArrayList<String>();

    
    public void analyze(ArrayList tokens,ArrayList yuju) {
    	yuan=(ArrayList<String>) tokens.clone();
        GUI.tokenList.append("\n***************�����﷨������***************\n");
        stateStack.removeAll(stateStack);
        charStack.removeAll(charStack);
        yuanyu.removeAll(yuanyu);
        yuyi_analysis.gui.removeAll(yuyi_analysis.gui);
    	int zb=1;int j=0;String[][] arr = new String[200][5];
        List<String> stateStr = new ArrayList<String>();
        List<String> charStr = new ArrayList<String>();
        stateStack.push("0");
        stateStr.add("0");
        charStack.push("#");yuanyu.push("#");
        charStr.add("#");
        int index = 0; //��¼����
        String input = "";
        boolean flag = false;
        int step = 1;  //����
        GUI.tokenList.append("�����������£�\n");
        while (!flag) {
            int topState = Integer.valueOf(stateStack.peek()).intValue();
            String temp = SLRTable[topState][getIndex(tokens.get(index).toString())];
            if (temp.startsWith("S")) { 
            	input = "";
                for (Object key : tokens) {
                    input += key.toString()+" ";
                }
                GUI.tokenList.append("����"+(step++) + "  \t״̬ջ��" + stateStr.toString() + "  \t����ջ:" + charStr + "  \t���봮:" + input + "  \tACTION:" + temp +"  �ƽ�\n");
                //�ƽ�
                String[] split = temp.split("S");
             //   System.out.println(split[0]+" "+split[1]);
                stateStack.push(split[1]);
                stateStr.add(split[1]);
                charStack.push(tokens.get(index).toString() + "");  //�ƽ����ַ�
                yuanyu.push(yuju.get(index).toString() + "");
                charStr.add(tokens.get(index).toString() + "");
                //���봮Ҫ����
                tokens.remove(index);yuju.remove(index);
              //  GUI.tokenList.append("  \t״̬ջ��" + stateStr.toString()+"\n");
            } else if (temp.startsWith("r")) {
            	String old_stateStr=stateStr.toString();
            	String old_charStr=charStr.toString();
                String[] split = temp.split("r");
                String[] css = SLR.grammar.get(Integer.valueOf(split[1])).split("->");
                String num = split[1];
                int len = getlength(num);
                String VT = css[0];
                //��ջ
                String gui="";String yue="";
                for (int i = 0; i < len; i++) {
                	if(len>1) {
                		gui+=charStack.peek();
                		yue=yuanyu.peek();
                		arr[j][i]=yue;
                		yuanyu.pop();
                	}
                    stateStack.pop();
                    stateStr.remove(stateStr.size() - 1);
                    charStack.pop();
                    charStr.remove(charStr.size() - 1);
                }
                if(len>1) {
                	j++;
                	yuyi_analysis.gui.add(gui);
                	yuanyu.push("t");
            	}
                int newTopState = Integer.valueOf(stateStack.peek()).intValue();
                String newState = SLRTable[newTopState][getIndex(VT)];
                stateStack.push(newState);
                stateStr.add(newState);
                
                charStack.push(VT);
                charStr.add(VT);
                input = "";
                for (Object key : tokens) 
                    input += key.toString()+" ";
                GUI.tokenList.append("����"+ (step++) + "  \t״̬ջ��" + old_stateStr.toString() + "  \t����ջ:" + old_charStr.toString() + "  \t���봮:" + input +"  \tACTION:" + temp + "  \tGOTO:" + newState + "  ��Լ\n");
            } else if (temp.equals("acc")) {
                flag = true;
                GUI.tokenList.append("����"+ (step++) + "  \t״̬ջ��" + stateStr.toString() + "  \t����ջ:" + charStr.toString() + "  \t���봮:" + input +"  \tACTION:" + "acc" + "  ����\n");
            } else {
            	GUI.tokenList.append("����"+ (step++) + "  \t״̬ջ��" + stateStr.toString() + "  \t����ջ:" + charStr.toString() +"\n");
                GUI.tokenList.append("������ı��������﷨!\n");
                getError(yuan);
              break;
            }
        }
        yuyi_analysis.Init_arr();
        yuyi_analysis.arr=arr;
        GUI.tokenList.append("***************�﷨��������!***************\n");
    }
    
    public int getlength(String num) {
    	if (num.equals("0") || num.equals("3") || num.equals("5") || num.equals("13") || num.equals("19") || num.equals("20"))
            return 1;
        else if (num.equals("1") || num.equals("6") || num.equals("11") || num.equals("16") || num.equals("7"))
            return 2;
        else if (num.equals("2") || num.equals("4") || num.equals("8") || num.equals("9") || num.equals("10") || num.equals("12") || num.equals("14") || num.equals("15") || num.equals("17") || num.equals("18"))
            return 3;
		return 0;
    }
    public String getError(ArrayList<String> a) {
    	int begin=0,end=0,k=-1;    	
    	//id
    	while(a.contains("id")) {
    		if(k==a.indexOf("id")) GUI.tokenList.append("���ڸ�ֵ���ȱ�ٷֺţ�������������ĸ����\n");
    		else k=a.indexOf("id");
    		a.remove(k);
    		
    	}
    	
    	//begin end
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains("begin"));
    	begin=k-a.size();
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains("end"));
    	end=k-a.size();
    	if(begin!=end) {
    		if(begin<end)
    			GUI.tokenList.append("ȱ��"+(end-begin)+"��begin\n");
    		else
    			GUI.tokenList.append("ȱ��"+(begin-end)+"��end\n");
    	}
    	//if then
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains("if"));
    	begin=k-a.size();
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains("then"));
    	end=k-a.size();
    	if(begin!=end) {
    		if(begin<end)
    			GUI.tokenList.append("ȱ��"+(end-begin)+"��if\n");
    		else
    			GUI.tokenList.append("ȱ��"+(begin-end)+"��then\n");
    	}
    	//( )
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains("("));
    	begin=k-a.size();
    	k=a.size();
    	a.removeIf(e -> ((String) e).contains(")"));
    	end=k-a.size();
    	if(begin!=end) {
    		if(begin<end)
    			GUI.tokenList.append("ȱ��"+(end-begin)+"��(\n");
    		else
    			GUI.tokenList.append("ȱ��"+(begin-end)+"��)\n");
    	}
    	if(a.contains("-"))
    	{
    		GUI.tokenList.append("�����Ǹ���ǰ����id\n");
    		
    	}else if(a.contains(":="))
		GUI.tokenList.append("���������ڸ�ֵ���ȱ�ٷֺ�\n");
    	return "";
    }
    public String getErr(int t) {
        switch (t) {
            case 3://r3
                return "id����)";
            case 5://r5
                return "id����)����end";
            case 7://r1
                return "id����)����end";
            case 8://r5
                return "id����)����end";
            case 13://r13
                return ";������ȷ��id����end";
            case 17://r19
                return "true";
            case 18://r20
                return "false";
            case 20://r2
                return "end";
            case 21://r7
                return "id����)����end";
            case 22://r6
                return "id����)����end";
            case 23://r4
                return "then";
            case 29://r11
                return "id����)";
            case 33://r16
                return "id����)";
            case 34://r8
                return "id����)";
            case 35://r14
                return "id����)";
            case 36://r15
                return "id����)";
            case 37://r9
                return "id����)";
            case 38://r10
                return "id����)";
            case 39://r18
                return "id����)";
            case 41://r17
                return ")";
            case 42://r12
                return ")";
        }
        return "";
    }

    public int getIndex(String t) {
        switch (t) {
            case "begin":
                return 0;
            case "end":
                return 1;
            case "if":
                return 2;
            case "then":
                return 3;
            case ";":
                return 4;
            case "id":
                return 5;
            case ":=":
                return 6;
            case "+":
                return 7;
            case "*":
                return 8;
            case "-":
                return 9;
            case "(":
                return 10;
            case ")":
                return 11;
            case "or":
                return 12;
            case "and":
                return 13;
            case "not":
                return 14;
            case "rop":
                return 15;
            case "true":
                return 16;
            case "false":
                return 17;
            case "#":
                return 18;
            case "S":
                return 19;
            case "C":
                return 20;
            case "A":
                return 21;
            case "L":
                return 22;
            case "K":
                return 23;
            case "B":
                return 24;
            case "E":
                return 25;
        }
        return -1;
    }
  
}

