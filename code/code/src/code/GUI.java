package code;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.TextArea;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;

public class GUI extends JFrame {

	private JPanel contentPane;
	public static JTextArea tokenList = new JTextArea();
	public static JTextArea textArea = new JTextArea();
	public static ArrayList<String> yuanyu = new ArrayList<>();
	public static JTextArea textArea2 = new JTextArea();
	ArrayList<String> tokens=new ArrayList<String>();
	String txt="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1188, 695);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane_1_1 = new JScrollPane();
		scrollPane_1_1.setBounds(6, 350, 363, 279);
		contentPane.add(scrollPane_1_1);
		
		//JTextArea textArea2 = new JTextArea();
		scrollPane_1_1.setViewportView(textArea2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(390, 62, 770, 567);
		contentPane.add(scrollPane);
		
		//JTextArea tokenList = new JTextArea();
		scrollPane.setViewportView(tokenList);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 62, 357, 256);
		contentPane.add(scrollPane_1);
		
		//JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		Button open = new Button("\u9009\u62E9\u6587\u672C");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open();				 
			}
		});
		open.setBounds(594, 26, 105, 30);
		contentPane.add(open);
		
		Button save = new Button("\u4FDD\u5B58");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 save();
			}
		});
		save.setBounds(1045, 26, 105, 30);
		contentPane.add(save);
		
		JLabel lblSlr = new JLabel("SLR\uFF081\uFF09\u5206\u6790\u8868");
		lblSlr.setBounds(10, 327, 191, 21);
		contentPane.add(lblSlr);
		
		JLabel label = new JLabel("\u6D4B\u8BD5\u6587\u672C");
		label.setBounds(15, 35, 81, 21);
		contentPane.add(label);
		
		Button cifa = new Button("\u8BCD\u6CD5\u5206\u6790");
		cifa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(txt=="") {
						tokenList.append("��û��ѡ���ı����޷��������������\n");
					}
					else
					AnalyzeTokens(txt);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		cifa.setBounds(705, 26, 105, 30);
		contentPane.add(cifa);
		
		Button yufa = new Button("\u8BED\u6CD5\u5206\u6790");
		yufa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run(tokens);
			}
		});
		yufa.setBounds(816, 26, 105, 30);
		contentPane.add(yufa);
		
		Button yuyi = new Button("\u8BED\u4E49\u5206\u6790");
		yuyi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yuyi_analysis.run();
			}
		});
		yuyi.setBounds(934, 26, 105, 30);
		contentPane.add(yuyi);
		
		JLabel label_1 = new JLabel("\u8F93\u51FA\u7ED3\u679C");
		label_1.setBounds(393, 35, 81, 21);
		contentPane.add(label_1);
		
        SLR.Init();
        SLR.show();
	}
	
	//��ʾ�ʷ��������
    public void AnalyzeTokens(String txt) throws IOException {
        tokens = cifa_analysis.Run(txt);
        yuyi_analysis.yuju.removeAll(yuyi_analysis.yuju);
        String s1=tokens.get(0).toString();
        String s2="#";
        if(s1==s2) tokens.remove(0);
        tokenList.append("�õ��������봮��");
        for (String c : tokens) {
            tokenList.append(c+" ");
            yuyi_analysis.yuju.add(c);
        }//����ָ���
        tokenList.append("\n**********************�ʷ���������******************\n");
    }

    public void run(ArrayList<String> tokens) {
        yuanyu=(ArrayList<String>) cifa_analysis.yuanyu.clone();
    	yufa_analysis yufa = new yufa_analysis();
        yufa.analyze(tokens,yuanyu);
    }
    
    public void open() {
    	JFileChooser fileChooser=new JFileChooser();
		 int option = fileChooser.showDialog(null, null);	 
		 if(option == JFileChooser.APPROVE_OPTION) { // ʹ���߰���ȷ�ϼ�
		 try {
		 BufferedReader buf = 
		 new BufferedReader(
		 new FileReader(
		 fileChooser.getSelectedFile()));
		 setTitle(fileChooser.getSelectedFile().toString());// �趨�ļ�����
		 textArea.setText(fileChooser.getSelectedFile().toString()+"���ĵ�����Ϊ\n");
		 tokenList.append("��"+fileChooser.getSelectedFile().toString()+"���з���\n");
		 txt=fileChooser.getSelectedFile().toString();//��ȡ�ļ���
		 String lineSeparator = System.getProperty("line.separator");// ���ǰһ���ļ�
		 // ��ȡ�ļ������������ֱ༭��
		 String text;
		 while((text = buf.readLine()) != null) {
		 textArea.append(text);
		 textArea.append(lineSeparator);
		 }
		 buf.close();
		 } 
		 catch(IOException e1) {
			 JOptionPane.showMessageDialog(null, 
			e1.toString(),
			 "�����ļ�ʧ��", JOptionPane.ERROR_MESSAGE);
			 } 
		 }
    }

    public void save() {
    	JFileChooser fileChooser= new JFileChooser();
		// ��ʾ�ļ��Ի���
		 int option = fileChooser.showSaveDialog(null);
		 // ���ȷ��ѡȡ�ļ�
		 if(option == JFileChooser.APPROVE_OPTION) {
		 // ȡ��ѡ����ļ�
		 File file = fileChooser.getSelectedFile();			 
		 // �ڱ��������趨�ļ�����
		 setTitle(file.toString());		 
		 try {
		 file.createNewFile();// �����ļ�
		 // �����ļ�����
		 BufferedWriter buf = new BufferedWriter(new FileWriter(file));// ����ָ�����ļ�
		 buf.write(tokenList.getText());// �����ֱ༭��������д���ļ�
		 buf.close(); 
		 } catch(IOException e1) {
		 JOptionPane.showMessageDialog(null, e1.toString(),"�޷��������ļ�", JOptionPane.ERROR_MESSAGE);
		 } 
		 }  
    }
}
