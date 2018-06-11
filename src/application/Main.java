package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Vector;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

	// ��¼��������
	public void start(Stage primaryStage) {

		// ������̨�������
		primaryStage.setTitle("LL(1)�ķ��ж�");
		GridPane grid = new GridPane();//�������
		grid.setAlignment(Pos.CENTER);// ���ö��뷽��Ϊ��������
		grid.setHgap(11);// ��֮���ˮƽ���Ŀ��
		grid.setVgap(11);// ������֮��Ĵ�ֱ���ĸ߶�
		grid.setPadding(new Insets(15, 8, 15, 20));// �����ң��ײ��������Χ�������������

		// ���ó�������
		Text scenetitle = new Text("LL(1) Grammar Judgment");
		scenetitle.setId("welcome-text");
		grid.add(scenetitle, 0, 0, 2, 1);

		Label production = new Label("�����ж��ķ�����ʽ:");
		grid.add(production, 0, 2, 1, 1);

		TextField production_left = new TextField("����ʽ��");
		grid.add(production_left, 1, 1, 2, 2);

		Label arrow_symbol = new Label(" �� ");
		grid.add(arrow_symbol, 3, 2, 1, 1);

		TextField production_right = new TextField("����ʽ�Ҳ�");
		grid.add(production_right, 4, 1, 2, 2);

		Button btn = new Button("��Ӳ���ʽ");
		grid.add(btn, 6, 1, 2, 2);

		Button btnn = new Button("    �� �� �� ��     ");
		btnn.setId("button1");
		grid.add(btnn, 6, 32, 8, 8);

		Button bttn = new Button("���ȫ��");
		grid.add(bttn, 8, 1, 2, 2);

		TextArea GrammarText = new TextArea();
		HBox hGrammarText = new HBox(10);//ˮƽ����
		hGrammarText.setAlignment(Pos.BOTTOM_RIGHT);//���ö��뷽ʽ
		hGrammarText.getChildren().add(GrammarText);
		grid.add(GrammarText, 0, 4, 3, 30);

		TextArea AnalysisText = new TextArea();
		HBox hAnalysisText = new HBox(10);
		hAnalysisText.setAlignment(Pos.BOTTOM_RIGHT);
		hAnalysisText.getChildren().add(AnalysisText);
		grid.add(AnalysisText, 5, 4, 5, 30);

		// ��Ӳ���ʽ��ť�¼�
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String a = production_left.getText();
				String b = production_right.getText();
				GrammarText.appendText(a + '��' + b + '\n');// ��������Ĳ���ʽ��ӵ��ı���
			}
		});

		// ���ȫ����ť�¼�
		bttn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				production_left.setText("");
				production_right.setText("");
				GrammarText.setText("");
				AnalysisText.setText("");
				LL1.Vn = null;
				LL1.P = null;
				LL1.firstComplete = null;
				LL1.first = null;
				LL1.followComplete = null;
				LL1.follow = null;
				LL1.select = null;
			}
		});

		// �ж��ķ���ť�¼�
		btnn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// �ж��ķ���ť������������
				{
					AnalysisText.setText("");// ÿ���ж϶�Ҫ����ϴ���ʾ������
					int Vnnum = 0;// ���ս������
					int k;//ѭ����������i,k
					LL1.Vn = new String[100];// Vn���ս�����ϣ��ַ������ͣ�
					LL1.P = new Vector<String>();// �ַ�������

					String s[] = GrammarText.getText().split("\n");// ���ζ�ȡ�ı����е�ÿ������ʽ
					                                               //split()���ݸ���������ʽ��ƥ���ִ��ַ���
					for (int i = 0; i < s.length; i++) // s.lengthΪ����ʽ������
					{
						if (s.length < 2)//  ��s.lengthΪ����ʽ������<2,�������
						{
							AnalysisText.setText("�ķ������������������룡����");// �жϲ���ʽ�����Ƿ����
							return;//���ټ���ִ��
						}

						if (s[i].charAt(0) <= 'Z' && s[i].charAt(0) >= 'A' && s[i].charAt(1) == '��')// ����ʽһ�����
						{
							for (k = 0; k < Vnnum; k++)//ѭ��Vnnum���ս�������Ĵ���
							{
								if (LL1.Vn[k].equals(s[i].substring(0, 1))) {
									break;// ��һ���ҷ��ս������ͬ���ս��������
									      //public boolean equals(Object anObject)�����ַ�����s[i]����ʽ����ָ���Ķ���s[i]����ʽ��һ�����Ƚ�
									      //public String substring(int beginIndex,int endIndex)����һ�����ַ��������Ǵ��ַ�����һ�����ַ���
								}
							}
							if (Vnnum == 0 || k >= Vnnum)// �ҵ��µķ��ս��������Vn[]
							{
								LL1.Vn[Vnnum] = s[i].substring(0, 1);// ����ʽ�󲿴��� Vn����
								Vnnum++;// ͳ��Vn������
							}
							LL1.P.add(s[i]);// ����i�в���ʽ��ӵ�����p��
						}

						else// ����ʽ�󲿲��Ƿ��ս��
						{
							AnalysisText.setText("�ķ������������������룡����");
							return;//���ټ���ִ��
						}
					} // ���� for (i = 0; i < s.length; i++)ѭ��

					LL1.yn_null = new char[100];// �洢�ܷ��Ƴ���
					LL1.first = new char[Vnnum][100];// ÿ�����ս��FIRST���ϣ���ά���飬Vnnum�����������õ����ս������
					int flag = 0;
					String firstVn[] = null;
					LL1.firstComplete = new int[Vnnum];// �洢���жϹ� first ������

					for (int i = 0; LL1.Vn[i] != null; i++) // ���ζ�ÿ��Vn�� FIRST��
					{
						flag = 0;
						firstVn = new String[20];
						if ((flag = LL1.addFirst(LL1.first[i], LL1.Vn[i], firstVn, flag)) == -1)
							// addFirst(char a[], String b, String firstVn[],int flag)���� FIRST**���ݹ飩
							// �����ж�x �Ƿ������ first�����flag��Ϊ-1�����ʾ�Ѿ����һ��Vn��first��
							return;
						LL1.firstComplete[i] = 1;
					}

					AnalysisText.appendText("FIRST ����" + "\n"); // ��ʾ FIRST��
					for (int i = 0; LL1.Vn[i] != null; i++) {// Vn���ս�����ϣ��ַ������ͣ�
						AnalysisText.appendText("FIRST(" + LL1.Vn[i] + ")={ " + LL1.first[i][0]); // �洢���
																									// first
																									// ���������
						for (int j = 1; LL1.first[i][j] != '\0'; j++) {
							AnalysisText.appendText(" , " + LL1.first[i][j]);
						}
						AnalysisText.appendText(" }" + "\n");
					}
					AnalysisText.appendText("\n");

					// END FIRST//

					LL1.follow = new char[Vnnum][100]; // �洢��� follow ���
					String followVn[] = null; // �洢���жϹ� follow ������
					LL1.followComplete = new int[Vnnum];// �ж� x�Ƿ������ follow
					for (int i = 0; LL1.Vn[i] != null; i++) // �� FOLLOW     // Vn���ս�����ϣ��ַ������ͣ�
					{
						flag = 0;
						followVn = new String[20];
						if ((flag = LL1.addFollow(LL1.follow[i], LL1.Vn[i], followVn, flag)) == -1)
							// addFollow(char a[], String b, String followVn[],
							// int flag)���� FOLLOW���ݹ飩
							return;
						LL1.followComplete[i] = 1;// �ж� x�Ƿ������ follow
					}
					AnalysisText.appendText("FOLLOW ����" + "\n"); // ��ʾ FOLLOW
					for (int i = 0; LL1.Vn[i] != null; i++) {
						AnalysisText.appendText("FOLLOW(" + LL1.Vn[i] + ")={ " + LL1.follow[i][0]);// �洢��� follow ���
						for (int j = 1; LL1.follow[i][j] != '\0'; j++) {
							AnalysisText.appendText(" , " + LL1.follow[i][j]);
						}
						AnalysisText.appendText(" }" + "\n");
					}
					AnalysisText.appendText("\n");
					// END FOLLOW//

					LL1.select = new char[LL1.P.size()][100]; // P.size()�ǲ���ʽ����Ŀ��ÿ������ʽ����һ��select��
					for (int i = 0; i < LL1.P.size(); i++) // �� SELECT
					{
						flag = 0;// ��ֵΪ0
						LL1.addSelect(LL1.select[i], (String) LL1.P.elementAt(i), flag);// ����SELECT
						                                                                //elementAt(int index)����ָ�������������
					}

					// AnalysisText.appendText("SELECT ����" + "\n"); // ��ʾ SELECT
					for (int i = 0; i < LL1.P.size(); i++) {
						// AnalysisText.appendText("SELECT(" + (String)
						// LL1.P.elementAt(i) + ")={ " + LL1.select[i][0]);
						for (int j = 1; LL1.select[i][j] != '\0'; j++) {
							// AnalysisText.appendText(" , " +
							// LL1.select[i][j]);
						}
						// AnalysisText.appendText(" }" + "\n");
					}
					// AnalysisText.appendText("\n");
					// end select in text//

					int temp = 0;// ���������0 ��LL(1)
					for (int i = 0; LL1.Vn[i] != null; i++)// �ж� select�����Ƿ�Ϊ��
					{
						// int temp = 0;
						char save[] = new char[100];
						for (int j = 0; j < LL1.P.size(); j++) {
							String t = (String) LL1.P.elementAt(j);
							if (t.substring(0, 1).equals(LL1.Vn[i])) {
								for (k = 0; LL1.select[j][k] != '\0'; k++) {// select[][]�洢��� select ���
									if (LL1.inChar(save, LL1.select[j][k])) // ��һ��ӵ�save�У����ظ��ľͲ�ִ�����
										                                    // boolean inChar(char a[], char x) 
									{
										save[temp] = LL1.select[j][k];
										temp++;
									} else// ���ظ�����������н���ʱ����Ϊ LL1�ķ�
									{
										AnalysisText.appendText("���� LL��1���ķ�������" + "\n");
										//AnalysisText.appendText("first����follow���Ľ�����Ϊ�գ�");
										return;
									}
								}
							}
						}
					}
					if (temp != 0) {
						AnalysisText.appendText("���ķ��� LL��1���ķ�������" + "\n");
					}
				}
			}
		});

		// ��������
		Scene scene = new Scene(grid, 780, 500);// ��̨����Ϊgrid���֣�780*500��С�ĳ���
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());// ʹ��CSS
		primaryStage.show();
	}

	// ��������
	public static void main(String[] args) {
		launch(args);
	}

}