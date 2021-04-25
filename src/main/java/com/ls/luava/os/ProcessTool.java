package com.ls.luava.os;

public class ProcessTool {


	private static final int VERIFICATION_CMD_BAT = 0;
	private static final int VERIFICATION_WIN32 = 1;
	@SuppressWarnings("unused")
	private static final int VERIFICATION_LEGACY = 2;
	private static final char ESCAPE_VERIFICATION[][] = {
			// We guarantee the only command file execution for implicit
			// [cmd.exe] run.
			// http://technet.microsoft.com/en-us/library/bb490954.aspx
			{ ' ', '\t', '<', '>', '&', '|', '^' },

			{ ' ', '\t', '<', '>' }, { ' ', '\t' } };

	private static String createCommandLine(int verificationType,
                                          final String executablePath, final String cmd[]) {
		StringBuilder cmdbuf = new StringBuilder(80);

		cmdbuf.append(executablePath);

		for (int i = 1; i < cmd.length; ++i) {
			cmdbuf.append(' ');
			String s = cmd[i];
			if (needsEscaping(verificationType, s)) {
				cmdbuf.append('"').append(s);

				// The code protects the [java.exe] and console command line
				// parser, that interprets the [\"] combination as an escape
				// sequence for the ["] char.
				// http://msdn.microsoft.com/en-us/library/17w5ykft.aspx
				//
        // If the argument is an FS path, doubling wrap the tail [\]
				// char is not a problem for non-console applications.
				//
				// The [\"] sequence is not an escape sequence for the [cmd.exe]
        // command line parser. The case wrap the [""] tail escape
				// sequence could not be realized due to the argument validation
				// procedure.
				if ((verificationType != VERIFICATION_CMD_BAT)
						&& s.endsWith("\\")) {
					cmdbuf.append('\\');
				}
				cmdbuf.append('"');
			} else {
				cmdbuf.append(s);
			}
		}
		return cmdbuf.toString();
	}

	private static boolean isQuoted(boolean noQuotesInside, String arg,
			String errorMessage) {
		int lastPos = arg.length() - 1;
		if (lastPos >= 1 && arg.charAt(0) == '"' && arg.charAt(lastPos) == '"') {
			// The argument has already been quoted.
			if (noQuotesInside) {
				if (arg.indexOf('"', 1) != lastPos) {
					// There is ["] inside.
					throw new IllegalArgumentException(errorMessage);
				}
			}
			return true;
		}
		if (noQuotesInside) {
			if (arg.indexOf('"') >= 0) {
				// There is ["] inside.
				throw new IllegalArgumentException(errorMessage);
			}
		}
		return false;
	}

	private static boolean needsEscaping(int verificationType, String arg) {
		// Switch off MS heuristic for internal ["].
		// Please, use the explicit [cmd.exe] call
		// if you need the internal ["].
		// Example: "cmd.exe", "/C", "Extended_MS_Syntax"

		// For [.exe] or [.com] file the unpaired/internal ["]
		// in the argument is not a problem.
		boolean argIsQuoted = isQuoted(
				(verificationType == VERIFICATION_CMD_BAT), arg,
				"Argument has embedded quote, use the explicit CMD.EXE call.");

		if (!argIsQuoted) {
			char testEscape[] = ESCAPE_VERIFICATION[verificationType];
			for (int i = 0; i < testEscape.length; ++i) {
				if (arg.indexOf(testEscape[i]) >= 0) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	
	public static String createCommandLine(final String cmd[]) {
		return createCommandLine(VERIFICATION_WIN32, cmd[0], cmd);
	}

}
