package static_variables;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StaticVariables {
	public static final ArrayList<String> files = new ArrayList<>(Arrays.asList("sheep", "muski", "zenski", "instrument", "sum", "generated signal", "10000Hz"));
	public static final ArrayList<String> powerOfTwo = new ArrayList<>(Arrays.asList("32" ,"64", "128", "256", "512", "1024"));
	public static final ArrayList<String> functions = new ArrayList<>(Arrays.asList("none", "Hamming", "Hanning"));
}
