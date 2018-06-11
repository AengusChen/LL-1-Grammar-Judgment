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

	// 登录窗口设置
	public void start(Stage primaryStage) {

		// 设置舞台布局面板
		primaryStage.setTitle("LL(1)文法判断");
		GridPane grid = new GridPane();//网格面板
		grid.setAlignment(Pos.CENTER);// 设置对齐方法为中心网格
		grid.setHgap(11);// 列之间的水平差距的宽度
		grid.setVgap(11);// 行与行之间的垂直间距的高度
		grid.setPadding(new Insets(15, 8, 15, 20));// 顶，右，底部，左侧周围地区的内容填充

		// 设置场景标题
		Text scenetitle = new Text("LL(1) Grammar Judgment");
		scenetitle.setId("welcome-text");
		grid.add(scenetitle, 0, 0, 2, 1);

		Label production = new Label("输入判定文法产生式:");
		grid.add(production, 0, 2, 1, 1);

		TextField production_left = new TextField("产生式左部");
		grid.add(production_left, 1, 1, 2, 2);

		Label arrow_symbol = new Label(" → ");
		grid.add(arrow_symbol, 3, 2, 1, 1);

		TextField production_right = new TextField("产生式右部");
		grid.add(production_right, 4, 1, 2, 2);

		Button btn = new Button("添加产生式");
		grid.add(btn, 6, 1, 2, 2);

		Button btnn = new Button("    判 断 文 法     ");
		btnn.setId("button1");
		grid.add(btnn, 6, 32, 8, 8);

		Button bttn = new Button("清空全部");
		grid.add(bttn, 8, 1, 2, 2);

		TextArea GrammarText = new TextArea();
		HBox hGrammarText = new HBox(10);//水平布局
		hGrammarText.setAlignment(Pos.BOTTOM_RIGHT);//设置对齐方式
		hGrammarText.getChildren().add(GrammarText);
		grid.add(GrammarText, 0, 4, 3, 30);

		TextArea AnalysisText = new TextArea();
		HBox hAnalysisText = new HBox(10);
		hAnalysisText.setAlignment(Pos.BOTTOM_RIGHT);
		hAnalysisText.getChildren().add(AnalysisText);
		grid.add(AnalysisText, 5, 4, 5, 30);

		// 添加产生式按钮事件
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String a = production_left.getText();
				String b = production_right.getText();
				GrammarText.appendText(a + '→' + b + '\n');// 将所输入的产生式添加到文本区
			}
		});

		// 清空全部按钮事件
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

		// 判断文法按钮事件
		btnn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// 判断文法按钮，有三级操作
				{
					AnalysisText.setText("");// 每次判断都要清空上次显示的内容
					int Vnnum = 0;// 非终结符个数
					int k;//循环条件变量i,k
					LL1.Vn = new String[100];// Vn非终结符集合（字符串类型）
					LL1.P = new Vector<String>();// 字符串向量

					String s[] = GrammarText.getText().split("\n");// 依次读取文本框中的每条产生式
					                                               //split()根据给定正则表达式的匹配拆分此字符串
					for (int i = 0; i < s.length; i++) // s.length为产生式的条数
					{
						if (s.length < 2)//  当s.length为产生式的条数<2,有误情况
						{
							AnalysisText.setText("文法输入有误，请重新输入！！！");// 判断产生式长度是否符合
							return;//不再继续执行
						}

						if (s[i].charAt(0) <= 'Z' && s[i].charAt(0) >= 'A' && s[i].charAt(1) == '→')// 产生式一般情况
						{
							for (k = 0; k < Vnnum; k++)//循环Vnnum非终结符个数的次数
							{
								if (LL1.Vn[k].equals(s[i].substring(0, 1))) {
									break;// 逐一查找非终结符，相同的终结符则跳过
									      //public boolean equals(Object anObject)将此字符串（s[i]产生式）与指定的对象（s[i]产生式第一个）比较
									      //public String substring(int beginIndex,int endIndex)返回一个新字符串，它是此字符串的一个子字符串
								}
							}
							if (Vnnum == 0 || k >= Vnnum)// 找到新的非终结符，加入Vn[]
							{
								LL1.Vn[Vnnum] = s[i].substring(0, 1);// 产生式左部存入 Vn数组
								Vnnum++;// 统计Vn的总数
							}
							LL1.P.add(s[i]);// 将第i行产生式添加到向量p中
						}

						else// 产生式左部不是非终结符
						{
							AnalysisText.setText("文法输入有误，请重新输入！！！");
							return;//不再继续执行
						}
					} // 结束 for (i = 0; i < s.length; i++)循环

					LL1.yn_null = new char[100];// 存储能否推出空
					LL1.first = new char[Vnnum][100];// 每个非终结符FIRST集合，二维数组，Vnnum由上述操作得到非终结符个数
					int flag = 0;
					String firstVn[] = null;
					LL1.firstComplete = new int[Vnnum];// 存储已判断过 first 的数据

					for (int i = 0; LL1.Vn[i] != null; i++) // 依次对每个Vn求 FIRST集
					{
						flag = 0;
						firstVn = new String[20];
						if ((flag = LL1.addFirst(LL1.first[i], LL1.Vn[i], firstVn, flag)) == -1)
							// addFirst(char a[], String b, String firstVn[],int flag)计算 FIRST**（递归）
							// 用于判断x 是否已完成 first，如果flag不为-1，则表示已经完成一个Vn的first集
							return;
						LL1.firstComplete[i] = 1;
					}

					AnalysisText.appendText("FIRST 集：" + "\n"); // 显示 FIRST集
					for (int i = 0; LL1.Vn[i] != null; i++) {// Vn非终结符集合（字符串类型）
						AnalysisText.appendText("FIRST(" + LL1.Vn[i] + ")={ " + LL1.first[i][0]); // 存储最后
																									// first
																									// 结果的数组
						for (int j = 1; LL1.first[i][j] != '\0'; j++) {
							AnalysisText.appendText(" , " + LL1.first[i][j]);
						}
						AnalysisText.appendText(" }" + "\n");
					}
					AnalysisText.appendText("\n");

					// END FIRST//

					LL1.follow = new char[Vnnum][100]; // 存储最后 follow 结果
					String followVn[] = null; // 存储已判断过 follow 的数据
					LL1.followComplete = new int[Vnnum];// 判断 x是否已完成 follow
					for (int i = 0; LL1.Vn[i] != null; i++) // 求 FOLLOW     // Vn非终结符集合（字符串类型）
					{
						flag = 0;
						followVn = new String[20];
						if ((flag = LL1.addFollow(LL1.follow[i], LL1.Vn[i], followVn, flag)) == -1)
							// addFollow(char a[], String b, String followVn[],
							// int flag)计算 FOLLOW（递归）
							return;
						LL1.followComplete[i] = 1;// 判断 x是否已完成 follow
					}
					AnalysisText.appendText("FOLLOW 集：" + "\n"); // 显示 FOLLOW
					for (int i = 0; LL1.Vn[i] != null; i++) {
						AnalysisText.appendText("FOLLOW(" + LL1.Vn[i] + ")={ " + LL1.follow[i][0]);// 存储最后 follow 结果
						for (int j = 1; LL1.follow[i][j] != '\0'; j++) {
							AnalysisText.appendText(" , " + LL1.follow[i][j]);
						}
						AnalysisText.appendText(" }" + "\n");
					}
					AnalysisText.appendText("\n");
					// END FOLLOW//

					LL1.select = new char[LL1.P.size()][100]; // P.size()是产生式的数目，每个产生式都有一个select集
					for (int i = 0; i < LL1.P.size(); i++) // 求 SELECT
					{
						flag = 0;// 初值为0
						LL1.addSelect(LL1.select[i], (String) LL1.P.elementAt(i), flag);// 计算SELECT
						                                                                //elementAt(int index)返回指定索引处的组件
					}

					// AnalysisText.appendText("SELECT 集：" + "\n"); // 显示 SELECT
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

					int temp = 0;// 如果不等于0 是LL(1)
					for (int i = 0; LL1.Vn[i] != null; i++)// 判断 select交集是否为空
					{
						// int temp = 0;
						char save[] = new char[100];
						for (int j = 0; j < LL1.P.size(); j++) {
							String t = (String) LL1.P.elementAt(j);
							if (t.substring(0, 1).equals(LL1.Vn[i])) {
								for (k = 0; LL1.select[j][k] != '\0'; k++) {// select[][]存储最后 select 结果
									if (LL1.inChar(save, LL1.select[j][k])) // 逐一添加到save中，有重复的就不执行添加
										                                    // boolean inChar(char a[], char x) 
									{
										save[temp] = LL1.select[j][k];
										temp++;
									} else// 有重复情况，即当有交集时，不为 LL1文法
									{
										AnalysisText.appendText("不是 LL（1）文法！！！" + "\n");
										//AnalysisText.appendText("first集与follow集的交集不为空！");
										return;
									}
								}
							}
						}
					}
					if (temp != 0) {
						AnalysisText.appendText("该文法是 LL（1）文法！！！" + "\n");
					}
				}
			}
		});

		// 场景设置
		Scene scene = new Scene(grid, 780, 500);// 舞台布置为grid布局，780*500大小的场景
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());// 使用CSS
		primaryStage.show();
	}

	// 窗口运行
	public static void main(String[] args) {
		launch(args);
	}

}