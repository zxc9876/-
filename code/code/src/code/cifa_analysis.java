package code;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class cifa_analysis {
    //��ȡtxt�ļ�
    private static char[] Code;
    public static ArrayList<String> tokens = new ArrayList<>();
    public static ArrayList<String> yuanyu = new ArrayList<>();
    static int k=1;
    public static ArrayList<String> Run(String Filename) {
    	tokens.removeAll(tokens);
    	yuanyu.removeAll(yuanyu);
    	k=1;
        try {
            BufferedReader in = new BufferedReader(new FileReader(Filename));
            String temp = "";
            StringBuilder sb = new StringBuilder();
            while ((temp = in.readLine()) != null) {
                sb.append(temp).append("\n");
            }
            String str = sb.toString();
            Divide(str);
        } catch (IOException e) {
            e.printStackTrace();
            GUI.tokenList.append("���ļ�ʧ��!\n");
        }
        return tokens;
    }

    public static void Divide(String s) {
        if (Ignore(s)) {  //���������벻Ϊ��
            GUI.tokenList.append("\n**********************���дʷ�����******************\n");
            StartState(0, 0);
        }
    }

    public static boolean Ignore(String s) {//�ж�Դ�����Ƿ�Ϊ�գ�Ϊ�����ܽ��дʷ�����
        if (s.isEmpty() || s.toCharArray()[0] == '#') {
            GUI.tokenList.append("Դ����Ϊ�գ��޷����дʷ�������\n");
            return false;
        } else {
            Code = s.toCharArray();
            return true;
        }
    }

    public static void StartState(int i, int j) {   //i��ʼָ�룬j��ǰָ��
        if (j>=Code.length) {//#
            GUI.tokenList.append("");
            yuanyu.add("#");
            tokens.add("#");
        } else
        if (Code[j]==' ')//�ո�
            StartState(j + 1, j + 1);
        else if (Code[j]== '\n')//����
        	StartState(j + 1, j + 1);
        else if (Code[j]== '\t')//tab
        	StartState(j + 1, j + 1);
        else if (Code[j]== '+')//+
        {
        	GUI.tokenList.append((k++)+" �����������+\n");
            yuanyu.add("+");
            tokens.add("+");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]=='(')//(
        {
            GUI.tokenList.append((k++)+" ���޷���(\n");
            yuanyu.add("(");
            tokens.add("(");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]==')')//)
        {
        	GUI.tokenList.append((k++)+" ���޷���)\n");
            yuanyu.add(")");
            tokens.add(")");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]=='-')//-
        {
        	GUI.tokenList.append((k++)+" �����������-\n");
            yuanyu.add("-");
            tokens.add("-");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]=='*')//*
        {
        	GUI.tokenList.append((k++)+" �����������*\n");
            yuanyu.add("*");
            tokens.add("*");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]==';')//;
        {
        	GUI.tokenList.append((k++)+" ���߷���;\n");
            yuanyu.add(";");
            tokens.add(";");
            StartState(j + 1, j + 1);
        }
        else if (Code[j]=='!')//!not
        {
            if (Code[j+1] != '=') {
                GUI.tokenList.append((k++)+" ��ϵ�������!\n");
                yuanyu.add("!");
                tokens.add("not");
                StartState(i + 1, i + 1);
            } else if (Code[i] == '!' && Code[j+1] == '=') {
                GUI.tokenList.append((k++)+" ��ϵ�������!=\n");
                yuanyu.add("!=");
                tokens.add("rop");
                StartState(i + 2, i + 2);
            }
        }
        else if (Code[j]==':')//=
        {
            GUI.tokenList.append((k++)+" ���߷���:=\n");
            yuanyu.add(":=");
            tokens.add(":=");
            StartState(j + 2, j + 2);
        }
        else if (Code[j]=='=')//==
        {
        	if(Code[j+1]=='=') {
        		GUI.tokenList.append((k++)+" ��ϵ�������==\n");
                yuanyu.add("==");
                tokens.add("rop");
                StartState(j + 2, j + 2);
        	}

        }
        else if (Code[j]== '<' || Code[j] == '>')//< > <= >= !=
            RopState(i, j + 1);
        else if (Code[j]=='&')//&&
        {
            if (Code[j+1] == '&') {
                GUI.tokenList.append((k++)+" �߼��������&&\n");
                yuanyu.add("and");
                tokens.add("and");
                StartState(j+2 , j+2);}
                else {
                	GUI.tokenList.append((k++)+" �߼��������&\n");
                    yuanyu.add("and");
                    tokens.add("and");
                    StartState(j+1,j+1);
                }
        }
        else if (Code[j]=='|')//||
        {
            if (Code[j+1] == '|') {
                GUI.tokenList.append((k++)+" �߼��������||\n");
                yuanyu.add("or");
                tokens.add("or");
                StartState(j+2 , j+2);}
                else {
                	GUI.tokenList.append((k++)+" �߼��������|\n");
                    yuanyu.add("or");
                    tokens.add("or");
                    StartState(j+1,j+1);
                }
        }
        else if (Character.isDigit(Code[j])) {//����
            if (Character.isDigit(Code[j+1]))
                IntState(i, j + 2);
            else {
                GUI.tokenList.append((k++)+" ���ͳ�����");
                printID(i, j+1);
            }
        }else if (Character.isLetter(Code[j])) {//��ĸ
            switch (Code[j]) {
                case 'b'://begin
                    BeginState(i, j + 1);
                    break;
                case 'e'://end
                    EndState(i, j + 1);
                    break;
                case 'i'://if
                    IfState(i, j + 1);
                    break;
                case 't'://then��true
                    if (Code[j + 1] == 'h')
                        ThenState(i, j + 1);
                    else if (Code[j + 1] == 'r')
                        TrueState(i, j + 1);
                    break;
                case 'f'://false
                    FalseState(i, j + 1);
                    break;
                case 'o'://or
                    OrState(i, j + 1);
                    break;
                case 'a'://and
                    AndState(i, j + 1);
                    break;
                case 'n'://not
                    NotState(i, j + 1);
                    break;
                default:
                    IdentState(i, j + 1);
            }
        } else {
            GUI.tokenList.append("(����!!:" + Code[j] + ")\n");
            String tempSt = "" + Code[j];
            tokens.add(tempSt);
            StartState(i + 1, j + 1);
        }
        
    }

    //�ս��
    public static void IdentState(int i, int j) {
        if (Character.isDigit(Code[j]) || Character.isLetter(Code[j])||Code[j]=='_')
            IdentState(i, j + 1); //�����ǰ�ַ���ȻΪ��ĸ���������ٴν���IdentState
        else
        {
            GUI.tokenList.append((k++)+" �ս����");
            printID(i, j);
        }
    }

    //���ͳ���
    public static void IntState(int i, int j) {
        if (Character.isDigit(Code[j]))
            IntState(i, j + 1);
        else {
            GUI.tokenList.append((k++)+" ���ͳ�����");
            printID(i, j);
        }
    }
    //begin
    public static void BeginState(int i, int j) {   
        if (Code[j] == 'e')
            BeginState(i, j + 1);
        else if (Code[j] == 'g')
            BeginState(i, j + 1);
        else if (Code[j] == 'i')
            BeginState(i, j + 1);
        else if (Code[j] == 'n') {
            GUI.tokenList.append((k++)+" ������:begin\n");
            yuanyu.add("begin");
            tokens.add("begin");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }

    //end
    public static void EndState(int i, int j) {   
        if (Code[j] == 'n')
            EndState(i, j + 1);
        else if (Code[j] == 'd') {
            GUI.tokenList.append((k++)+" ������:end\n");
            yuanyu.add("end");
            tokens.add("end");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }

    //if
    public static void IfState(int i, int j) {   
        if (Code[j] == 'f') {
            GUI.tokenList.append((k++)+" ������:if\n");
            yuanyu.add("if");
            tokens.add("if");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }
  //or
    public static void OrState(int i, int j) {   
        if (Code[j] == 'r') {
            GUI.tokenList.append((k++)+" �߼������:or\n");
            yuanyu.add("or");
            tokens.add("or");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }
    //and
    public static void AndState(int i, int j) {   
    	if (Code[j] == 'n')
            AndState(i, j + 1);
        else if (Code[j] == 'd') {
            GUI.tokenList.append((k++)+" �߼������:and\n");
            yuanyu.add("and");
            tokens.add("and");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }
  //not
    public static void NotState(int i, int j) {   
    	if (Code[j] == 'o') {
    		NotState(i, j + 1);
    		}
        else if (Code[j] == 't') {
            GUI.tokenList.append((k++)+" ��ϵ�����:not\n");
            yuanyu.add("!");
            tokens.add("not");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }
    //then
    public static void ThenState(int i, int j) {   
        if (Code[j] == 'h')
            ThenState(i, j + 1);
        else if (Code[j] == 'e')
            ThenState(i, j + 1);
        else if (Code[j] == 'n') {
            GUI.tokenList.append((k++)+" ������:then\n");
            yuanyu.add("then");
            tokens.add("then");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }

    //true
    public static void TrueState(int i, int j) {   
        if (Code[j] == 'r')
            TrueState(i, j + 1);
        else if (Code[j] == 'u')
            TrueState(i, j + 1);
        else if (Code[j] == 'e') {
            GUI.tokenList.append((k++)+" ������:true\n");
            yuanyu.add("true");
            tokens.add("true");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }

    //false
    public static void FalseState(int i, int j) {   
        if (Code[j] == 'a')
            FalseState(i, j + 1);
        else if (Code[j] == 'l')
            FalseState(i, j + 1);
        else if (Code[j] == 's')
            FalseState(i, j + 1);
        else if (Code[j] == 'e') {
            GUI.tokenList.append((k++)+" ������:false\n");
            yuanyu.add("false");
            tokens.add("false");
            StartState(j + 1, j + 1);
        } else
            IdentState(i, i + 1);
    }
    
    //rop
    public static void RopState(int i, int j) {
        if (Code[i] == '<' && Code[j] != '=') {
            GUI.tokenList.append((k++)+" �������<\n");
            yuanyu.add("<");
            tokens.add("rop");
            StartState(i + 1, i + 1);
        } else if (Code[i] == '<' && Code[j] == '=') {
            GUI.tokenList.append((k++)+" �������<=\n");
            yuanyu.add("<=");
            tokens.add("rop");
            StartState(i + 2, i + 2);
        } else if (Code[i] == '>' && Code[j] != '=') {
            GUI.tokenList.append((k++)+" �������>\n");
            yuanyu.add(">");
            tokens.add("rop");
            StartState(i + 1, i + 1);
        } else if (Code[i] == '>' && Code[j] == '=') {
            GUI.tokenList.append((k++)+" �������>=\n");
            yuanyu.add(">=");
            tokens.add("rop");
            StartState(i + 2, i + 2);
        } 
    }

    public static void printID(int i, int j) {
        String temp="";
    	for (int k = i; k < j; k++) {
            temp = temp + Code[k];
        }
        GUI.tokenList.append(temp+"\n");
        yuanyu.add(temp);
        tokens.add("id");
        StartState(j, j);
    }
}
