package application;

import java.util.Vector;

public abstract class LL1 {
	
	static String Vn[] = null; // ���з��ս������
	static Vector<String> P = null; // �ַ�����������������Ԥ�������
	static int firstComplete[] = null; // �洢���жϹ� first ������
	static char first[][] = null; // �洢��� first ���������
	static int followComplete[] = null;// �洢���жϹ� follow ������
	static char follow[][] = null; // �洢��� follow ���
	static char select[][] = null; // select[][]�洢��� select ���
	static char yn_null[] = null; // �洢�ܷ��Ƴ���
	
	static int addFirst(char a[], String b, String firstVn[], int flag)// ���first��
	// addFirst(first[i], Vn[i], firstVn, flag)
	// ���� FIRST**���ݹ飩
	{
		if (inString(firstVn, b.charAt(0))) // �ж�firstVn�ַ������Ƿ����b�ĵ�һ���ַ���������Ϊtrue
		// boolean inString(String a[], char b)
		{
			addString(firstVn, b);// ���ڣ��� ��i��Vn�����ַ����� firstVn[]
		} else {
			return flag;
		}

		for (int i = 0; i < P.size(); i++) // P.size()�ǲ���ʽ����Ŀ
		{
			String t = (String) P.elementAt(i);// ������i��Ԫ�ظ�ֵ���ַ���t��tΪ����ʽ�ַ���
			for (int k = 2; k < t.length(); k++)// t.length()�Ǳ�������ʽ�ĳ��ȣ��Ӳ���ʽ�ұߵ�һ���ַ���ʼ�жϣ�T->+SB��
			{
				if (t.substring(0, 1).equals(b)) // ������i������ʽ���ַ���Vn[i]���
				{
					if (t.charAt(k) > 'Z' || t.charAt(k) < 'A')// ���������ս��������A~Z��,����
					{
						if (flag == 0 || inChar(a, t.charAt(k))) // ���ʽ�Ҷ˵�
																	// k-1���ַ����ڸ�Vn��first���У������ظ����
																	// ����A->aB,A->aC
						{
							if (t.charAt(k) == '#')// ��ʱ
							{
								if (k + 1 == t.length()) // �����һ���ַ�
								{
									a[flag] = t.charAt(k);// �����Vn��first��
									flag++;
								}
								int flag1 = 0;
								for (int j = 0; yn_null[j] != '\0'; j++)// ����Vn����yn_null[]
								{
									if (yn_null[j] == b.charAt(0))// ѭ���ж�Vn[i]�ܷ��Ƴ���
									{
										flag1 = 1;
										break;// �Ѿ���ѭ������ʱ�����Ƴ��յ�Vn������yn_null[]������Ҫ��ִ��
									}
								}
								if (flag1 == 0)// û�ҵ�������Ҫ�����Ƴ��յ�Vn����yn_null[]
								{
									int j;
									for (j = 0; yn_null[j] != '\0'; j++) {
									}
									yn_null[j] = b.charAt(0);// ��j��yn_null[]�����һ��λ�ã�����Vn
								}
								continue;// �������Ʋ�����һ���ַ�
							}
							// end if (t.charAt(k) == '#')
							else// �����Ϊ�գ����ս��ֱ�Ӽ��� first[],��a[]
							{
								a[flag] = t.charAt(k);
								flag++;
								break;
							}
						}
						break;
					} // end if(t.charAt(k) > 'Z' || t.charAt(k) < 'A')
					else // �����ĵ�һ���ַ�Ϊ���ս�������
					{
						if (!inString(Vn, t.charAt(k))) // ����������ĸ��ַ���Vn
						// boolean inString(String a[], char b)
						{
							int p = firstComplete(t.charAt(k));// ���÷��ս����
																// first���Ѿ����

							if (p != -1) // �Ѿ����first�ж�
							{
								flag = addElementFirst(a, p, flag);// ֱ�Ӽ�������first

							}
							// �����û������ж�
							else if ((flag = addFirst(a, String.valueOf(t.charAt(k)), firstVn, flag)) == -1)// �ݹ�
								return -1;
							int flag1 = 0;
							for (int j = 0; yn_null[j] != '\0'; j++)// �����ս����first����#
							{
								if (yn_null[j] == t.charAt(k)) {
									flag1 = 1;
									break;
								}
							}
							if (flag1 == 1)// �����ս���� first ���Ƴ���
							{
								if (k + 1 == t.length() && inChar(a, '#'))// ֮���޷��ţ�������first�����޿ա���S->A,A->#
								{
									a[flag] = '#';// first �м���#
									flag++;
								}
								continue;// �ж���һ���ַ�
							} else {
								break;
							}
						}
						// endif(!inString(Vn, t.charAt(k))) //����������ĸ��ַ���Vn
						else// �����ĸ��ַ�Ϊ����
						{
							return -1;
						}
					} // ���ս�����
				}
			} // һ������ʽ�������
		} // ���в���ʽ�������
		return flag;
	}

