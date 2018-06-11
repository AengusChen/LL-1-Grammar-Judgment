package application;

import java.util.Vector;

public abstract class LL1 {
	
	static String Vn[] = null; // 所有非终结符集合
	static Vector<String> P = null; // 字符串型向量，用于填预测分析表
	static int firstComplete[] = null; // 存储已判断过 first 的数据
	static char first[][] = null; // 存储最后 first 结果的数组
	static int followComplete[] = null;// 存储已判断过 follow 的数据
	static char follow[][] = null; // 存储最后 follow 结果
	static char select[][] = null; // select[][]存储最后 select 结果
	static char yn_null[] = null; // 存储能否推出空
	
	static int addFirst(char a[], String b, String firstVn[], int flag)// 添加first集
	// addFirst(first[i], Vn[i], firstVn, flag)
	// 计算 FIRST**（递归）
	{
		if (inString(firstVn, b.charAt(0))) // 判断firstVn字符串中是否存在b的第一个字符，不存在为true
		// boolean inString(String a[], char b)
		{
			addString(firstVn, b);// 不在，把 第i个Vn加入字符串组 firstVn[]
		} else {
			return flag;
		}

		for (int i = 0; i < P.size(); i++) // P.size()是产生式的数目
		{
			String t = (String) P.elementAt(i);// 向量第i处元素赋值给字符串t，t为产生式字符串
			for (int k = 2; k < t.length(); k++)// t.length()是本条产生式的长度，从产生式右边第一个字符开始判断（T->+SB）
			{
				if (t.substring(0, 1).equals(b)) // 向量第i条产生式首字符与Vn[i]相等
				{
					if (t.charAt(k) > 'Z' || t.charAt(k) < 'A')// 遇到的是终结符（不是A~Z）,继续
					{
						if (flag == 0 || inChar(a, t.charAt(k))) // 表达式右端第
																	// k-1个字符不在该Vn的first集中，避免重复添加
																	// 例如A->aB,A->aC
						{
							if (t.charAt(k) == '#')// 空时
							{
								if (k + 1 == t.length()) // 是最后一个字符
								{
									a[flag] = t.charAt(k);// 加入该Vn的first集
									flag++;
								}
								int flag1 = 0;
								for (int j = 0; yn_null[j] != '\0'; j++)// 所求Vn进入yn_null[]
								{
									if (yn_null[j] == b.charAt(0))// 循环判断Vn[i]能否推出空
									{
										flag1 = 1;
										break;// 已经在循环查找时把能推出空的Vn加入了yn_null[]，不需要再执行
									}
								}
								if (flag1 == 0)// 没找到，则需要把能推出空的Vn加入yn_null[]
								{
									int j;
									for (j = 0; yn_null[j] != '\0'; j++) {
									}
									yn_null[j] = b.charAt(0);// 让j在yn_null[]的最后一个位置，加入Vn
								}
								continue;// 继续右移查找下一个字符
							}
							// end if (t.charAt(k) == '#')
							else// 如果不为空，则终结符直接加入 first[],即a[]
							{
								a[flag] = t.charAt(k);
								flag++;
								break;
							}
						}
						break;
					} // end if(t.charAt(k) > 'Z' || t.charAt(k) < 'A')
					else // 遇到的第一个字符为非终结符的情况
					{
						if (!inString(Vn, t.charAt(k))) // 如果检索到的该字符是Vn
						// boolean inString(String a[], char b)
						{
							int p = firstComplete(t.charAt(k));// 当该非终结符的
																// first集已经求出

							if (p != -1) // 已经完成first判断
							{
								flag = addElementFirst(a, p, flag);// 直接加入所求first

							}
							// 如果还没有完成判断
							else if ((flag = addFirst(a, String.valueOf(t.charAt(k)), firstVn, flag)) == -1)// 递归
								return -1;
							int flag1 = 0;
							for (int j = 0; yn_null[j] != '\0'; j++)// 当非终结符的first集有#
							{
								if (yn_null[j] == t.charAt(k)) {
									flag1 = 1;
									break;
								}
							}
							if (flag1 == 1)// 当非终结符的 first 能推出空
							{
								if (k + 1 == t.length() && inChar(a, '#'))// 之后无符号，且所求first集中无空。如S->A,A->#
								{
									a[flag] = '#';// first 中加入#
									flag++;
								}
								continue;// 判断下一个字符
							} else {
								break;
							}
						}
						// endif(!inString(Vn, t.charAt(k))) //如果检索到的该字符是Vn
						else// 检索的该字符为其他
						{
							return -1;
						}
					} // 非终结符情况
				}
			} // 一条产生式处理完毕
		} // 所有产生式处理完毕
		return flag;
	}

