package code;

import java.util.ArrayList;
import java.util.Stack;

public class yuyi_analysis {
	public static ArrayList<String> gui = new ArrayList<>();
	public static String[][] arr=new String[200][5];
	public static ArrayList<String> siyuanshi = new ArrayList<>();
	public static ArrayList<String> yuju = new ArrayList<>();
	public static void run() {
		siyuanshi.removeAll(siyuanshi);
		System.out.println(gui);
		String str="";
		boolean flag=true;
		int len=gui.size();
		int t=1,j=0,i=1,jz=0;
		boolean jm=false;
	    Stack<Integer> jmp = new Stack<Integer>();
	    Stack<Boolean> back = new Stack<Boolean>();
		while(flag) {
			if(j>=len) break;
			switch(gui.get(j)) {
				case"EropE":
					if(arr[j][0]=="t") arr[j][0]="t"+(t-1);if(arr[j][2]=="t") arr[j][2]="t"+(t-1);//t>a变成t1>a
					siyuanshi.add((i++)+":("+arr[j][1]+","+arr[j][2]+","+arr[j][0]+",");
					back.push(false);//表示尚未回填
					break;
				case"E+E":
					if(arr[j][0]=="t") arr[j][0]="t"+(t-1);if(arr[j][2]=="t") arr[j][2]="t"+(t-1);arr[j][3]="t"+(t++);//a+b=t->t1
					siyuanshi.add((i++)+":("+arr[j][1]+","+arr[j][2]+","+arr[j][0]+","+arr[j][3]+")");
					break;
				case"E*E":
					if(arr[j][0]=="t") arr[j][0]="t"+(t-1);if(arr[j][2]=="t") arr[j][2]="t"+(t-1);arr[j][3]="t"+(t++);//a*b=t->t1
					siyuanshi.add((i++)+":("+arr[j][1]+","+arr[j][2]+","+arr[j][0]+","+arr[j][3]+")");
					break;
				case"E:=id":
					if (arr[j][0].charAt(0)=='t') {//将上一条四元式的末尾t回填
						t--;
						str=siyuanshi.get(i-2);
						str=siyuanshi.get(i-2).substring(0,str.lastIndexOf(',')+1)+arr[j][2]+")";
						siyuanshi.set(i-2,str);
					}
					else {//上一条四元式不需要回填
						siyuanshi.add((i++)+":(:=,"+arr[j][0]+",_,"+arr[j][2]+")");
					}
					break;
				case"E-":
					if(arr[j][0]=="t") arr[j][0]="t"+(t-1);arr[j][3]="t"+(t++);//t->t1
					siyuanshi.add((i++)+":(-,0,"+arr[j][0]+","+arr[j][3]+")");
					break;
				case"BorB":
					if(i-3>=0) {//两个B都要回填
						str=siyuanshi.get(i-3)+(i+1)+")";
						siyuanshi.set(i-3,str);
						str=siyuanshi.get(i-2)+(i+1)+")";
						siyuanshi.set(i-2,str);
					}else if(i-2>=0){//一个B需要回填
						if(arr[j][0].length()==4||arr[j][2].length()==4)
						{
							str=siyuanshi.get(i-2)+i+")";
							siyuanshi.set(i-2,str);
							jm=true;
						}
						else {
							str=siyuanshi.get(i-2)+(i+1)+")";
							siyuanshi.set(i-2,str);
						}
					}else if(i-1>=0) {//不需要回填
						if(arr[j][0].length()==4||arr[j][2].length()==4)//如果有true跳过
							jm=true;
					}
					back.push(true);
					break;
				case"BandB":
					if(i-3>=0) {//两个B都要回填
						str=siyuanshi.get(i-3)+i+")";
						siyuanshi.set(i-3,str);
						str=siyuanshi.get(i-2)+(i+2)+")";
						str=i+str.substring(1);
						siyuanshi.set(i-2,str);
						str=(i-1)+":(j,_,_,"+(i+1)+")";
						siyuanshi.add(i-2,str);
						i++;
						back.push(true);
					}else if(i-2>=0){//一个B需要回填
						if(arr[j][0].length()==5||arr[j][2].length()==5)
						{
							siyuanshi.remove(i-2);
							back.push(true);
							i--;
						}
					}else if(i-1>=0) {//不需要回填
						if(arr[j][0].length()==4&&arr[j][2].length()==4)//如果两个B都为true跳过
							jm=true;
					}
					break;
				case"thenBif":
					if(jm) {//是否跳过
						jm=false;
						break;
					}
					if(!back.empty()) {//是否需要回填
						if(!back.pop()&&arr[j][1]=="t") {
							str=siyuanshi.get(i-2)+(i+1)+")";
							siyuanshi.set(i-2,str);
						}
					}
					if(arr[j][1].length()==4) {//B=true
						back.push(true);
					}else if(j>0&&arr[j-1][1]=="not"&&(arr[j-1][0]=="t"||arr[j-1][0].length()==5)) {//!t和!false
					}
					else {
						siyuanshi.add((i++)+":(j,_,_,");
						jmp.push(i-2);
					}
					break;
				case"endLbegin":
					if(!jmp.empty() ) {//是否需要回填
						jz=jmp.pop();
						str=siyuanshi.get(jz)+i+")";
						siyuanshi.set(jz,str);
					}
					break;
				case";L":
					while(!jmp.empty()&&jump()) {//是否需要回填
						jz=jmp.pop();
						str=siyuanshi.get(jz)+i+")";
						siyuanshi.set(jz,str);
					}
					break;
				case"Bnot":
					arr[j][1]="not";
					if(!back.empty()) {//是否需要回填
						if(!back.pop()) {
							jmp.push(i-2);
						}
					}
					break;
				case")B(":
					if(arr[j][1].length()==4&&arr[j+1][0]=="t") arr[j+1][0]="true";
					if(arr[j][1].length()==5&&arr[j+1][0]=="t") arr[j+1][0]="false"; 
			}
			j++;
		}	
		while(!jmp.empty() ) {
			jz=jmp.pop();
			str=siyuanshi.get(jz)+i+")";
			siyuanshi.set(jz,str);
		}
		GUI.tokenList.append("\n***********进行语义分析************\n");
		GUI.tokenList.append("语义分析的四元式中间代码如下：\n");
		/*	for (String st : siyuanshi) {
				GUI.tokenList.append(st+"\n");
	        }*/
			for (String st : siyuanshi) {
				if(st.indexOf(')')!=-1) {
					String str2=st.substring(0, st.indexOf(')')+1);
					GUI.tokenList.append(str2+"\n");
				}
	        }
		GUI.tokenList.append("***********语义分析结束************\n\n");
			System.out.println(gui);
			for(i=0;i<29;i++) {
				for(j=0;j<5;j++)
					System.out.print(arr[i][j]);
				System.out.println();
			}
		/*	for (String st : siyuanshi) {
				if(st.indexOf(')')!=-1) {
					String str2=st.substring(0, st.indexOf(')')+1);
					GUI.tokenList.append(str2+"\n");
				}
	        }*/
			
		}
	
	
	private static boolean jump() {//then后面没有begin，就返回true
		if(yuju.contains("then")&&yuju.contains("begin"))
		{
			for(int i=0;i<yuju.size();i++)
			{
				if(yuju.get(i)=="then"&&yuju.get(i+1)=="begin")
					return false;
			}
		}
		return true;
	}

	public static void Init_arr() {
		for(int i=0;i<50;i++) {
			for(int j=0;j<5;j++)
				arr[i][j]=null;
		}
	}
}