	static int addFollow(char a[], String b, String followVn[], int flag)
	// addFollow(follow[i], Vn[i], followVn, flag)
	// ���� FOLLOW���ݹ飩
	{
		if (inString(followVn, b.charAt(0))) {
			addString(followVn, b);
		} else {
			return flag;
		}
		if (b.equals("S")) // ��Ϊ S ʱ#ֱ�Ӵ��� follow
		{
			a[flag] = '#';
			flag++;
		}
		for (int i = 0; i < P.size(); i++) {
			String t = (String) P.elementAt(i);
			for (int j = 2; j < t.length(); j++) {
				if (t.charAt(j) == b.charAt(0) && j + 1 < t.length()) // ����ʽ�Ҷ˵ĸ�Vn�������һ���ַ�
				{
					if (t.charAt(j + 1) != '\0') {
						if ((t.charAt(j + 1) > 'Z' || t.charAt(j + 1) < 'A'))// ��Vn��һλ���ս��������S->Ac��c
						{
							if (flag == 0 || inChar(a, t.charAt(2))) {
								a[flag] = t.charAt(j + 1);// ����һλ����FOLLOW��
								flag++;
							}
						} else// ��һλΪVn
						{
							int k;
							for (k = 0; Vn[k] != null; k++) {
								if (Vn[k].equals(String.valueOf(t.charAt(j + 1)))) {
									break;// ��Ѱ��һ�����ս���� Vn λ��
								}
							}
							// �ҵ����±�k
							flag = addElementFirst(a, k, flag);// ����һ�����ս�� first�������� follow����a��ʱΪfollow[]

							for (k = j + 1; k < t.length(); k++) {
								if ((t.charAt(j + 1) > 'Z' || t.charAt(j + 1) < 'A'))// ��һλ����Vn�������ϣ�break
									break;
								else {
									if (isEmpty(t.charAt(k))) // ��һ�����ս�����Ƴ��գ��ѱ��ʽ���Vn��follow���������� follow��
									{
										int p = followComplete(t.charAt(0));// �õ�����ʽ��Vn���±�
										if (p != -1) {
											flag = addElementFollow(a, p, flag);// flag���ڵݹ��ж�
										} else if ((flag = addFollow(a, String.valueOf(t.charAt(0)), followVn,
												flag)) == -1)// �ݹ�
											return -1;
									} else {
										break;
									}
								}
							}
						}
					} else // �����ķ�
					{
						return -1;
					}
				}
				if (t.charAt(j) == b.charAt(0) && j + 1 == t.length())
				// �ұ�ֻ��һ��Vn��һ�����ս�����Ƴ��գ��ѱ��ʽ���Vn��follow ���������� follow ��
				// ����A->B,FOLLOW(A)����FOLLOW(B)
				{
					int p = followComplete(t.charAt(0));// �ҵ���Vn���±�p
					if (p != -1) {
						flag = addElementFollow(a, p, flag);// ���±�Ϊp��Vn��FOLLOW�����뵽��ǰFOLLOW��
					} else if ((flag = addFollow(a, String.valueOf(t.charAt(0)), followVn, flag)) == -1)
						return -1;
				}
			}
		}
		return flag;
	}

