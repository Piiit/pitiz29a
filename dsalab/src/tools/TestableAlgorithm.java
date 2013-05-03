package tools;

public interface TestableAlgorithm {
	public void execute() throws Exception;
	public String getName();
	public int[] getArray();
	public void setArray(int[] array);
	public int getElementCount();
}