	static int addFollow(char a[], String b, String followVn[], int flag)
	// addFollow(follow[i], Vn[i], followVn, flag)
	// 计算 FOLLOW（递归）
	{
		if (inString(followVn, b.charAt(0))) {
			addString(followVn, b);
		} else {
			return flag;
		}
		if (b.equals("S")) // 当为 S 时#直接存入 follow
		{
			a[flag] = '#';
			flag++;
		}
		for (int i = 0; i < P.size(); i++) {
			String t = (String) P.elementAt(i);
			for (int j = 2; j < t.length(); j++) {
				if (t.charAt(j) == b.charAt(0) && j + 1 < t.length()) // 产生式右端的该Vn不是最后一个字符
				{
					if (t.charAt(j + 1) != '\0') {
						if ((t.charAt(j + 1) > 'Z' || t.charAt(j + 1) < 'A'))// 该Vn后一位是终结符，例如S->Ac的c
						{
							if (flag == 0 || inChar(a, t.charAt(2))) {
								a[flag] = t.charAt(j + 1);// 将后一位加入FOLLOW集
								flag++;
							}
						} else// 后一位为Vn
						{
							int k;
							for (k = 0; Vn[k] != null; k++) {
								if (Vn[k].equals(String.valueOf(t.charAt(j + 1)))) {
									break;// 找寻下一个非终结符的 Vn 位置
								}
							}
							// 找到了下标k
							flag = addElementFirst(a, k, flag);// 把下一个非终结符 first加入所求 follow集，a此时为follow[]

							for (k = j + 1; k < t.length(); k++) {
								if ((t.charAt(j + 1) > 'Z' || t.charAt(j + 1) < 'A'))// 后一位不是Vn，不符合，break
									break;
								else {
									if (isEmpty(t.charAt(k))) // 下一个非终结符可推出空，把表达式左边Vn的follow集加入所求 follow集
									{
										int p = followComplete(t.charAt(0));// 得到产生式左部Vn的下标
										if (p != -1) {
											flag = addElementFollow(a, p, flag);// flag用于递归判断
										} else if ((flag = addFollow(a, String.valueOf(t.charAt(0)), followVn,
												flag)) == -1)// 递归
											return -1;
									} else {
										break;
									}
								}
							}
						}
					} else // 错误文法
					{
						return -1;
					}
				}
				if (t.charAt(j) == b.charAt(0) && j + 1 == t.length())
				// 右边只有一个Vn下一个非终结符可推出空，把表达式左边Vn的follow 集加入所求 follow 集
				// 例如A->B,FOLLOW(A)加入FOLLOW(B)
				{
					int p = followComplete(t.charAt(0));// 找到左部Vn的下标p
					if (p != -1) {
						flag = addElementFollow(a, p, flag);// 把下标为p的Vn的FOLLOW集加入到当前FOLLOW集
					} else if ((flag = addFollow(a, String.valueOf(t.charAt(0)), followVn, flag)) == -1)
						return -1;
				}
			}
		}
		return flag;
	}