	static void addSelect(char a[], String b, int flag) // ���� SELECT
	// addSelect(select[i], (String) P.elementAt(i), flag);
	{
		int i = 2;// i���ڶ�λ����ʼλ��Ϊ�Ҷ˵�һ���ַ�
		int temp = 0;
		while (i < b.length()) // b.length()��һ������ʽ�ĳ���
		{
			if ((b.charAt(i) > 'Z' || b.charAt(i) < 'A') && b.charAt(i) != '#')// ���ս�����Ҳ�Ϊ�գ�
				                                                               //����A->b��A->+...
			{
				a[flag] = b.charAt(i);// ������ַ����뵽 Select�������� Select�� �ļ���
				break;
			} else if (b.charAt(i) == '#')// �ǿ�
			{
				int j;
				for (j = 0; Vn[i] != null; j++)// �����ʽ���ķ��ս���� follow ����
												// select������A->#���������FOLLOW(A)
				{
					if (Vn[j].equals(b.substring(0, 1))) // �ҵ�����ʽ��Vn���±�j
					{
						break;
					}
				}
				for (int k = 0; follow[j][k] != '\0'; k++) // ���ΰ���Vn��Follow�����뵽������ʽ��select��
				{
					if (inChar(a, follow[j][k])) {
						a[flag] = follow[j][k];
						flag++;
					}
				}
				break;
			} else if (b.charAt(i) >= 'A' && b.charAt(i) <= 'Z' && i + 1 < b.length())// ��Vn��������һ���ַ�
			{
				int j;
				for (j = 0; Vn[i] != null; j++) {
					if (Vn[j].equals(String.valueOf(b.charAt(i))))// ��һ�뵱ǰVn�Ƚ��ҵ��±�j
					{
						break;// �ҵ�������ѭ��
					}
				}
				for (int k = 0; first[j][k] != '\0'; k++) {
					if (inChar(a, first[j][k]))// �ѱ��ʽ�Ҳ����з��ս���� first ������
					{
						if (first[j][k] == '#')// first �д��ڿգ����Ҳ��Vn���Ƴ���
						{
							temp = 1;
						} else // �Ҳ��Vn�Ʋ�����
						{
							a[flag] = first[j][k];// ����
							flag++;
						}
					}
				}
				if (temp == 1)// ���Ҳ����з��ս���� first �е�#ȥ��
				{
					i++;
					temp = 0;// ��Ϊ0
					continue;
				} else {
					temp = 0;
					break;
				}
			} else if (b.charAt(i) >= 'A' && b.charAt(i) <= 'Z' && i + 1 >= b.length())// ��Vn���Ѿ������һ���ַ�
			{
				int j;
				for (j = 0; Vn[i] != null; j++) {
					if (Vn[j].equals(String.valueOf(b.charAt(i)))) // �̶�Vn[i]���±�j
					{
						break;
					}
				}
				for (int k = 0; first[j][k] != '\0'; k++) {
					if (inChar(a, first[j][k])) {
						if (first[j][k] == '#') {
							temp = 1;// ���ʽ�Ҳ����Ƴ��գ����1
						} else {
							a[flag] = first[j][k];// �����Ƴ��գ�ֱ�ӽ� first ������ select��

							flag++;
						}
					}
				}
				if (temp == 1)// ���ʽ�Ҳ����Ƴ��գ���Ҫ���󲿵�FOLLOW����SELECT
				{
					for (j = 0; Vn[i] != null; j++) {
						if (Vn[j].equals(b.substring(0, 1))) // ���±�j
						{
							break;
						}
					}
					for (int k = 0; follow[j][k] != '\0'; k++) {
						if (inChar(a, follow[j][k])) {
							a[flag] = follow[j][k]; // �����ʽ���Vn��follow ����select
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

	// �ж� x �Ƿ��� �ַ��� a �У��ڷ��� false�����ڷ��� true
	static boolean inChar(char a[], char x) {
		for (int i = 0; a[i] != '\0'; i++) {
			if (a[i] == x)
				return false;
		}
		return true;
	}

	// �ж��ַ� x �Ƿ����ַ��� a �У��ڷ��� false�����ڷ��� true
	private static boolean inString(String a[], char x) {
		for (int i = 0; a[i] != null; i++) {
			if (a[i].equals(String.valueOf(x)))
				return false;// ��
		}
		return true;// ����
	}

	// ��x�����ַ����� firstVn[]�����
	private static void addString(String firstVn[], String x) {
		int i;
		for (i = 0; firstVn[i] != null; i++) {
		}
		firstVn[i] = x;
	}

	// �ж�x �Ƿ������ first
	private static int firstComplete(char x) {
		int i;
		for (i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // ����ҵ�Vn[i]=x
			{
				if (firstComplete[i] == 1) // �������жϣ���1
					return i; // �����ظ�Vn���±�i
				else
					return -1; // û������жϣ�����
			}
		}
		return -1; // û���ҵ�������
	}

	// �ж� x�Ƿ������ follow
	private static int followComplete(char x) {
		for (int i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // ����ҵ�Vn[i]=x
			{
				if (followComplete[i] == 1) // �������жϣ���1
					return i; // �����ظ�Vn���±�i
				else
					return -1; // û������жϣ�����
			}
		}
		return -1; // û���ҵ�������
	}

	// ����Ӧ�ս����ӵ�first[]��, posΪ���±�
	private static int addElementFirst(char a[], int pos, int flag) {
		for (int i = 0; first[pos][i] != '\0'; i++) {
			if (inChar(a, first[pos][i]) && first[pos][i] != '#') {
				a[flag] = first[pos][i];
				flag++;// ��¼ÿ��Vn��first��Ԫ�ظ���
			}
		}
		return flag;
	}

	// ����Ӧ�ս����ӵ� follow[]��, posΪ���±�
	private static int addElementFollow(char a[], int pos, int flag) {
		for (int i = 0; follow[pos][i] != '\0'; i++) {
			if (inChar(a, follow[pos][i])) // ��Ԫ����һ��ӵ�FOLLOW[]��
			{
				a[flag] = follow[pos][i];
				flag++;
			}
		}
		return flag;
	}

	// �жϸ�Vn��FIRST�����Ƿ��п�
	private static boolean isEmpty(char x) {
		int i;
		for (i = 0; Vn[i] != null; i++) {
			if (Vn[i].equals(String.valueOf(x))) // �ҵ�Vn���±�i
			{
				break;
			}
		}
		for (int j = 0; first[i][j] != '\0'; j++) // �����ҵ���iֵ���Ҷ�ӦVn��FIRST���Ƿ񺬿�
		{
			if (first[i][j] == '#')// �п�
				return true;
		}
		return false;
	}


}
