package code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class yufa_analysis {
	
    private String[][] SLRTable = SLR.SLR;
    private Stack<String> stateStack = new Stack<String>();   //状态栈
    private Stack<String> charStack = new Stack<String>();  //符号栈
    private Stack<String> yuanyu = new Stack<String>();
    private ArrayList<String> yuan = new ArrayList<String>();

    
    public void analyze(ArrayList tokens,ArrayList yuju) {
    	yuan=(ArrayList<String>) tokens.clone();
        GUI.tokenList.append("\n***************进行语法分析！***************\n");
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
        int index = 0; //记录索引
        String input = "";
        boolean flag = false;
        int step = 1;  //步骤
        GUI.tokenList.append("分析步骤如下：\n");
        while (!flag) {
            int topState = Integer.valueOf(stateStack.peek()).intValue();
            String temp = SLRTable[topState][getIndex(tokens.get(index).toString())];
            if (temp.startsWith("S")) { 
            	input = "";
                for (Object key : tokens) {
                    input += key.toString()+" ";
                }
                GUI.tokenList.append("步骤"+(step++) + "  \t状态栈：" + stateStr.toString() + "  \t符号栈:" + charStr + "  \t输入串:" + input + "  \tACTION:" + temp +"  移进\n");
                //移进
                String[] split = temp.split("S");
             //   System.out.println(split[0]+" "+split[1]);
                stateStack.push(split[1]);
                stateStr.add(split[1]);
                charStack.push(tokens.get(index).toString() + "");  //移进该字符
                yuanyu.push(yuju.get(index).toString() + "");
                charStr.add(tokens.get(index).toString() + "");
                //输入串要减少
                tokens.remove(index);yuju.remove(index);
              //  GUI.tokenList.append("  \t状态栈：" + stateStr.toString()+"\n");
            } else if (temp.startsWith("r")) {
            	String old_stateStr=stateStr.toString();
            	String old_charStr=charStr.toString();
                String[] split = temp.split("r");
                String[] css = SLR.grammar.get(Integer.valueOf(split[1])).split("->");
                String num = split[1];
                int len = getlength(num);
                String VT = css[0];
                //出栈
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
                GUI.tokenList.append("步骤"+ (step++) + "  \t状态栈：" + old_stateStr.toString() + "  \t符号栈:" + old_charStr.toString() + "  \t输入串:" + input +"  \tACTION:" + temp + "  \tGOTO:" + newState + "  规约\n");
            } else if (temp.equals("acc")) {
                flag = true;
                GUI.tokenList.append("步骤"+ (step++) + "  \t状态栈：" + stateStr.toString() + "  \t符号栈:" + charStr.toString() + "  \t输入串:" + input +"  \tACTION:" + "acc" + "  接受\n");
            } else {
            	GUI.tokenList.append("步骤"+ (step++) + "  \t状态栈：" + stateStr.toString() + "  \t符号栈:" + charStr.toString() +"\n");
                GUI.tokenList.append("输入的文本不符合语法!\n");
                getError(yuan);
              break;
            }
        }
        yuyi_analysis.Init_arr();
        yuyi_analysis.arr=arr;
        GUI.tokenList.append("***************语法分析结束!***************\n");
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
    		if(k==a.indexOf("id")) GUI.tokenList.append("相邻赋值语句缺少分号，或者数字与字母相连\n");
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
    			GUI.tokenList.append("缺少"+(end-begin)+"个begin\n");
    		else
    			GUI.tokenList.append("缺少"+(begin-end)+"个end\n");
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
    			GUI.tokenList.append("缺少"+(end-begin)+"个if\n");
    		else
    			GUI.tokenList.append("缺少"+(begin-end)+"个then\n");
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
    			GUI.tokenList.append("缺少"+(end-begin)+"个(\n");
    		else
    			GUI.tokenList.append("缺少"+(begin-end)+"个)\n");
    	}
    	if(a.contains("-"))
    	{
    		GUI.tokenList.append("可能是负号前面有id\n");
    		
    	}else if(a.contains(":="))
		GUI.tokenList.append("可能是相邻赋值语句缺少分号\n");
    	return "";
    }
    public String getErr(int t) {
        switch (t) {
            case 3://r3
                return "id或者)";
            case 5://r5
                return "id或者)或者end";
            case 7://r1
                return "id或者)或者end";
            case 8://r5
                return "id或者)或者end";
            case 13://r13
                return ";或者正确的id或者end";
            case 17://r19
                return "true";
            case 18://r20
                return "false";
            case 20://r2
                return "end";
            case 21://r7
                return "id或者)或者end";
            case 22://r6
                return "id或者)或者end";
            case 23://r4
                return "then";
            case 29://r11
                return "id或者)";
            case 33://r16
                return "id或者)";
            case 34://r8
                return "id或者)";
            case 35://r14
                return "id或者)";
            case 36://r15
                return "id或者)";
            case 37://r9
                return "id或者)";
            case 38://r10
                return "id或者)";
            case 39://r18
                return "id或者)";
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