	static void addSelect(char a[], String b, int flag) // 计算 SELECT
	// addSelect(select[i], (String) P.elementAt(i), flag);
	{
		int i = 2;// i用于定位，起始位置为右端第一个字符
		int temp = 0;
		while (i < b.length()) // b.length()是一条产生式的长度
		{
			if ((b.charAt(i) > 'Z' || b.charAt(i) < 'A') && b.charAt(i) != '#')// 是终结符，且不为空，
				                                                               //例如A->b或A->+...
			{
				a[flag] = b.charAt(i);// 将这个字符加入到 Select集，结束 Select集 的计算
				break;
			} else if (b.charAt(i) == '#')// 是空
			{
				int j;
				for (j = 0; Vn[i] != null; j++)// 将表达式左侧的非终结符的 follow 加入
												// select，例如A->#，则需计算FOLLOW(A)
				{
					if (Vn[j].equals(b.substring(0, 1))) // 找到产生式左部Vn的下标j
					{
						break;
					}
				}
				for (int k = 0; follow[j][k] != '\0'; k++) // 依次把左部Vn的Follow集加入到本产生式的select集
				{
					if (inChar(a, follow[j][k])) {
						a[flag] = follow[j][k];
						flag++;
					}
				}
				break;
			} else if (b.charAt(i) >= 'A' && b.charAt(i) <= 'Z' && i + 1 < b.length())// 是Vn，且有下一个字符
			{
				int j;
				for (j = 0; Vn[i] != null; j++) {
					if (Vn[j].equals(String.valueOf(b.charAt(i))))// 逐一与当前Vn比较找到下标j
					{
						break;// 找到，结束循环
					}
				}
				for (int k = 0; first[j][k] != '\0'; k++) {
					if (inChar(a, first[j][k]))// 把表达式右侧所有非终结符的 first 集加入
					{
						if (first[j][k] == '#')// first 中存在空，即右侧的Vn能推出空
						{
							temp = 1;
						} else // 右侧的Vn推不出空
						{
							a[flag] = first[j][k];// 加入
							flag++;
						}
					}
				}
				if (temp == 1)// 把右侧所有非终结符的 first 中的#去除
				{
					i++;
					temp = 0;// 改为0
					continue;
				} else {
					temp = 0;
					break;
				}
			} else if (b.charAt(i) >= 'A' && b.charAt(i) <= 'Z' && i + 1 >= b.length())// 是Vn且已经是最后一个字符
			{
				int j;
				for (j = 0; Vn[i] != null; j++) {
					if (Vn[j].equals(String.valueOf(b.charAt(i)))) // 固定Vn[i]找下标j
					{
						break;
					}
				}
				for (int k = 0; first[j][k] != '\0'; k++) {
					if (inChar(a, first[j][k])) {
						if (first[j][k] == '#') {
							temp = 1;// 表达式右侧能推出空，标记1
						} else {
							a[flag] = first[j][k];// 不能推出空，直接将 first 集加入 select集

							flag++;
						}
					}
				}
				if (temp == 1)// 表达式右侧能推出空，需要把左部的FOLLOW加入SELECT
				{
					for (j = 0; Vn[i] != null; j++) {
						if (Vn[j].equals(b.substring(0, 1))) // 找下标j
						{
							break;
						}
					}
					for (int k = 0; follow[j][k] != '\0'; k++) {
						if (inChar(a, follow[j][k])) {
							a[flag] = follow[j][k]; // 将表达式左侧Vn的follow 加入select
							flag++;
						}
					}
					break;
				} else {
					temp = 0;
					break;
				}
			}
		}
	}

	// 判断 x 是否在 字符串 a 中，在返回 false，不在返回 true
	static boolean inChar(char a[], char x) {
		for (int i = 0; a[i] != '\0'; i++) {
			if (a[i] == x)
				return false;
		}
		return true;
	}

	// 判断字符 x 是否在字符串 a 中，在返回 false，不在返回 true
	private static boolean inString(String a[], char x) {
		for (int i = 0; a[i] != null; i++) {
			if (a[i].equals(String.valueOf(x)))
				return false;// 在
		}
		return true;// 不在
	}

	// 把x加入字符串组 firstVn[]的最后
	private static void addString(String firstVn[], String x) {
		int i;
		for (i = 0; firstVn[i] != null; i++) {
		}
		firstVn[i] = x;
	}

	// 判断x 是否已完成 first
	private static int firstComplete(char x) {
		int i;
		for (i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // 如果找到Vn[i]=x
			{
				if (firstComplete[i] == 1) // 如果完成判断，置1
					return i; // 并返回该Vn的下标i
				else
					return -1; // 没有完成判断，返回
			}
		}
		return -1; // 没有找到，返回
	}

	// 判断 x是否已完成 follow
	private static int followComplete(char x) {
		for (int i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // 如果找到Vn[i]=x
			{
				if (followComplete[i] == 1) // 如果完成判断，置1
					return i; // 并返回该Vn的下标i
				else
					return -1; // 没有完成判断，返回
			}
		}
		return -1; // 没有找到，返回
	}

	// 把相应终结符添加到first[]中, pos为该下标
	private static int addElementFirst(char a[], int pos, int flag) {
		for (int i = 0; first[pos][i] != '\0'; i++) {
			if (inChar(a, first[pos][i]) && first[pos][i] != '#') {
				a[flag] = first[pos][i];
				flag++;// 记录每个Vn的first集元素个数
			}
		}
		return flag;
	}

	// 把相应终结符添加到 follow[]中, pos为该下标
	private static int addElementFollow(char a[], int pos, int flag) {
		for (int i = 0; follow[pos][i] != '\0'; i++) {
			if (inChar(a, follow[pos][i])) // 把元素逐一添加到FOLLOW[]中
			{
				a[flag] = follow[pos][i];
				flag++;
			}
		}
		return flag;
	}

	// 判断该Vn的FIRST集合是否有空
	private static boolean isEmpty(char x) {
		int i;
		for (i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // 找到Vn的下标i
			{
				break;
			}
		}
		for (int j = 0; first[i][j] != '\0'; j++) // 根据找到的i值查找对应Vn的FIRST集是否含空
		{
			if (first[i][j] == '#')// 有空
				return true;
		}
		return false;
	}


}
