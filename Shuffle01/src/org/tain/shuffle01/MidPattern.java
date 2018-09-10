package org.tain.shuffle01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MidPattern {

	private static boolean flag;

	private static int DEFAULT_WAIT_TIME;
	private static String DEFAULT_BASE_DIR;
	private static String DEFAULT_DATA_FILE;
	private static int DEFAULT_CNT_EXAM;

	static {
		flag = true;
		DEFAULT_WAIT_TIME = 5000;
		DEFAULT_BASE_DIR = "C:/hanwha/workspace_eng01/Shuffle01/";
		DEFAULT_DATA_FILE = "03.MidPattern.txt";
		DEFAULT_CNT_EXAM = 10;
	}

	///////////////////////////////////////////////////////////////////////////

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	private static class Pattern {
		private String seq;
		private String pattern;   // eng
		private String meaning;   // kor
		private List<String> examples;
		public Pattern(String line) throws Exception {
			try {
				String[] parts = line.split("\\|");
				if (parts.length < 4)
					throw new Exception("wrong line - 1");

				this.seq = parts[0].trim();
				this.pattern = parts[1].trim();
				this.meaning = parts[2].trim();
				this.examples = new ArrayList<String>();
				for (int i=3; i < parts.length; i++) {
					this.examples.add(parts[i].trim());
				}
			} catch (Exception e) {
				// e.printStackTrace();
				throw new Exception("wrong line - 2");
			}
		}
		public Pattern(String seq, String pattern, String meaning, List<String> examples) {
			this.seq = seq;
			this.pattern = pattern;
			this.meaning = meaning;
			this.examples = examples;
		}
		public String getSeq() {
			return seq;
		}
		public void setSeq(String seq) {
			this.seq = seq;
		}
		public String getPattern() {
			return pattern;
		}
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
		public String getMeaning() {
			return meaning;
		}
		public void setMeaning(String meaning) {
			this.meaning = meaning;
		}
		public List<String> getExamples() {
			return examples;
		}
		public void setExamples(List<String> examples) {
			this.examples = examples;
		}
		public String toString() {
			return String.format("(%s) [%s:%s] -> %s"
					, this.getSeq()
					, this.getPattern()
					, this.getMeaning()
					, this.getExamples());
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private static String baseDir = null;
	private static String dataFile = null;

	private static int begIdx = 0;
	private static int endIdx = 0;

	private static boolean runShuffle = false;

	private static boolean runAuto = false;
	private static long waitTime = DEFAULT_WAIT_TIME;

	private static boolean runExam = false;
	private static int cntExam = 0;

	private static void setVariables() throws Exception {
		String param = null;

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
		}

		if (flag) {
			// for -range
			begIdx = 1 - 1;
			endIdx = 99999;

			if ((param = Utils.getValue("-range", 0)) != null) {
				begIdx = Integer.parseInt(param) - 1;
			}

			if ((param = Utils.getValue("-range", 1)) != null) {
				endIdx = Integer.parseInt(param);
			}
		}

		if (flag) {
			// for -shuffle
			runShuffle = false;

			if (Utils.getValues("-shuffle") != null) {
				runShuffle = true;
			}
		}

		if (flag) {
			// for -auto
			runAuto = false;
			waitTime = DEFAULT_WAIT_TIME;

			if (Utils.getValues("-auto") != null) {
				runAuto = true;

				if ((param = Utils.getValue("-auto", 0)) != null) {
					waitTime = Integer.parseInt(param) * 1000;
					if (waitTime == 0) waitTime = DEFAULT_WAIT_TIME;
				}
			}

			// for -exam
			runExam = false;
			cntExam = DEFAULT_CNT_EXAM;

			if (Utils.getValues("-exam") != null) {
				runExam = true;

				if ((param = Utils.getValue("-exam", 0)) != null) {
					cntExam = Integer.parseInt(param);
				}
			}
		}
	}

	private static void initialize(String[] args) throws Exception {

		if (flag) {
			args = new String[] {
					"-basedir", "./",
					"-datafile", "03.MidPattern.txt",

					"-range", "001", "050",
					"-range", "050", "100",
					"-range", "100", "150",
					"-range", "150", "200",

					"-range", "001", "200",    // 2
					//"-range",   "1", "100",    // 1
					//"-range",   "1", "200",    // 0

					"--shuffle",
					"--auto", "1",
					"-exam", "1",
					};
		}

		Utils.setParams(args);

		setVariables();
	}

	///////////////////////////////////////////////////////////////////////////

	private static List<Pattern> getListAll(final String fileName) throws Exception {
		List<Pattern> listAll = new ArrayList<Pattern>();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		String line;
		Pattern pattern;

		while ((line = bufferedReader.readLine()) != null) {
			if (!flag) System.out.println(">>>>> " + line);

			try {
				pattern = new Pattern(line);
			} catch (Exception e) {
				continue;
			}

			if (!flag) System.out.println(">>>>> " + pattern);

			listAll.add(pattern);
		}

		bufferedReader.close();

		return listAll;
	}

	private static void process() throws Exception {

		List<Pattern> listAll = null;
		List<Pattern> list = null;

		// scanner
		Scanner scanner = new Scanner(System.in);

		if (flag) {
			// listAll
			System.out.printf("##### data file : [%s] [%s]%n", baseDir, dataFile);

			listAll = getListAll(baseDir + dataFile);
		}

		if (flag) {
			// list
			endIdx = Math.min(endIdx, listAll.size());

			System.out.printf("##### range (%d ~ %d)%n", begIdx + 1, endIdx);

			list = listAll.subList(begIdx, endIdx);

			if (!flag) {
				// confirm print
				System.out.println("##### print list sequence");
				for (Pattern pattern : list) {
					System.out.printf(">>>>>>>>>> %s%n", pattern);
				}
			}
		}

		if (flag) {
			// shuffle
			if (runShuffle) {
				long randShuffle = new Date().getTime();

				System.out.printf("##### shuffle list. randShuffle = %d%n", randShuffle);

				Collections.shuffle(list, new Random(randShuffle));
			}
		}

		if (flag) {
			// question
			System.out.printf("##### QUESTION %n");

			for (Pattern pattern : list) {

				System.out.printf("%n%s) %-40s (%s) ...."
						, pattern.getSeq()
						, pattern.getPattern()
						, pattern.getMeaning()
						);

				if (runAuto) {
					try { Thread.sleep(waitTime); } catch (InterruptedException e) {}
					System.out.println();
				} else {
					scanner.nextLine();
				}

				if (runExam) {
					List<String> examples = pattern.getExamples();
					for (int i=0; i < cntExam && i < examples.size(); i++) {
						System.out.printf("\t\t\t(%d) %s%n", i, examples.get(i));
					}
				}
			}
		}

		scanner.close();
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		initialize(args);

		if (flag) process();
	}
}
