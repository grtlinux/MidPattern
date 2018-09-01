package org.tain.shuffle01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Voca800 {

	private static boolean flag;

	private static String DEFAULT_BASE_DIR;
	private static String DEFAULT_DATA_FILE;
	private static int DEFAULT_NUMBER_SHOW;

	static {
		flag = true;
		DEFAULT_BASE_DIR = "C:/hanwha/workspace_eng01/Shuffle01/";
		DEFAULT_DATA_FILE = "02.Voca800.txt";
		DEFAULT_NUMBER_SHOW = 10;
	}

	///////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	private static class VocaPattern {
		private String seq;
		private String word;   // eng
		private String mean;   // kor
		private int num;       // number in exam
		private List<String> examples;   // TODO KANG-20180831: not use
		public VocaPattern(String line) throws Exception {
			try {
				String[] parts = line.split("\\|");
				if (parts.length < 3)
					throw new Exception("wrong line - 1");

				this.seq = parts[0].trim();
				this.word = parts[1].trim();
				this.mean = parts[2].trim();
				this.num = -1;
			} catch (Exception e) {
				// e.printStackTrace();
				throw new Exception("wrong line - 2");
			}
		}
		public String getSeq() {
			return seq;
		}
		public void setSeq(String seq) {
			this.seq = seq;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getMean() {
			return mean;
		}
		public void setMean(String mean) {
			this.mean = mean;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public List<String> getExamples() {
			return examples;
		}
		public void setExamples(List<String> examples) {
			this.examples = examples;
		}
		public String toString() {
			if (!flag) {
				return String.format("(%s) [%s:%s] (%d)"
						, this.getSeq()
						, this.getWord()
						, this.getMean()
						, this.getNum()
						);
			} else {
				return String.format("%s", this.getSeq());
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private static String baseDir = null;
	private static String dataFile = null;

	private static boolean runEng = false;

	private static int numShow = 0;

	private static void setVariables() throws Exception {
		String  param = null;

		if (flag) {
			// for -basedir -datafile
			baseDir = DEFAULT_BASE_DIR;
			dataFile = DEFAULT_DATA_FILE;

			if ((param = Utils.getValue("-basedir", 0)) != null) {
				baseDir = param;
			}

			if ((param = Utils.getValue("-datafile", 0)) != null) {
				dataFile = param;
			}

			System.out.printf("##### baseDir  : [%s]%n", baseDir);
			System.out.printf("##### dataFile : [%s]%n", dataFile);
		}

		if (flag) {
			// for -runeng
			runEng = false;

			if (Utils.getValues("-runeng") != null) {
				runEng = true;
			}
		}

		if (flag) {
			// for -numshow
			numShow = DEFAULT_NUMBER_SHOW;

			if ((param = Utils.getValue("-numshow", 0)) != null) {
				numShow = Integer.parseInt(param);
			}

			System.out.printf("##### numShow : [%d]%n", numShow);
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private static void initialize(String[] args) throws Exception {

		if (flag) {
			args = new String[] {
					"-basedir", "./",
					"-datafile", "02.Voca800.txt",
					"-runeng",
					"-numshow", "10",
					};
		}

		Utils.setParams(args);

		setVariables();
	}

	///////////////////////////////////////////////////////////////////////////

	private static List<VocaPattern> getListAll(final String fileName) throws Exception {
		List<VocaPattern> listAll = new ArrayList<VocaPattern>();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		String line;
		VocaPattern pattern;

		while ((line = bufferedReader.readLine()) != null) {
			if (!flag) System.out.println(">>>>> " + line);

			try {
				pattern = new VocaPattern(line);
			} catch (Exception e) {
				continue;
			}

			if (!flag) System.out.println(">>>>> " + pattern);

			listAll.add(pattern);
		}

		bufferedReader.close();

		return listAll;
	}

	///////////////////////////////////////////////////////////////////////////

	private static String getStringItems(List<String> lstItems) throws Exception {
		StringBuffer sb = new StringBuffer(lstItems.get(0));
		int maxIdx = lstItems.size();
		String glue = " | ";

		for (int i=1; i < maxIdx; i++) {
			sb.append(glue);
			sb.append(lstItems.get(i));
		}

		return sb.toString();
	}

	private static String getStringChoices(List<String> lstChoices) throws Exception {
		StringBuffer sb = new StringBuffer();
		int maxIdx = lstChoices.size();

		for (int i=0; i < maxIdx; i++) {
			if (i % 2 == 0)
				sb.append("\n");
			sb.append(String.format("     %2d) %-30s", (i+1), lstChoices.get(i)));
		}

		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////

	private static void process() throws Exception {

		List<VocaPattern> listAll = null;
		List<VocaPattern> list = null;

		// scanner
		Scanner scanner = new Scanner(System.in);

		if (flag) {
			// listAll
			listAll = getListAll(baseDir + dataFile);
		}

		if (flag) {
			List<String> lstItems = new ArrayList<>();
			List<String> lstChoices = new ArrayList<>();
			List<String> lstCorrectAnswer = new ArrayList<>();
			List<String> lstYourAnswer = null;
			List<String> lstYourScore = new ArrayList<>();
			String line = null;
			boolean flgPerfect;

			// test loop
			int idx = 0;
			while (flag) {
				if (idx >= listAll.size())
					break;

				int begIdx = idx;
				int endIdx = Math.min(idx + numShow, listAll.size());
				flgPerfect = true;

				list = listAll.subList(begIdx, endIdx);
				if (!flag) System.out.println(">>>>> " + list);

				// 1st shuffle
				Collections.shuffle(list, new Random(new Date().getTime()));

				// set number
				int num = 0;
				for (VocaPattern pattern : list) {
					pattern.setNum(++num);
				}

				// set choices
				lstChoices.clear();
				for (VocaPattern pattern : list) {
					if (runEng) {
						lstChoices.add(pattern.getMean());
					} else {
						lstChoices.add(pattern.getWord());
					}
				}

				// 2nd shuffle
				Collections.shuffle(list, new Random(new Date().getTime()));

				// set items, correct answers
				lstItems.clear();
				lstCorrectAnswer.clear();
				for (VocaPattern pattern : list) {
					if (runEng) {
						lstItems.add(pattern.getWord());
					} else {
						lstItems.add(pattern.getMean());
					}

					lstCorrectAnswer.add(String.valueOf(pattern.getNum()));
				}

				// display items
				System.out.printf("\n\nMEANING [ %s ]%n", getStringItems(lstItems));
				if (!flag) System.out.println(">>>>> " + lstCorrectAnswer);

				// display choices
				System.out.printf("%s%n", getStringChoices(lstChoices));

				// get inputs
				line = scanner.nextLine().trim();
				if (line.equalsIgnoreCase("Quit"))
					break;
				lstYourAnswer = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
				if (!flag) System.out.println(">>>>> " + lstYourAnswer);

				// make score
				lstYourScore.clear();
				for (int i=0; i < lstCorrectAnswer.size(); i++) {
					String q = lstCorrectAnswer.get(i);
					if (i >= lstYourAnswer.size()) {
						lstYourScore.add(i, "X");
						flgPerfect = false;
						continue;
					}
					String a = lstYourAnswer.get(i);
					if (a.equalsIgnoreCase(q)) {
						lstYourScore.add(i, "O");
					} else {
						lstYourScore.add(i, "X");
						flgPerfect = false;
					}
				}

				// print results
				if (flag) System.out.println(">>>>> CA: " + lstCorrectAnswer);
				if (flag) System.out.println(">>>>> YA: " + lstYourAnswer);
				if (flag) System.out.println(">>>>> YS: " + lstYourScore);

				if (flgPerfect) {
					if (flag) System.out.println(">>>>> Your score is perfect!!!");
					idx += numShow;
				}
			}
		}

		scanner.close();
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + Utils.getClassInfo());

		initialize(args);

		if (flag) process();
	}
}
