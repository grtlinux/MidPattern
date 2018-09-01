package org.tain.shuffle01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * USAGE : java org.tain.shuffle01.Shuffle --shuffle --auto 1 -exam 1 -range 101 110 -basedir "../" -datafile "03.MidPattern.txt"
 *
 * @author KangSeok_Mac
 *
 */
public class Shuffle {

	private static boolean flag;
	private static int DEFAULT_WAIT_TIME;
	private static String DEFAULT_BASE_DIR;

	static {
		Shuffle.flag = true;
		Shuffle.DEFAULT_WAIT_TIME = 5000;
		Shuffle.DEFAULT_BASE_DIR = "C:/hanwha/workspace_eng01/Shuffle01/";
	}

	////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	private static class MidPattern {
		private String seq;
		private String pattern;   // eng
		private String meaning;   // kor
		private List<String> examples;
		public MidPattern(String line) throws Exception {
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
		public MidPattern(String seq, String pattern, String meaning, List<String> examples) {
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

	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////

	private static String getInfo() throws Exception {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		sb.append(" - ");
		sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	////////////////////////////////////////////////////////

	private static final Map<String, List<String>> params = new HashMap<>();

	private static void getArguments(String[] args) throws Exception {
		List<String> options = null;
		for (String arg : args) {
			if (arg.charAt(0) == '-') {
				if (arg.length() < 2) {
					throw new IllegalArgumentException("Error at argument " + arg);
				}
				options = new ArrayList<>();
				params.put(arg.substring(0), options);
			} else if (options != null) {
				options.add(arg);
			} else {
				throw new IllegalArgumentException("Illegal parameter usage.");
			}
		}
	}

	private static void setParameter() throws Exception {

	}

	private static void setInitalize(String[] args) throws Exception {
		if (flag) getArguments(args);
		if (flag) setParameter();
	}

	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////

	private static List<MidPattern> getList(final String fileName) throws Exception {
		List<MidPattern> listAll = new ArrayList<MidPattern>();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		String line;
		MidPattern pattern;

		while ((line = bufferedReader.readLine()) != null) {
			if (!flag) System.out.println(">>>>> " + line);

			try {
				pattern = new MidPattern(line);
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
		// all list
		String baseDir = null;
		String dataFile = null;
		List<MidPattern> listAll = null;

		// shuffle
		int begIdx = 0;
		int endIdx = 0;
		List<MidPattern> list = null;

		// scanner
		Scanner scanner = new Scanner(System.in);
		List<String> options = null;

		if (flag) {
			// base dir / data file
			baseDir = DEFAULT_BASE_DIR;
			dataFile = "03.MidPattern.txt";

			if ((options = params.get("-basedir")) != null) {
				baseDir = options.get(0);
			}

			if ((options = params.get("-datafile")) != null) {
				dataFile = options.get(0);
			}

			listAll = getList(baseDir + dataFile);
		}

		if (flag) {
			// range
			begIdx = 1 - 1;
			endIdx = listAll.size();
			if ((options = params.get("-range")) != null) {
				if (options.size() == 1) {
					begIdx = Integer.parseInt(options.get(0)) - 1;
				} else if (options.size() >= 2) {
					begIdx = Integer.parseInt(options.get(0)) - 1;
					endIdx = Integer.parseInt(options.get(1));
				}
			}

			list = listAll.subList(begIdx, endIdx);

			System.out.printf("##### range (%d ~ %d)%n", begIdx + 1, endIdx);

			if (!flag) {
				// confirm print
				System.out.println("##### print list sequence");
				for (MidPattern pattern : list) {
					System.out.printf(">>>>>>>>>> %s%n", pattern);
				}
			}
		}

		if (flag) {
			// shuffle
			if ((options = params.get("-shuffle")) != null) {
				long randShuffle = new Date().getTime();
				System.out.printf("##### shuffle list. randShuffle=%d%n", randShuffle);
				Collections.shuffle(list, new Random(randShuffle));
			}
		}

		if (flag) {
			// question
			System.out.println("##### questions");

			for (MidPattern pattern : list) {

				System.out.printf("%s) %-40s (%s) ...."
						, pattern.getSeq()
						, pattern.getPattern()
						, pattern.getMeaning()
						);

				if ((options = params.get("-auto")) != null) {
					long waitTime = DEFAULT_WAIT_TIME;
					if (options.size() > 0) {
						waitTime = Integer.parseInt(options.get(0)) * 1000;
						if (waitTime == 0) waitTime = DEFAULT_WAIT_TIME;
					}

					try { Thread.sleep(waitTime); } catch (InterruptedException e) {}
					System.out.println();
				} else {
					scanner.nextLine();
				}

				if ((options = params.get("-exam")) != null) {
					if (options.size() > 0) {
						int cnt = Integer.parseInt(options.get(0));
						List<String> examples = pattern.getExamples();
						for (int i=0; i < cnt && i < examples.size(); i++) {
							System.out.printf("\t\t\t(%d) %s%n", i, examples.get(i));
						}
					} else {
						for (String example : pattern.getExamples()) {
							System.out.printf("\t\t\t%s%n", example);
						}
					}
				}

				System.out.println();
			}
		}

		scanner.close();
	}

	private static void testScanner() throws Exception {

		if (!flag) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("* input two integer number: ");
			int i1 = scanner.nextInt();
			int i2 = scanner.nextInt();
			System.out.printf(">>>>> %d %d%n%n", i1, i2);
			System.out.println("* input two double number: ");
			double d1 = scanner.nextDouble();
			double d2 = scanner.nextDouble();
			System.out.printf(">>>>> %f %f%n%n", d1, d2);
			scanner.close();
		}

		if (!flag) {
			Scanner scanner = new Scanner(System.in);
			String line = "";
			do {
				System.out.printf("input line('q' is quit) > ");
				line = scanner.nextLine();
				System.out.printf("the input line is [%s]%n", line);
			} while(!line.equalsIgnoreCase("q"));
			System.out.println("exit this program...");
			scanner.close();
		}

		if (!flag) {
			Scanner scanner = new Scanner(System.in);
			String line = "";
			do {
				System.out.printf("input the answers of the above(if exit, print 'quit') >");
				line = scanner.nextLine();

				String[] nums = line.split("\\s+");
				List<String> list = new ArrayList<String>(Arrays.asList(nums));

				System.out.println(">>>>> " + Arrays.toString(nums));
				System.out.println(">>>>> " + list);
			} while (!line.equalsIgnoreCase("Quit"));
			scanner.close();
		}

		if (flag) {
			Scanner scanner = new Scanner(System.in);
			String line = "";
			while (true) {
				System.out.printf("input the questions (if exit, print 'quit') >");
				line = scanner.nextLine().trim();
				if (line.equalsIgnoreCase("Quit"))
					break;
				List<String> lstQuestion = new ArrayList<String>(Arrays.asList(line.split("\\s+")));

				System.out.printf("input the answers (if exit, print 'quit') >");
				line = scanner.nextLine().trim();
				if (line.equalsIgnoreCase("Quit"))
					break;
				List<String> lstAnswer = new ArrayList<String>(Arrays.asList(line.split("\\s+")));

				List<String> lstCheck = new ArrayList<String>();

				for (int i=0; i < lstQuestion.size(); i++) {
					String q = lstQuestion.get(i);
					if (i >= lstAnswer.size()) {
						lstCheck.add(i, "X");
						continue;
					}
					String a = lstAnswer.get(i);
					if (a.equalsIgnoreCase(q)) {
						lstCheck.add(i, "O");
					} else {
						lstCheck.add(i, "X");
					}
				}

				System.out.println(">>>>> Q: " + lstQuestion);
				System.out.println(">>>>> A: " + lstAnswer);
				System.out.println(">>>>> C: " + lstCheck);
			}
			scanner.close();

			System.out.println("exit this program");
		}
	}

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getInfo());

		setInitalize(args);

		if (!flag) process();    // KANG-20180829
		if (flag) testScanner();  // KANG-20180831
	}
}
